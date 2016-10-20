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
 * Caches things that are identifiable and can be uniquely mapped back to an identifier. Cache
 * entries are expired automatically some time after they have been stored.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-20
 */
public interface IdCache<K, V> {
    /**
     * Invalidates all mappings in this cache.
     */
    void clear();

    /**
     * Caches a value in this cache, obtaining its identifier from the id function. Note that
     * mappings expire after the write expiry time set in the constructor.
     *
     * @param value the value of the mapping to cache
     * @param <R>   the implementation type of the value, for fluent returns
     * @return the value
     */
    <R extends V> R cache(R value);

    /**
     * Computes a value by applying given supplier to given key, using the id function to find its
     * identifier.
     *
     * @param id       the identifier parameter for the supplier function
     * @param supplier the supplier function supplying the value for the identifier
     * @param <R>      the implementation type of the supplied value, for fluent returns
     * @return the computed value
     */
    <R extends V> R compute(K id, Function<? super K, R> supplier);

    /**
     * Gets the optional mapping for given key.
     *
     * @param id the key to retrieve the mapping for
     * @return an optional containing the value of the mapping if it exists, or an empty optional if
     * it doesn't
     */
    Optional<V> get(K id);

    /**
     * Gets the currently cached value for given key, if present. Otherwise, computes the mapping
     * for given key using given supplier function, caches and returns it. Note that supplied values
     * are always mapped to the identifier returned by the id function, not to the id parameter.
     *
     * @param id       the identifier to get or compute the mapping for
     * @param supplier the value supplier
     * @return the found or computed value of the mapping for given key in the cache
     */
    V getOrCompute(K id, Function<? super K, ? extends V> supplier);

    /**
     * Invalidates the mapping for given value, if present, using the id function to obtain the
     * key.
     *
     * @param value the value whose mapping to invalidate
     */
    void invalidateValue(V value);

    /**
     * Invalidates the mapping for given key, if any.
     *
     * @param key the key to operate on
     */
    void invalidateKey(K key);

    /**
     * @param id the identifier to look up in the cache
     * @return whether given identifier is currently part of a valid mapping
     */
    boolean containsKey(K id);

    /**
     * @param value the value to pass to the id function
     * @return whether a mapping is currently cached for the identifier that the id function
     * returned for given key that is {@link Object#equals(Object) equal} to given value
     */
    boolean containsValue(V value);
}
