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

import li.l1t.common.sql.SqlConnectable;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connects to JDBC data sources and provides established connections
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
public interface SqlConnector {
    /**
     * Obtains a new connection to the JDBC data source specified by given connectable.
     *
     * @param connectable the connectable with the data source credentials
     * @return a new connection
     */
    Connection obtainConnection(SqlConnectable connectable) throws SQLException;
}
