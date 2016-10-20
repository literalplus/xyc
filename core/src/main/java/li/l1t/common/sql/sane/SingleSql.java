/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
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
