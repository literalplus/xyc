/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.sql;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

/**
 * Provides some static utility methods for {@link SqlConnectable}s.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2014-05-14
 */
public final class SqlConnectables {

    private SqlConnectables() {
    }

    /**
     * Builds a host string from a connectable. <p>A host string consists of the {@link
     * SqlConnectable#getSqlHost() database URL}, a slash (/), and the {@link
     * SqlConnectable#getSqlDb() database name}.</p> If the database URL ends with the database
     * name, it is not appended another time.
     *
     * @param connectable the connectable to get information from
     * @return a host string derived from given connectable
     */
    public static String getHostString(@Nonnull SqlConnectable connectable) {
        String jdbcUrl = Preconditions.checkNotNull(connectable.getSqlHost(), "connectable.sqlHost");
        return getHostString(connectable.getSqlDb(), jdbcUrl);
    }

    /**
     * Builds a host string from database name and database URL. <p>A host string consists of the
     * {@link SqlConnectable#getSqlHost() database URL}, a slash (/), and the {@link
     * SqlConnectable#getSqlDb() database name}.</p> If the database URL ends with the database
     * name, it is not appended another time.
     *
     * @param database the database the host string should point to
     * @param jdbcUrl  the fully qualified JDBC URL the host string should point to
     * @return a host string derived from given parameters
     */
    public static String getHostString(String database, String jdbcUrl) {
        String hostString = appendDatabaseIfNotAlreadyPresent(database, jdbcUrl);

        if (!hostString.startsWith("jdbc:")) {
            if (!hostString.startsWith("mysql://")) {
                hostString = "jdbc:mysql://" + hostString;
            } else {
                hostString = "jdbc:" + hostString;
            }
        }

        if (!hostString.contains("?")) {
            hostString += "?autoReconnect=true";
        }

        return hostString;
    }

    private static String appendDatabaseIfNotAlreadyPresent(String database, String jdbcUrl) {
        if (jdbcUrl.contains(database)) { //some databases allow parameters with ?, so can't use endsWith
            return jdbcUrl;
        } else if (jdbcUrl.endsWith("/")) {
            return jdbcUrl + database;
        } else {
            return jdbcUrl + "/" + database;
        }
    }

    /**
     * @param host     {@link SqlConnectable#getSqlHost() the fully-qualified JDBC URL}
     * @param database {@link SqlConnectable#getSqlDb() the database}
     * @param user     {@link SqlConnectable#getSqlUser() the user name}
     * @param password {@link SqlConnectable#getSqlPwd() the password}
     * @return a connectable instance with given credentials
     */
    @Nonnull
    public static SqlConnectable fromCredentials(@Nonnull final String host, @Nonnull final String database,
                                                 @Nonnull final String user, @Nonnull final String password) {
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
}
