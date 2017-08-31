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

package li.l1t.lanatus.sql.position;

import com.google.common.base.VerifyException;
import li.l1t.common.collections.cache.GuavaMapCache;
import li.l1t.common.collections.cache.MapCache;
import li.l1t.common.collections.cache.OptionalCache;
import li.l1t.common.collections.cache.OptionalGuavaCache;
import li.l1t.lanatus.api.position.Position;
import li.l1t.lanatus.api.position.PositionRepository;
import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.api.purchase.Purchase;
import li.l1t.lanatus.sql.AbstractSqlLanatusRepository;
import li.l1t.lanatus.sql.SqlLanatusClient;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final OptionalCache<UUID, Position> purchasePositionCache = new OptionalGuavaCache<>();
    private final JdbcPositionFetcher fetcher = new JdbcPositionFetcher(
            new JdbcPositionCreator(client().products()), client().sql()
    );
    private final JdbcPositionWriter writer = new JdbcPositionWriter(client().sql());
    private final MapCache<UUID, Set<UUID>> playerPositionsCache = new GuavaMapCache<>();

    public SqlPositionRepository(SqlLanatusClient client) {
        super(client);
    }

    @Override
    public Optional<Position> findByPurchase(UUID purchaseId) {
        return purchasePositionCache.getOrCompute(purchaseId, fetcher::fetchByPurchase);
    }

    @Override
    public Collection<Position> findAllByPlayer(UUID playerId) {
        Collection<Position> positions = fetcher.fetchAllByPlayer(playerId);
        Set<UUID> positionIds = positions.stream()
                .map(Position::getProduct)
                .map(Product::getUniqueId)
                .collect(Collectors.toSet());
        playerPositionsCache.cache(playerId, positionIds);
        return positions;
    }

    @Override
    public boolean playerHasProduct(UUID playerId, UUID productId) {
        if (!playerPositionsCache.containsKey(playerId)) {
            findAllByPlayer(playerId); //adds to cache
        }
        Set<UUID> playerProducts = playerPositionsCache.get(playerId)
                .orElseThrow(VerifyException::new);
        return playerProducts.contains(productId);
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
        playerPositionsCache.invalidateKey(purchase.getPlayerId());
        return position;
    }

    @Override
    public void clearCache() {
        purchasePositionCache.clear();
    }

    @Override
    public void clearCachesFor(UUID playerId) {
        playerPositionsCache.get(playerId)
                .ifPresent(set -> set.forEach(purchasePositionCache::invalidateKey));
        //we don't need to scan the player caches for references to ^ because positions only belong
        //to a single player, the player we're clearing v
        playerPositionsCache.invalidateKey(playerId);
    }
}
