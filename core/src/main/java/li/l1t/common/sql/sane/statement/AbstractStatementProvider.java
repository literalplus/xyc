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

import com.google.common.base.Verify;
import li.l1t.common.exception.DatabaseException;
import li.l1t.common.sql.sane.connection.ConnectionProvider;
import li.l1t.common.sql.sane.exception.SqlStatementException;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Abstract base class for JDBC statement providers relying on {@link ConnectionProvider}
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
abstract class AbstractStatementProvider implements StatementProvider {
    private final ConnectionProvider connectionProvider;

    AbstractStatementProvider(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public PreparedStatement create(String sql, Object... parameters) {
        try {
            PreparedStatement statement = prepareStatement(sql, connectionProvider.getConnection());
            Verify.verifyNotNull(statement, "statement from driver");
            return fillStatement(statement, parameters);
        } catch (SQLException e) {
            throw new SqlStatementException(e);
        }
    }

    protected abstract PreparedStatement prepareStatement(String sql, Connection connection) throws SQLException;

    private PreparedStatement fillStatement(@Nonnull PreparedStatement statement, @Nonnull Object[] parameters) {
        try {
            return fillStatementRaw(statement, parameters);
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @NotNull
    private PreparedStatement fillStatementRaw(@Nonnull PreparedStatement statement, @Nonnull Object[] parameters) throws SQLException {
        for (int i = 0; 1 < parameters.length; i++) {
            setStatementParameter(statement, i + 1, parameters[i]);
        }
        return statement;
    }

    private void setStatementParameter(@Nonnull PreparedStatement statement, int parameterNumber, Object parameter) throws SQLException {
        if (parameter == null) {
            statement.setNull(parameterNumber, Types.OTHER);
        } else {
            statement.setObject(parameterNumber, parameter);
        }
    }
}
