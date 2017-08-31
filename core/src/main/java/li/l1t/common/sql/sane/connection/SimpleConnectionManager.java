/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
