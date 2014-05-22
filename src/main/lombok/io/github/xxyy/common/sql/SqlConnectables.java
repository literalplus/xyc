package io.github.xxyy.common.sql;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang.Validate;

/**
 * Statuic utilities for the {@link io.github.xxyy.common.sql.SqlConnectable} class.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 14.5.14
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SqlConnectables {

    /**
     * Returns the host string for the given Connectable. The host string consists of
     * the database URL, as returned by {@link SqlConnectable#getSqlHost()}, of a slash (/)
     * and the database name, as returned by {@link SqlConnectable#getSqlDb()}.
     * If the database name is already appended to the host, it will not be appended another time.
     *
     * @param connectable Connectable to get information from
     * @return Host string, as accepted by database drivers.
     */
    public static String getHostString(@NonNull SqlConnectable connectable){
        String sqlHost = connectable.getSqlHost();

        Validate.notNull(sqlHost);

        return getHostString(connectable.getSqlDb(), sqlHost);
    }

    /**
     * Returns the host string for the given paramaeters. The host string consists of
     * the database URL, as returned by {@link SqlConnectable#getSqlHost()}, of a slash (/)
     * and the database name, as returned by {@link SqlConnectable#getSqlDb()}.
     * If the database name is already appended to the host, it will not be appended another time.
     * @param database Database to use
     * @param sqlHost Fully-qualified JDBC connection string of the host to use
     * @return Host string, as accepted by database drivers.
     */
    public static String getHostString(String database, String sqlHost) {
        return sqlHost.contains(database) ? sqlHost : (sqlHost.endsWith("/") ? sqlHost + database : sqlHost + "/" + database);
    }

    /**
     * Creates a simple SqlConnectable that simply returns the connection credentials provided.
     * @param host {@link SqlConnectable#getSqlHost()}
     * @param database {@link SqlConnectable#getSqlDb()}
     * @param user {@link SqlConnectable#getSqlUser()}
     * @param password {@link SqlConnectable#getSqlPwd()}
     * @return A simple SqlConnectable matching provided arguments.
     */
    @NonNull
    public static SqlConnectable fromCredentials(@NonNull final String host, @NonNull final String database,
                                                 @NonNull final String user, @NonNull final String password) {
        return new SqlConnectable() {
            @Override
            public String getSqlDb() {
                return database;
            }

            @Override
            public String getSqlHost() {
                return host;
            }

            @Override
            public String getSqlPwd() {
                return password;
            }

            @Override
            public String getSqlUser() {
                return user;
            }
        };
    }

    /**
     * Returns the host string for the given paramaeters. The host string consists of
     * the database URL, as returned by {@link SqlConnectable#getSqlHost()}, of a slash (/)
     * and the database name, as returned by {@link SqlConnectable#getSqlDb()}.
     * If the database name is already appended to the host, it will not be appended another time.
     * @param database Database to use
     * @param hostname Fully-qualified JDBC connection string of the host to use or just a FQDN.
     * @param port port number to use.
     * @return Host string, as accepted by database drivers.
     */
    public static String getHostString(String hostname, int port, String database) {
        String sqlHost;

        if(!hostname.startsWith("jdbc:")) {
            sqlHost = "jdbc:mysql://" + hostname + ":" + port + "/";
        } else {
            sqlHost = hostname;
        }

        return getHostString(sqlHost, database);
    }
}
