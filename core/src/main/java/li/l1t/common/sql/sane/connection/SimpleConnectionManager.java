/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.sql.sane.connection;

import com.google.common.base.Verify;
import li.l1t.common.sql.SqlConnectable;
import li.l1t.common.sql.SqlConnectables;
import li.l1t.common.sql.sane.exception.SqlConnectionException;
import li.l1t.common.util.Closer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A simple implementation of a connection manager that takes a {@link SqlConnectable} and connects
 * to a JDBC data source using its credentials. Connections created by this manager are retained
 * until they are either closed or a reconnect is forced. There will only be a single open
 * connection at all times.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
public class SimpleConnectionManager implements ConnectionManager {
    private final SqlConnectable credentials;
    private Connection currentConnection;


    public SimpleConnectionManager(SqlConnectable credentials) {
        this.credentials = credentials;
    }


    @Override
    public Connection getCurrentConnection() {
        return currentConnection;
    }

    @Override
    public Connection getConnection() throws SqlConnectionException {
        if (!hasOpenConnection()) {
            currentConnection = connect();
        }
        return currentConnection;
    }

    private boolean hasOpenConnection() {
        try {
            return currentConnection != null && !currentConnection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void forceReconnect() throws SqlConnectionException {
        close();
        currentConnection = connect();
    }

    @Override
    public boolean hasActiveConnection() {
        try {
            return hasOpenConnection() && currentConnection.isValid(2);
        } catch (SQLException ignore) { //This should not happen - timeout is static > 0
            return false;
        }
    }

    @Override
    public void close() {
        Closer.close(currentConnection);
    }

    private Connection connect() throws SqlConnectionException {
        try {
            return obtainConnectionFrom(credentials);
        } catch (SQLException e) {
            throw new SqlConnectionException(e);
        }
    }

    private Connection obtainConnectionFrom(SqlConnectable credentials) throws SQLException {
        String jdbcUrl = SqlConnectables.getHostString(credentials);
        Connection connection = DriverManager.getConnection(
                jdbcUrl, credentials.getSqlUser(), credentials.getSqlPwd()
        );
        return Verify.verifyNotNull(connection, "connection returned by JDBC driver", credentials);
    }
}
