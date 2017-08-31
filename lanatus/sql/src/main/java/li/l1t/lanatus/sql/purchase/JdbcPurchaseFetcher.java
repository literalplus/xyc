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

package li.l1t.lanatus.sql.purchase;

import li.l1t.common.exception.DatabaseException;
import li.l1t.common.sql.sane.SaneSql;
import li.l1t.common.sql.sane.result.QueryResult;
import li.l1t.lanatus.api.exception.NoSuchPurchaseException;
import li.l1t.lanatus.api.purchase.Purchase;

import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

/**
 * Fetches purchases from a JDBC SQL data store as purchase objects.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class JdbcPurchaseFetcher extends li.l1t.common.sql.sane.util.AbstractJdbcFetcher<Purchase> {
    JdbcPurchaseFetcher(JdbcPurchaseCreator creator, SaneSql sql) {
        super(creator, sql);
    }

    public Purchase fetchById(UUID purchaseId) throws DatabaseException {
        try (QueryResult result = selectSingle(purchaseId)) {
            if (proceedToNextRow(result)) {
                return entityFromCurrentRow(result);
            } else {
                throw new NoSuchPurchaseException("purchase with id " + purchaseId);
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    private QueryResult selectSingle(UUID purchaseId) {
        return select("id=?", purchaseId.toString());
    }

    @Override
    protected String buildSelect(String whereClause) {
        return "SELECT id, player_uuid, product_id, created, data, comment, melonscost " +
                "FROM " + SqlPurchaseRepository.TABLE_NAME + " WHERE " + whereClause;
    }

    public Collection<Purchase> fetchByPlayer(UUID playerId) throws DatabaseException {
        try (QueryResult result = selectByPlayer(playerId)) {
            return collectAll(result);
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    private QueryResult selectByPlayer(UUID playerId) {
        return select("player_uuid=?", playerId.toString());
    }
}
