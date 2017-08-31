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

import com.google.common.base.Preconditions;
import li.l1t.common.collections.cache.IdCache;
import li.l1t.common.collections.cache.MapIdCache;
import li.l1t.common.misc.Identifiable;
import li.l1t.lanatus.api.exception.NoSuchPurchaseException;
import li.l1t.lanatus.api.purchase.Purchase;
import li.l1t.lanatus.api.purchase.PurchaseRepository;
import li.l1t.lanatus.sql.AbstractSqlLanatusRepository;
import li.l1t.lanatus.sql.SqlLanatusClient;

import java.util.Collection;
import java.util.UUID;

/**
 * Simple repository for purchases backed by a JDBC SQL data source. Caches id lookups.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
public class SqlPurchaseRepository extends AbstractSqlLanatusRepository implements PurchaseRepository {
    public static final String TABLE_NAME = "mt_main.lanatus_purchase";
    private final IdCache<UUID, Purchase> cache = new MapIdCache<>(Identifiable::getUniqueId);
    private final JdbcPurchaseFetcher fetcher = new JdbcPurchaseFetcher(
            new JdbcPurchaseCreator(client().products()), client().sql()
    );

    public SqlPurchaseRepository(SqlLanatusClient client) {
        super(client);
    }

    @Override
    public Purchase findById(UUID purchaseId) throws NoSuchPurchaseException {
        return cache.getOrCompute(purchaseId, fetcher::fetchById);
    }

    @Override
    public Collection<Purchase> findByPlayer(UUID playerId) {
        return fetcher.fetchByPlayer(playerId);
    }

    @Override
    public void clearCache() {
        cache.clear();
    }

    @Override
    public void clearCachesFor(UUID playerId) {
        Preconditions.checkNotNull(playerId, "playerId");
        cache.stream()
                .filter(purchase -> purchase.getPlayerId().equals(playerId))
                .forEach(cache::invalidateValue);
    }
}
