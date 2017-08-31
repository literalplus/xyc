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

package li.l1t.common.sql.sane;

import com.google.common.base.Preconditions;
import li.l1t.common.exception.DatabaseException;
import li.l1t.common.sql.SqlConnectable;
import li.l1t.common.sql.sane.connection.ConnectionManager;
import li.l1t.common.sql.sane.connection.SimpleConnectionManager;
import li.l1t.common.sql.sane.exception.SqlExecutionException;
import li.l1t.common.sql.sane.exception.SqlStatementException;
import li.l1t.common.sql.sane.result.QueryResult;
import li.l1t.common.sql.sane.result.SimpleQueryResult;
import li.l1t.common.sql.sane.result.SimpleUpdateResult;
import li.l1t.common.sql.sane.result.UpdateResult;
import li.l1t.common.sql.sane.scoped.JdbcScopedSession;
import li.l1t.common.sql.sane.scoped.ScopedSessionManager;
import li.l1t.common.sql.sane.statement.GeneratedKeysStatementProvider;
import li.l1t.common.sql.sane.statement.SimpleStatementProvider;
import li.l1t.common.sql.sane.statement.StatementProvider;
import li.l1t.common.util.Closer;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * Implementation of a sane SQL accessor class that manages a single database connection.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
public class SingleSql implements SaneSql {
    private final ConnectionManager connectionManager;
    private final StatementProvider simpleStatementProvider;
    private final StatementProvider generatedKeysStatementProvider;
    private final ScopedSessionManager<JdbcScopedSession> scopedSessionManager;

    public SingleSql(SqlConnectable connectable) {
        this.connectionManager = new SimpleConnectionManager(connectable);
        this.simpleStatementProvider = new SimpleStatementProvider(connectionManager);
        this.generatedKeysStatementProvider = new GeneratedKeysStatementProvider(connectionManager);
        Supplier<JdbcScopedSession> scopedSessionProvider = () -> new JdbcScopedSession(connectionManager.getConnection());
        this.scopedSessionManager = new ScopedSessionManager<>(scopedSessionProvider);
    }

    @Override
    public QueryResult query(String sqlQuery, Object... parameters) throws DatabaseException {
        try {
            return executeQuery(simpleStatement(sqlQuery, parameters));
        } catch (SQLException e) {
            throw new SqlExecutionException(e);
        }
    }

    private PreparedStatement simpleStatement(String sqlQuery, Object[] params) {
        Preconditions.checkNotNull(sqlQuery, "sqlQuery");
        Preconditions.checkNotNull(params, "params");
        return simpleStatementProvider.create(sqlQuery, params);
    }

    private QueryResult executeQuery(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        return new SimpleQueryResult(statement, resultSet);
    }

    @Override
    public int updateRaw(String sqlQuery, Object... parameters) throws DatabaseException {
        try (PreparedStatement statement = simpleStatement(sqlQuery, parameters)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new SqlExecutionException(e);
        }
    }

    @Override
    public UpdateResult update(String sqlQuery, Object... parameters) throws DatabaseException {
        try {
            return executeUpdate(genKeysStatement(sqlQuery, parameters));
        } catch (SQLException e) {
            throw new SqlStatementException(e);
        }
    }

    @NotNull
    private UpdateResult executeUpdate(PreparedStatement statement) throws SQLException {
        int affectedRowCount = statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        return new SimpleUpdateResult(statement, generatedKeys, affectedRowCount);
    }

    private PreparedStatement genKeysStatement(String sqlQuery, Object[] params) {
        Preconditions.checkNotNull(sqlQuery, "sqlQuery");
        Preconditions.checkNotNull(params, "params");
        return generatedKeysStatementProvider.create(sqlQuery, params);
    }

    @Override
    public JdbcScopedSession scoped() {
        return scopedSessionManager.scoped();
    }

    @Override
    public void close() throws Exception {
        Closer.close(connectionManager);
    }
}
