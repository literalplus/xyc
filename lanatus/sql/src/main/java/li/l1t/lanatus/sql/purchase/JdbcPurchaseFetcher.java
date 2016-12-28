/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
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
