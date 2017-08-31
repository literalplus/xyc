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

package li.l1t.common.sql.sane.statement;

import com.google.common.base.Verify;
import li.l1t.common.exception.DatabaseException;
import li.l1t.common.sql.sane.connection.ConnectionProvider;
import li.l1t.common.sql.sane.exception.SqlStatementException;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.sql.*;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.UUID;

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
        } else if (parameter instanceof Temporal) {
            long epochMilli = ((Instant) parameter).toEpochMilli();
            statement.setTimestamp(parameterNumber, new Timestamp(epochMilli));
        } else if (parameter instanceof UUID) {
            statement.setString(parameterNumber, parameter.toString());
        } else {
            statement.setObject(parameterNumber, parameter);
        }
    }
}
