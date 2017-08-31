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
