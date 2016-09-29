/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql.account;

import li.l1t.common.sql.QueryResult;
import li.l1t.common.sql.SafeSql;
import li.l1t.lanatus.api.account.LanatusAccount;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Fetches accounts from an underlying JDBC database.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-29
 */
class JdbcAccountFetcher<T extends LanatusAccount> {
    public static final String TABLE_NAME = "mt_main.lanatus_player";
    private final JdbcAccountCreator<T> creator;
    private final SafeSql sql;

    public JdbcAccountFetcher(JdbcAccountCreator<T> creator, SafeSql sql) {
        this.creator = creator;
        this.sql = sql;
    }

    public T fetchSingle(UUID playerId) throws SQLException {
        try (QueryResult qr = fetchSingleAccount(playerId)) {
            if (proceedToFirstRow(qr)) {
                return creator.createFromCurrentRow(qr.rs());
            } else {
                return creator.createDefault(playerId);
            }
        }
    }

    private boolean proceedToFirstRow(QueryResult qr) throws SQLException {
        return qr.rs().next();
    }

    private QueryResult fetchSingleAccount(UUID playerId) throws SQLException {
        return executeSql("player_uuid = ?", playerId.toString());
    }

    private QueryResult executeSql(String whereClause, Object parameters) throws SQLException {
        return sql.executeQueryWithResult(
                "SELECT player_uuid, melons, lastrank FROM " + TABLE_NAME +
                        " WHERE " + whereClause,
                parameters);
    }
}
