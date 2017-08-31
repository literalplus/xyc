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
