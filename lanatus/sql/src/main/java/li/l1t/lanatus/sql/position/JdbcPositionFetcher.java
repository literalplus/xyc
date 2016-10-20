/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql.position;

import li.l1t.common.exception.DatabaseException;
import li.l1t.common.sql.sane.SaneSql;
import li.l1t.common.sql.sane.result.QueryResult;
import li.l1t.lanatus.api.position.Position;
import li.l1t.lanatus.sql.common.AbstractJdbcFetcher;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Fetches position objects from an underlying JDBC SQL data source.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class JdbcPositionFetcher extends AbstractJdbcFetcher<Position> {

    JdbcPositionFetcher(JdbcPositionCreator creator, SaneSql sql) {
        super(creator, sql);
    }

    public Optional<Position> fetchByPurchase(UUID positionId) {
        try (QueryResult result = selectById(positionId)) {
            if (proceedToNextRow(result)) {
                return Optional.of(creator.createFromCurrentRow(result.rs()));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    private QueryResult selectById(UUID purchaseId) {
        return select("purchase_id=?", purchaseId.toString());
    }

    public Collection<Position> fetchAllByPlayer(UUID playerId) {
        try (QueryResult result = selectByPlayer(playerId)) {
            return collectAll(result);
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    private QueryResult selectByPlayer(UUID playerId) {
        return select("player_uuid=?", playerId.toString());
    }

    @Override
    protected String buildSelect(String whereClause) {
        return "SELECT purchase_id, player_uuid, product_id, data " +
                "FROM " + SqlPositionRepository.TABLE_NAME + " " +
                "WHERE " + whereClause;
    }
}
