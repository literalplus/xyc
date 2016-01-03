/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.sql;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.github.xxyy.common.util.TextOutputHelper;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collection;
import java.util.function.Function;
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

    public static Logger getLogger() {
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
            return stmnt.executeQuery();
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
        try (PreparedStatement stmnt = this.getAnyConnection().prepareStatement(query)) {
            stmnt.executeUpdate();
            return true;
        } catch (SQLException e) {
            this.formatAndPrintException(e, "§cException while trying to execute update: '" + query + "'");
        }
        return false;
    }

    /**
     * formats an exception, prints a line before it, then prints (to Ops &amp; console) &amp; logs it.
     *
     * @param e         Exception to use
     * @param firstLine A line describing the error, normally class &amp; method name - more efficient than getting the caller
     */
    public void formatAndPrintException(SQLException e, String firstLine) {
        System.out.println(firstLine);
        System.out.println("§4SQL Error " + e.getErrorCode() + ": " + e.getLocalizedMessage());
        if (this.errLogger != null){
            System.out.printf("%s\nSQL ERROR: %s: %s", firstLine, e.getErrorCode(), e.getLocalizedMessage());
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
            String sqlHost = SqlConnectables.getHostString(this.authDataProvider);

            c = DriverManager.getConnection(sqlHost, this.authDataProvider.getSqlUser(), this.authDataProvider.getSqlPwd());

            if (c == null || !c.isValid(5)){
//                CommandHelper.sendMessageToOpsAndConsole("§4§l[SEVERE] Could not establish database connection.");// Everybody panic.
                TextOutputHelper.printAndOrLog("[XYC] Connection to " + sqlHost + " failed.", this.errLogger, Level.SEVERE);
            } else {
                DatabaseMetaData meta = c.getMetaData();
                TextOutputHelper.printAndOrLog("[XYC] Connected to: " + meta.getURL(), this.errLogger, Level.INFO);
            }
        } catch (SQLException e) {
            this.formatAndPrintException(e, "§cException while trying to establish connection!");
            tryClose(c);
        }
        return c;
    }

    public Connection getAnyConnection() {
        try {
            if (this.currentConnection == null || this.currentConnection.isClosed()){
                this.currentConnection = makeConnection();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        Validate.notNull(currentConnection, "Could not make connection!");

        return getCurrentConnection();
    }

    /**
     * @return a newly made connection.
     * @see #makeConnection()
     * @deprecated Ambiguous name. Kept for compatibility with previous code.
     */
    @Deprecated
    public Connection getConnection() {
        return makeConnection();
    }

    /**
     * Safely prepares a statement. Remember to close it afterwards. Insert values by using '?'.
     * <p>
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
     * }</p>
     *
     * @param query Query to prepare (may contain '?')
     * @return {@link PreparedStatement}; not executed OR null at failure
     */
    @NotNull
    public PreparedStatement prepareStatement(@NotNull String query) throws SQLException {
        PreparedStatement stmt = this.getAnyConnection().prepareStatement(query);
        Validate.notNull(stmt);
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
            Validate.notNull(stmnt);
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
            Validate.notNull(stmnt);
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
     * @deprecated Uses QueryResult instead of UpdateResult. Use {@link #executeUpdateWithGenKeys(String, Object...)}.
     */
    @Deprecated
    public QueryResult executeUpdateWithResult(String query, Object... objects) throws SQLException {
        PreparedStatement stmt = this.prepareStatement(query);

        fillStatement(stmt, objects);

        return new QueryResult(stmt, stmt.executeUpdate());
    }

    /**
     * Executes an update in the database by creating a {@link java.sql.PreparedStatement} and filling with the the given objects.
     *
     * @param query     Update to execute (? is filled out with the corresponding {@code objects} value)
     * @param arguments Objects to fill the statement with
     * @return A QueryResult representing the executed update ({@link QueryResult#getResultSet()} will be {@code null}). Remember to always close this!
     * @throws SQLException When an error occurs while creating the statement, executing the statement or filling in the values.
     */
    @NotNull
    public UpdateResult executeUpdateWithGenKeys(@NotNull String query, Object... arguments) throws SQLException {
        PreparedStatement stmt = this.getAnyConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        Validate.notNull(stmt);

        fillStatement(stmt, arguments);

        return new UpdateResult(stmt.executeUpdate(), stmt.getGeneratedKeys());
    }

    @Nullable
    public PreparedStatement fillStatement(@Nullable PreparedStatement stmt, @NotNull Object[] objects) throws SQLException {
        if (stmt == null){
            return null;
        }

        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null){
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

    /**
     * Executes a set of updates for a given object type in a batch. Note that this can only operate on same objects and
     * same SQL statements.
     *
     * @param sql             the SQL update or insert statement to fill with the parameters for each batch element
     * @param data            a collection of objects representing the data to be written to the database
     * @param parameterMapper a mapper function mapping an object to the {@code sql} parameters representing it, in
     *                        declaration order.
     * @param <T>             the type of object to be written to the database
     * @return an integer array, see {@link PreparedStatement#executeBatch()}
     * @throws SQLException if an error occurs while executing the batch
     */
    public <T> int[] executeBatchUpdate(String sql, Collection<T> data, Function<T, Object[]> parameterMapper) throws SQLException {
        Preconditions.checkNotNull(data, "data");
        Preconditions.checkNotNull(sql, "sql");
        Preconditions.checkNotNull(parameterMapper, "parameterMapper");

        Connection connection = getAnyConnection();
        int[] count;
        try (PreparedStatement statement = Preconditions.checkNotNull(connection.prepareStatement(sql), "statement")) {
            connection.setAutoCommit(false);

            for (T t : data) {
                Object[] parameters = parameterMapper.apply(t);
                fillStatement(statement, parameters);
                statement.addBatch();
            }

            count = statement.executeBatch();
        } finally {
            connection.commit();
            connection.setAutoCommit(true);
        }

        return count;
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
        if (closeable == null){
            return true;
        }
        try {
            closeable.close();
        } catch (Exception exc) {
            Logger.getLogger(SafeSql.class.getName()).log(Level.WARNING, "Could not close something: " + closeable, exc);
            return false;
        }
        return true;
    }

    @Override
    public void close() throws Exception {
        if (currentConnection != null){
            currentConnection.close();
            currentConnection = null;
        }
    }

    public Connection getCurrentConnection() {
        return this.currentConnection;
    }

    public void setCurrentConnection(Connection currentConnection) {
        this.currentConnection = currentConnection;
    }
}
