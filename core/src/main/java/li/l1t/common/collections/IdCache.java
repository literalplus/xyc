/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.collections;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import li.l1t.common.misc.Identifiable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Caches things that are identifiable. Cache entries are expired automatically some time after they
 * have been stored.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
public class IdCache<T extends Identifiable> {
    private final Cache<UUID, T> idCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public void clear() {
        idCache.invalidateAll();
    }

    public <R extends T> R cache(R product) {
        idCache.put(product.getUniqueId(), product);
        return product;
    }

    public <R extends T> R compute(UUID id, Function<UUID, R> supplier) {
        return cache(supplier.apply(id));
    }

    public Optional<T> get(UUID id) {
        return Optional.ofNullable(idCache.getIfPresent(id));
    }

    public T getOrCompute(UUID id, Function<UUID, T> supplier) {
        Optional<T> value = get(id);
        if (value.isPresent()) {
            return value.get();
        } else {
            return compute(id, supplier);
        }
    }
}
