/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql.product;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import li.l1t.lanatus.api.product.Product;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Caches products by name and id.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class SqlProductCache {
    public static final String TABLE_NAME = "mt_main.lanatus_product";
    private final Cache<UUID, Product> idCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public void clear() {
        idCache.invalidateAll();
    }

    public <T extends Product> T cache(T product) {
        idCache.put(product.getUniqueId(), product);
        return product;
    }

    public Optional<Product> get(UUID productId) {
        return Optional.ofNullable(idCache.getIfPresent(productId));
    }
}
