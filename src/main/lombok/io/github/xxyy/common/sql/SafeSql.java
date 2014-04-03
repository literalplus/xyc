package io.github.xxyy.common.sql;

import io.github.xxyy.common.util.CommandHelper;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class providing methods to connect to SQL databases. Requires sql driver to be in CLASSPATH.
 *
 * @author xxyy98
 */
public class SafeSql implements AutoCloseable, PreparedStatementFactory {
    /**
     * A DEBUG switch to print every single query made by ANY {@link SafeSql} to {@link System#out}.
     */
    public static boolean debug = false;

    private static final Logger LOGGER = Logger.getLogger(SafeSql.class.getName());

    public final String dbName;
    /**
     * Connection maintained by this SafeSql.
     */
    @Getter
    @Setter
    private Connection currentConnection = null;
    /**
     * {@link SqlConnectable} providing connection data for this {@link SafeSql}.
     */
    private SqlConnectable authDataProvider;
    /**
     * A logger to print errors to
     */
    public Logger errLogger = null;

    /**
     * Constructs a new instance.
     *
     * @param pl {@link SqlConnectable} providing login data
     * @throws IllegalArgumentException If plug is {@code null}.
     */
    public SafeSql(SqlConnectable pl) {
        Validate.notNull(pl);
        this.authDataProvider = pl;
        this.dbName = authDataProvider.getSqlDb();
    }

    public static Logger getLogger(){
        return LOGGER;
    }

    /**
     * WARNING UNSAFE
     *
     * @param query QUERY to execute
     * @return ResultSet
     * @deprecated This does not provide the created {@link java.sql.PreparedStatement} so there is no way to close it. That may lead to severe memory leaks. Use {@link #executeQueryWithResult(String, Object...)} instead.
     */
    @Deprecated
    public ResultSet executeQuery(String query) {
        try {
            PreparedStatement stmnt = this.getAnyConnection().prepareStatement(query);
            ResultSet rtrn = stmnt.executeQuery();
            if (SafeSql.debug) {
                CommandHelper.sendMessageToOpsAndConsole("§6Query: " + query);
            }
            return rtrn;
        } catch (SQLException e) {
            this.formatAndPrintException(e, "§cException while trying to execute Query: '" + query + "'");
        }
        return null;
    }

    /**
     * This executes a normal update statement in this object's connection.
     *
     * @param query UPDATE to be executed
     * @return Whether the operation succeeded
     * @see #executeUpdateWithResult(String, Object...)
     */
    public boolean executeUpdate(String query) {
        PreparedStatement stmnt = null;
        try {
            stmnt = this.getAnyConnection().prepareStatement(query);
            stmnt.executeUpdate();
            if (SafeSql.debug) {
                CommandHelper.sendMessageToOpsAndConsole("§6Update: " + query);
            }
            return true;
        } catch (SQLException e) {
            this.formatAndPrintException(e, "§cException while trying to execute update: '" + query + "'");
        } finally {
            tryClose(stmnt);
        }
        return false;
    }

    /**
     * formats an exception, prints a line before it, then prints (to Ops &amp; console) &amp; logs it.
     *
     * @param e         Exception to use
     * @param firstLine A line describing the error, normally class &amp; method name - more efficient than getting the caller (some methods take up to
     *                  7s!)
     */
    public void formatAndPrintException(SQLException e, String firstLine) {
        CommandHelper.sendMessageToOpsAndConsole(firstLine);
        CommandHelper.sendMessageToOpsAndConsole("§4SQL Error " + e.getErrorCode() + ": " + e.getLocalizedMessage());
        if (this.errLogger != null) {
            this.errLogger.log(Level.WARNING, "{0}\nSQL ERROR: {1}: {2}", new Object[]{firstLine, e.getErrorCode(), e.getLocalizedMessage()});
        }
        e.printStackTrace();
    }

