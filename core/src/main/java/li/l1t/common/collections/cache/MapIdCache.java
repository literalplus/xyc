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

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

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
        Preconditions.checkNotNull(value, "value");
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

    @Override
    public boolean containsKey(K id) {
        return proxy.containsKey(id);
    }

    @Override
    public boolean containsValue(V value) {
        Optional<V> result = get(findId(value));
        return result.isPresent() && result.get().equals(value);
    }

    @Override
    public Stream<V> stream() {
        return proxy.valueStream();
    }
}
