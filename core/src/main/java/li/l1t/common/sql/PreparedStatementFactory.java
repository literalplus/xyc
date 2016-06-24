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

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Factory for {@link java.sql.PreparedStatement}s.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.3.14
 */
public interface PreparedStatementFactory {
    /**
     * Prepares a statement.
     *
     * @param query Query String to use
     * @return A PreparedStatement with the given query string.
     * @throws SQLException When an error occurs communicating to the underlying database.
     */
    PreparedStatement prepareStatement(String query) throws SQLException;

    /**
     * Fills arguments into a prepared statement.
     *
     * @param stmt    PreparedStatement to fill.
     * @param objects Objects to infer
     * @return The statement, for convenience. The parameter is also modified!
     * @throws SQLException When an error occurs communicating to the underlying database.
     */
    PreparedStatement fillStatement(PreparedStatement stmt, Object[] objects) throws SQLException;
}