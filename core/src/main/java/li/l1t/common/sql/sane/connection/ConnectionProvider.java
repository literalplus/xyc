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
 * Provides connections to a JDBC data source. Implementations must specify their behaviour
 * regarding closing of the connections, how many connections there are and the lifetime of
 * connections.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
public interface ConnectionProvider {
    /**
     * @return a connection to the data source, either cached or newly created
     */
    Connection getConnection() throws SqlConnectionException;
}