    /**
     * Sets up a connection, regardless if it's already established or not.
     * Note that this does <b>NOT</b> set this object's connection field!
     *
     * @return The created {@link java.sql.Connection}
     */
    public Connection makeConnection() {
        Connection c = null;
        try {
            String sqlHost = this.authDataProvider.getSqlHost();
            if (sqlHost == null) {
                System.err.println("!! SAFESQL: plug.getHost is null!");
                return null;
            }
            sqlHost = (sqlHost.contains(this.authDataProvider.getSqlDb()) ? sqlHost : ((sqlHost.endsWith("/")) ? sqlHost + this.authDataProvider.
                    getSqlDb() : sqlHost + "/" + this.authDataProvider.getSqlDb()));

            c = DriverManager.getConnection(sqlHost, this.authDataProvider.getSqlUser(), this.authDataProvider.getSqlPwd());

            if (c == null || !c.isValid(5)) {
                CommandHelper.sendMessageToOpsAndConsole("§4§l[SEVERE] Could not establish database connection.");// Everybody panic.
                CommandHelper.printAndOrLog("[XYC] Connection to " + sqlHost + " failed.", this.errLogger, Level.SEVERE);
            } else {
                DatabaseMetaData meta = c.getMetaData();
                CommandHelper.printAndOrLog("[XYC] Connected to: " + meta.getURL(), this.errLogger, Level.INFO);
            }
        } catch (SQLException e) {
            this.formatAndPrintException(e, "§cException while trying to establish connection!");
            tryClose(c);
        }
        return c;
    }

    public Connection getAnyConnection() {
        try {
            if (this.currentConnection == null || this.currentConnection.isClosed()) {
                this.currentConnection = makeConnection();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        Validate.notNull(currentConnection, "Could not make connection!");

        return getCurrentConnection();
    }

    /**
     * @see #makeConnection()
     * @deprecated Ambiguous name. Kept for compatibility with previous code.
     */
    @Deprecated
    public Connection getConnection() {
        return makeConnection();
    }

    /**
     * Safely prepares a statement. Remember to close it afterwards. Insert values by using '?'.
     *
     * Example:
     * {@code
     * PreparedStatement stmt = null;
     * try{
     * stmt = safesql.prepareStatement("UPDATE "+safesql.dbName+".users SET status = 0 AND some_string = ? WHERE user_id = ?");
     * if(stmt == null) panic();
     * stmt.setString(1,"THAT'S SAFE");//just look at what you can't do now!
     * stmt.setInt(2,42);
     * stmt.executeUpdate();
     * }finally{
     * try{ if(stmt != null){ stmt.close(); } }catch{ logAndThenPanic(); } }
     * }
     *
     * @param query Query to prepare (may contain '?')
     * @return {@link PreparedStatement}; not executed OR null at failure
     */
    @Nullable
    public PreparedStatement prepareStatement(@NonNull String query) throws SQLException {
        PreparedStatement stmt = this.getAnyConnection().prepareStatement(query);
        if (SafeSql.debug) {
            CommandHelper.sendMessageToOpsAndConsole("§ePreparing Statement: " + query);
        }
        return stmt;
    }

    /**
     * Tries to close any open {@link Connection} managed by this object.
     */
    public void preReload() {
        tryClose(this);
    }

    /**
     * SAFE
     *
     * @param query Query to execute
     * @param ints  ints to insert using {@link PreparedStatement#setInt(int, int)}
     * @return ResultSet
     * @see #executeQuery(String)
     * @deprecated This does not provide the created {@link java.sql.PreparedStatement} so there is no way to close it. That may lead to severe memory leaks. Use {@link #executeQueryWithResult(String, Object...)} instead.
     */
    @Deprecated
    public ResultSet safelyExecuteQuery(String query, int... ints) {
        try {
            PreparedStatement stmnt = this.prepareStatement(query);
            int i = 1;
            for (int nr : ints) {
                stmnt.setInt(i, nr);
                i++;
            }
            return stmnt.executeQuery();
        } catch (SQLException e) {
            this.formatAndPrintException(e, "§cException while trying to safely execute a query (int): '" + query + "'");
        }
        return null;
    }

    /**
     * SAFE
     *
     * @param query   Query to execute
     * @param strings Strings to insert using {@link PreparedStatement#setString(int, String)}
     * @return ResultSet
     * @see #executeQuery(String)
     * @see #executeQueryWithResult(String, Object...)
     * @deprecated This does not provide the created {@link java.sql.PreparedStatement} so there is no way to close it. That may lead to severe memory leaks. Use {@link #executeQueryWithResult(String, Object...)} instead.
     */
    @Deprecated
    public ResultSet safelyExecuteQuery(String query, String... strings) {
        try {
            PreparedStatement stmnt = this.prepareStatement(query);
            int i = 1;
            for (String str : strings) {
                stmnt.setString(i, str);
                i++;
            }
            return stmnt.executeQuery();
        } catch (SQLException e) {
            this.formatAndPrintException(e, "§cException while trying to safely execute a query (String): '" + query + "'");
        }
        return null;
    }

    /**
     * Executes a query in the database by creating a {@link java.sql.PreparedStatement} and filling with the the given objects.
     *
     * @param query   Query to execute (? is filled out with the corresponding {@code objects} value)
     * @param objects Objects to fill the statement with
     * @return A QueryResult representing the executed query ({@link QueryResult#getUpdateReturn()} will be {@code -1}). Remember to always close this!
     * @throws SQLException When an error occurs while creating the statement, executing the statement or filling in the values.
     */
    public QueryResult executeQueryWithResult(String query, Object... objects) throws SQLException {
        PreparedStatement stmt = this.prepareStatement(query);

        fillStatement(stmt, objects);

        return new QueryResult(stmt, stmt.executeQuery());
    }

    /**
     * Executes an update in the database by creating a {@link java.sql.PreparedStatement} and filling with the the given objects.
     *
     * @param query   Update to execute (? is filled out with the corresponding {@code objects} value)
     * @param objects Objects to fill the statement with
     * @return A QueryResult representing the executed update ({@link QueryResult#getResultSet()} will be {@code null}). Remember to always close this!
     * @throws SQLException When an error occurs while creating the statement, executing the statement or filling in the values.
     */
    public QueryResult executeUpdateWithResult(String query, Object... objects) throws SQLException {
        PreparedStatement stmt = this.prepareStatement(query);

        fillStatement(stmt, objects);

        return new QueryResult(stmt, stmt.executeUpdate());
    }

    @NonNull
    public PreparedStatement fillStatement(@Nullable PreparedStatement stmt, @NonNull Object[] objects) throws SQLException {
        if(stmt == null){
            return null;
        }

        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                stmt.setNull(i + 1, Types.OTHER);
            } else {
                stmt.setObject(i + 1, objects[i]);
            }
        }

        return stmt;
    }

