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
