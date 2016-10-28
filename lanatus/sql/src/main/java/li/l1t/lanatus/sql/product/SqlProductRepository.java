/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.lanatus.sql.product;

import com.google.common.base.Preconditions;
import li.l1t.common.collections.cache.IdCache;
import li.l1t.common.collections.cache.MapIdCache;
import li.l1t.common.misc.Identifiable;
import li.l1t.lanatus.api.exception.NoSuchProductException;
import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.api.product.ProductQueryBuilder;
import li.l1t.lanatus.api.product.ProductRegistrationBuilder;
import li.l1t.lanatus.api.product.ProductRepository;
import li.l1t.lanatus.sql.AbstractSqlLanatusRepository;
import li.l1t.lanatus.sql.SqlLanatusClient;

import java.util.Collection;
import java.util.UUID;

/**
 * Simple repository for products from a JDBC SQL database. Provides a cache by id.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
public class SqlProductRepository extends AbstractSqlLanatusRepository implements ProductRepository {
    public static final String TABLE_NAME = "mt_main.lanatus_product";
    private final IdCache<UUID, Product> cache = new MapIdCache<>(Identifiable::getUniqueId);
    private final JdbcProductFetcher fetcher = new JdbcProductFetcher(
            new JdbcProductCreator(), client().sql()
    );
    private final JdbcProductWriter writer = new JdbcProductWriter(client().sql());

    public SqlProductRepository(SqlLanatusClient client) {
        super(client);
    }

    @Override
    public Product findById(UUID productId) throws NoSuchProductException {
        return cache.getOrCompute(productId, fetcher::fetchById);
    }

    @Override
    public ProductQueryBuilder query() {
        return new SqlProductQueryBuilder(this);
    }

    Collection<Product> execute(ProductQuery query) {
        return fetcher.fetchByQuery(query);
    }

    @Override
    public ProductRegistrationBuilder registration(UUID productId) {
        return new SqlProductRegistrationBuilder(this, productId);
    }

    void createNewProduct(SqlProduct product) {
        Preconditions.checkNotNull(product, "product");
        cache.invalidateKey(product.getUniqueId());
        writer.write(product);
    }

    @Override
    public void clearCache() {
        cache.clear();
    }

    @Override
    public void clearCachesFor(UUID playerId) {
        //no-op
    }
}
