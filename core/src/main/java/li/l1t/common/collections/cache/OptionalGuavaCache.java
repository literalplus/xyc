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