    /**
     * SAFE
     *
     * @param query   Update to execute
     * @param objects Objects to insert using {@link PreparedStatement#setString(int, String)}
     * @return int (see: {@link PreparedStatement#executeUpdate()}
     * @see #executeUpdate(String)
     */
    public int safelyExecuteUpdate(String query, Object... objects) {
        try (PreparedStatement stmnt = this.prepareStatement(query)) {
            this.fillStatement(stmnt, objects);

            return stmnt.executeUpdate();
        } catch (SQLException e) {
            this.formatAndPrintException(e, "§cException while trying to safely execute an update (String)!");
        }
        return -1;
    }

    @Override
    public String toString() {
        String conStr;
        try {
            conStr = " [con:" + ((this.currentConnection.isClosed()) ? "" : "!") + "closed" + " @ " + this.currentConnection.getMetaData().getURL() + "]";
        } catch (Exception e) {
            conStr = " [con: ~" + e.getClass().getName() + "~]";
        }
        return getClass().getName() + "->" + this.authDataProvider.getSqlUser() + "@" + this.authDataProvider.getSqlHost() + "|" + this.authDataProvider.getSqlDb() + conStr;
    }

    /**
     * Convenience method. Tries to close an {@link AutoCloseable}. If it could not be closed, logs the encountered exception.
     *
     * @param closeable What to close
     * @return {@code false} if an Exception occurred while closing {@code closeable}, {@code true} otherwise.
     */
    public static boolean tryClose(AutoCloseable closeable) {
        if (closeable == null) {
            return true;
        }
        try {
            closeable.close();
        } catch (Exception exc) {
            Logger.getLogger(SafeSql.class.getName()).log(Level.WARNING, "Could not close stuff.", exc);
            return false;
        }
        return true;
    }

    @Override
    public void close() throws Exception {
        if (currentConnection != null) {
            currentConnection.close();
            currentConnection = null;
        }
    }
}
