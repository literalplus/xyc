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
import java.util.function.Function;

/**
 * Caches mappings from one thing to another thing. Implementations may implement ways to
 * automatically expire mappings based on certain conditions documented in their class JavaDoc.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-19
 */
public interface MapCache<K, V> {
    /**
     * Invalidates all entries of this cache.
     */
    void clear();

    /**
     * Caches a mapping in this cache. Note that mappings expire after the write expiry time set in
     * the constructor.
     *
     * @param key   the key of the mapping to cache
     * @param value the value of the mapping
     * @param <R>   the implementation return type, for fluent returns
     * @return the value
     */
    <R extends V> R cache(K key, R value);

    /**
     * Computes a value by applying given supplier to given key.
     *
     * @param key      the key to compute the mapping for
     * @param supplier the supplier of the value
     * @param <R>      the implementation return type, for fluent returns
     * @return the computed value
     */
    <R extends V> R compute(K key, Function<? super K, R> supplier);

    /**
     * Gets the optional mapping for given key.
     *
     * @param key the key to retrieve the mapping for
     * @return an optional containing the value of the mapping if it exists, or an empty optional if
     * it doesn't
     */
    Optional<V> get(K key);

    /**
     * Gets the currently cached value for given key, if present. Otherwise, computes the mapping
     * for given key using given supplier function, caches and returns it.
     *
     * @param key      the key to get or compute the mapping for
     * @param supplier the value supplier
     * @return the found or computed value of the mapping for given key in the cache
     */
    V getOrCompute(K key, Function<K, ? extends V> supplier);

    /**
     * Invalidates the mapping for given key, if any.
     *
     * @param key the key to operate on
     */
    void invalidateKey(K key);
}
