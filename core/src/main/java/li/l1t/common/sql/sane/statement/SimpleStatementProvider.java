/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.sql.sane.statement;

import li.l1t.common.sql.sane.connection.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Provides ordinary prepared statements, getting connections from a {@link ConnectionProvider}.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
public class SimpleStatementProvider extends AbstractStatementProvider {
    public SimpleStatementProvider(ConnectionProvider provider) {
        super(provider);
    }

    @Override
    protected PreparedStatement prepareStatement(String sql, Connection connection) throws SQLException {
        return connection.prepareStatement(sql);
    }
}
