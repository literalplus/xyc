/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.lanatus.sql.account;

import li.l1t.common.exception.DatabaseException;
import li.l1t.common.exception.InternalException;
import li.l1t.common.sql.sane.SaneSql;
import li.l1t.common.sql.sane.result.QueryResult;
import li.l1t.lanatus.api.account.LanatusAccount;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

/**
 * Fetches accounts from an underlying JDBC database.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-29
 */
class JdbcAccountFetcher<T extends LanatusAccount> extends li.l1t.common.sql.sane.util.AbstractJdbcFetcher<T> {
    private final JdbcAccountCreator<? extends T> creator;

    public JdbcAccountFetcher(JdbcAccountCreator<? extends T> creator, SaneSql sql) {
        super(creator, sql);
        this.creator = creator;
    }

    public T fetchOrDefault(UUID playerId) throws InternalException {
        return fetchOptionally(playerId)
                .orElseGet(() -> creator.createDefault(playerId));
    }

    public Optional<T> fetchOptionally(UUID playerId) throws InternalException {
        try (QueryResult qr = fetchSingleAccount(playerId)) {
            if (proceedToFirstRow(qr)) {
                return Optional.of(creator.createFromCurrentRow(qr.rs()));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw InternalException.wrap(e);
        }
    }

    private boolean proceedToFirstRow(QueryResult qr) throws SQLException {
        return qr.rs().next();
    }

    private QueryResult fetchSingleAccount(UUID playerId) throws DatabaseException {
        return executeSql("player_uuid = ?", playerId.toString());
    }

    private QueryResult executeSql(String whereClause, Object... parameters) throws DatabaseException {
        return sql().query(
                buildSelect(whereClause),
                parameters
        );
    }

    @Override
    protected String buildSelect(String whereClause) {
        return "SELECT player_uuid, melons, lastrank " +
                "FROM " + SqlAccountRepository.TABLE_NAME + " " +
                "WHERE " + whereClause;
    }
}
