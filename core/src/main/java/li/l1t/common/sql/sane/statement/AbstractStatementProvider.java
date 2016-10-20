/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
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
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;

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
        for (int i = 0; i < parameters.length; i++) {
            setStatementParameter(statement, i + 1, parameters[i]);
        }
        return statement;
    }

    private void setStatementParameter(@Nonnull PreparedStatement statement, int parameterNumber, Object parameter) throws SQLException {
        if (parameter == null) {
            statement.setNull(parameterNumber, Types.OTHER);
        } else if (parameter instanceof Instant) {
            long epochMilli = ((Instant) parameter).toEpochMilli();
            statement.setTimestamp(parameterNumber, new Timestamp(epochMilli));
        } else {
            statement.setObject(parameterNumber, parameter);
        }
    }
}
