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

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;


/**
 * A cache that caches optionals, i.e. has the ability to store that a value is not present in an
 * underlying data store.
 *
 * @param <T> the value type
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
public class OptionalIdCache<T> {
    private final IdCache<Pair<UUID, Optional<T>>> cache;

    public OptionalIdCache() {
        cache = new IdCache<>(Pair::getLeft);
    }

    public void clear() {
        cache.clear();
    }

    /**
     * Caches a value that need not be present.
     *
     * @param id    the unique id key
     * @param value the value, or null to indicate that there is no value for given key
     * @return an optional with the value, or an empty optional if null was provided as value
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public Optional<T> cache(UUID id, Optional<T> value) {
        cache.cache(new Pair<>(id, value));
        return value;
    }

    /**
     * Caches that a key does not have an associated value.
     *
     * @param id the unique id key
     */
    public void cacheAbsence(UUID id) {
        cache(id, null);
    }

    /**
     * Computes the value for given key using given supplier. The supplier may return null to
     * indicate that there is no value for given key.
     *
     * @param id       the unique id key
     * @param supplier the supplied used to compute the value
     * @return an optional with the computed value, or an empty optional if the supplier indicated
     * that there is no value for given key
     */
    public Optional<T> compute(UUID id, Function<UUID, Optional<T>> supplier) {
        return cache(id, supplier.apply(id));
    }

    /**
     * Gets the optional value associated with given key.
     *
     * @param id the unique id key
     * @return an optional with the optional value, or an empty optional if there is nothing cached
     * for given key
     */
    public Optional<Optional<T>> get(UUID id) {
        Optional<Pair<UUID, Optional<T>>> pair = cache.get(id);
        if (pair.isPresent()) {
            return Optional.ofNullable(pair.get().getRight());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Attempts to get the optional value associated with given key. If nothing has been cached for
     * that key, a value will be computed using given supplier.
     *
     * @param id       the unique id key
     * @param supplier the supplier of the value
     * @return the optional value associated with given key
     */
    public Optional<T> getOrCompute(UUID id, Function<UUID, Optional<T>> supplier) {
        Optional<Optional<T>> value = get(id);
        if (value.isPresent()) {
            return value.get();
        } else {
            return compute(id, supplier);
        }
    }

    /**
     * Invalidates the value associated with given key, meaning that it will not be returned by
     * further queries unless cached again.
     *
     * @param id the key whose value to invalidate
     */
    public void invalidate(UUID id) {
        cache.invalidateKey(id);
    }
}
