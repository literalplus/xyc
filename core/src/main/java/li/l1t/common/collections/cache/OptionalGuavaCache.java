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

import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * A map cache that caches optionals, i.e. has the ability to cache the fact that a value is not
 * present in an underlying data store.
 *
 * @param <V> the value type
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
public class OptionalGuavaCache<K, V> extends GuavaMapCache<K, Optional<V>> implements OptionalCache<K, V> {
    /**
     * Creates a new optional id cache.
     *
     * @param writeExpiryDuration the time to wait after a value was cached before it is
     *                            invalidated
     * @param writeExpiryUnit     the unit of the write expiry duration
     */
    public OptionalGuavaCache(long writeExpiryDuration, TimeUnit writeExpiryUnit) {
        super(writeExpiryDuration, writeExpiryUnit);
    }

    /**
     * Creates a new optional id cache with a write expiry time of five minutes.
     */
    public OptionalGuavaCache() {
        super();
    }

    @Override
    public Optional<V> cacheValue(K id, V value) {
        Optional<V> optional = Optional.ofNullable(value);
        cache(id, optional);
        return optional;
    }

    @Override
    public void cacheAbsence(K id) {
        cache(id, Optional.empty());
    }

    @Override
    public Optional<V> getOptionally(K key) {
        return get(key).orElseGet(Optional::empty);
    }
}
