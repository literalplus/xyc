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

import li.l1t.common.collections.cache.OptionalGuavaCache;
import li.l1t.common.collections.cache.OptionalCache;
import li.l1t.lanatus.api.position.Position;
import li.l1t.lanatus.api.position.PositionRepository;
import li.l1t.lanatus.api.purchase.Purchase;
import li.l1t.lanatus.sql.AbstractSqlLanatusRepository;
import li.l1t.lanatus.sql.SqlLanatusClient;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Simple implementation of a position repository based on a JDBC SQL data source. Caches positions
 * by purchase id and product ids by player. Caches are automatically invalidated some time after
 * they have been updated from the data source.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
public class SqlPositionRepository extends AbstractSqlLanatusRepository implements PositionRepository {
    public static final String TABLE_NAME = "mt_main.lanatus_position";
    private final OptionalCache<UUID, Position> cache = new OptionalGuavaCache<>();
    private final JdbcPositionFetcher fetcher = new JdbcPositionFetcher(
            new JdbcPositionCreator(client().products()), client().sql()
    );
    private final JdbcPositionWriter writer = new JdbcPositionWriter(client().sql());

    public SqlPositionRepository(SqlLanatusClient client) {
        super(client);
    }

    @Override
    public Optional<Position> findByPurchase(UUID purchaseId) {
        return cache.getOrCompute(purchaseId, fetcher::fetchByPurchase);
    }

    @Override
    public Collection<Position> findAllByPlayer(UUID playerId) {
        return fetcher.fetchAllByPlayer(playerId);
    }

    @Override
    public boolean playerHasProduct(UUID playerId, UUID productId) {
        return false;
    }

    /**
     * Creates a <b>new</b> position from a purchase and writes it to the database.
     *
     * @param purchase the purchase to create a position from
     * @return the created position
     */
    public Position createFromPurchase(Purchase purchase) {
        SqlPosition position = new SqlPosition(purchase);
        writer.write(position);
        return position;
    }

    @Override
    public void clearCache() {
        cache.clear();
    }
}
