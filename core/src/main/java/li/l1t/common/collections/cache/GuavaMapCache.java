/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.collections.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Caches mappings from one thing to another thing, wrapping Guava's {@link Cache} class with an
 * easier to use, Map-like API and support for the compute operation. When any invalid value is
 * accessed, all invalid values are removed from the cache.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-19
 */
public class GuavaMapCache<K, V> implements MapCache<K, V> {
    private final Cache<K, V> proxy;

    /**
     * Creates a new map cache.
     *
     * @param writeExpiryDuration the time to wait after a value was cached before it is
     *                            invalidated
     * @param writeExpiryUnit     the unit of the write expiry duration
     */
    public GuavaMapCache(long writeExpiryDuration, TimeUnit writeExpiryUnit) {
        this.proxy = CacheBuilder.newBuilder()
                .expireAfterWrite(writeExpiryDuration, writeExpiryUnit)
                .build();
    }

    /**
     * Creates a new map cache that invalidates entries five minutes after caching.
     */
    public GuavaMapCache() {
        this(5, TimeUnit.MINUTES);
    }

    @Override
    public void clear() {
        proxy.invalidateAll();
    }

    @Override
    public <R extends V> R cache(K key, R value) {
        proxy.put(key, value);
        return value;
    }

    @Override
    public <R extends V> R compute(K key, Function<? super K, R> supplier) {
        return cache(key, supplier.apply(key));
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(proxy.getIfPresent(key));
    }

    @Override
    public V getOrCompute(K key, Function<K, ? extends V> supplier) {
        Optional<V> value = get(key);
        if (value.isPresent()) {
            return value.get();
        } else {
            return compute(key, supplier);
        }
    }

    @Override
    public void invalidateKey(K key) {
        proxy.invalidate(key);
    }
}
