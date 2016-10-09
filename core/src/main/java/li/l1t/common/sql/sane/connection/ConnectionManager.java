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

import li.l1t.common.sql.sane.exception.SqlConnectionException;

import java.sql.Connection;

/**
 * Manages a single connection to a JDBC data source. Connections may be reused, so any connection
 * returned by a manager may not be closed by client directly. However, all connections associated
 * with this manager may be closed by calling {@link ConnectionManager#close()}. Connections are
 * scoped to the manager. If a reconnect is forced, all previously created connections are
 * invalidated.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
public interface ConnectionManager extends AutoCloseable, ConnectionProvider {
    /**
     * @return the connection most recently created by this manager, or null if no connection has
     * been created yet
     */
    Connection getCurrentConnection();

    /**
     * Forces the manager to close its current connection and establish a new one.
     */
    void forceReconnect() throws SqlConnectionException;

    /**
     * <p><b>Note:</b> This method may send a statement to the data source in order to validate the
     * connection, depending on the driver.</p>
     *
     * @return whether there is currently a connection and that connection is considered valid by
     * the database driver
     */
    boolean hasActiveConnection();
}
