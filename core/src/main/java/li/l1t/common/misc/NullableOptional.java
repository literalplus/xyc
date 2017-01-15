/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.misc;

import java.util.NoSuchElementException;

/**
 * An {@link java.util.Optional} which can contain NULL values.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 19.8.14
 * @deprecated This is not at all how Optional is supposed to be used. Use nested optionals instead.
 */
@Deprecated
public class NullableOptional<T> {
    private final T value;
    private final boolean present;

    private NullableOptional(T value, boolean present) {
        this.value = value;
        this.present = present;
    }

    /**
     * Creates a new NullableOptional.
     *
     * @param value the value the NullableOptional will contain
     * @param <T>   the type of the value
     * @return a NullableOptional containing specified value
     */
    public static <T> NullableOptional<T> of(T value) {
        return new NullableOptional<>(value, true);
    }

    /**
     * Creates a new, empty NullableOptional.
     *
     * @param <T> the type of the value the NullableOptional does not have
     * @return an empty NullableOptional
     */
    public static <T> NullableOptional<T> of() {
        return new NullableOptional<>(null, false);
    }

    /**
     * Gets the value, if present.
     *
     * @return the value
     * @throws java.util.NoSuchElementException if no value is present
     */
    public T get() {
        if (!isPresent()) {
            throw new NoSuchElementException("No value present");
        }

        return value;
    }

    /**
     * Gets the value if present or returns a provided default otherwise.
     *
     * @param def the default to return if no value is present
     * @return the value or the default if no value is present
     */
    public T orElse(T def) {
        if (isPresent()) {
            return value;
        } else {
            return def;
        }
    }

    /**
     * Gets the value if present or NULL otherwise.
     *
     * @return the value or NULL if no value is present
     */
    public T orNull() {
        return orElse(null);
    }

    /**
     * @return whethher a value is present in the NullableOptional
     */
    public boolean isPresent() {
        return present;
    }
}
