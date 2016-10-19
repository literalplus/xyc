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

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Caches things that are identifiable and can be uniquely mapped back to an identifier. Cache
 * entries are expired automatically some time after they have been stored. Uses a {@link MapCache}
 * as backend.
 *
 * @param <K> the key type of this cache
 * @param <V> the value type of this cache
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-19
 */
public class MapIdCache<K, V> implements IdCache<K, V> {
    private final MapCache<K, V> proxy;
    private final Function<? super V, K> idFunction;

    /**
     * Creates a new id cache.
     *
     * @param idFunction          the function mapping values to their unique ids, may not return
     *                            null
     * @param writeExpiryDuration the time duration to wait after a value has been cached before it
     *                            is invalidated
     * @param writeExpiryUnit     the unit of the write expiry duration
     */
    public MapIdCache(Function<? super V, K> idFunction, long writeExpiryDuration, TimeUnit writeExpiryUnit) {
        this.proxy = new GuavaMapCache<>(writeExpiryDuration, writeExpiryUnit);
        this.idFunction = Preconditions.checkNotNull(idFunction);
    }

    /**
     * Creates a new id cache with a write expiry time of five minutes.
     *
     * @param idFunction the function mapping values to their unique ids, may not return null
     */
    public MapIdCache(Function<? super V, K> idFunction) {
        this(idFunction, 5, TimeUnit.MINUTES);
    }

    @Override
    public void clear() {
        proxy.clear();
    }

    @Override
    public <R extends V> R cache(R value) {
        proxy.cache(findId(value), value);
        return value;
    }

    private <R extends V> K findId(R value) {
        return Verify.verifyNotNull(idFunction.apply(value), "id function result is null for %s", value);
    }

    @Override
    public <R extends V> R compute(K id, Function<? super K, R> supplier) {
        return cache(supply(id, supplier));
    }

    private <R extends V> R supply(K id, Function<? super K, R> supplier) {
        return Verify.verifyNotNull(supplier.apply(id), "value supplier result is null for %s", id);
    }

    @Override
    public Optional<V> get(K id) {
        return proxy.get(id);
    }

    @Override
    public V getOrCompute(K id, Function<? super K, ? extends V> supplier) {
        Optional<V> value = get(id);
        if (value.isPresent()) {
            return value.get();
        } else {
            return compute(id, supplier);
        }
    }

    @Override
    public void invalidateValue(V value) {
        invalidateKey(findId(value));
    }

    @Override
    public void invalidateKey(K key) {
        proxy.invalidateKey(key);
    }
}
