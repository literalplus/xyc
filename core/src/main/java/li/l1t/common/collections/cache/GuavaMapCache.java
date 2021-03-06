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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

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

    @Override
    public boolean containsKey(K key) {
        return get(key).isPresent();
    }

    @Override
    public Stream<Map.Entry<K, V>> entryStream() {
        return proxy.asMap().entrySet().stream();
    }

    @Override
    public Stream<V> valueStream() {
        return proxy.asMap().values().stream();
    }

    @Override
    public Stream<K> keyStream() {
        return proxy.asMap().keySet().stream();
    }
}
