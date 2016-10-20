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

/**
 * A map cache that caches optionals, i.e. has the ability to cache the fact that a value is not
 * present in an underlying data store.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-20
 */
public interface OptionalCache<K, V> extends MapCache<K, Optional<V>> {
    /**
     * Caches a value that need not be present.
     *
     * @param id    the key
     * @param value an optional containing the value for given key, or an empty optional to indicate
     *              absence of the value
     * @param <O>   the optional type - note that optional is final and this is only kept for API
     *              compatibility
     * @return the provided optional
     * @see #cacheValue(Object, Object) for when the value is not already stored in an optional
     */
    @Override
    <O extends Optional<V>> O cache(K id, O value);

    /**
     * Caches a value that need not be present.
     *
     * @param id    the key
     * @param value the value, or null to indicate that there is no value for given key
     * @return an optional with the value, or an empty optional if null was provided as value
     * @see #cache(Object, Optional) for when the value is already stored in an optional
     */
    Optional<V> cacheValue(K id, V value);

    /**
     * Caches that a key does not have an associated value.
     *
     * @param id the key
     */
    void cacheAbsence(K id);

    /**
     * Gets the actual value associated with given key, if any. Note that this method does not allow
     * to differentiate between whether the value is not cached or not present. Use {@link
     * #get(Object)} for that.
     *
     * @param key the key to find the value for
     * @return an optional if a present value has been cached for given key, or an empty optional if
     * absence of a value has been cached or no mapping has been cached
     */
    Optional<V> getOptionally(K key);
}
