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

package li.l1t.common.mongo;

import com.mongodb.DBObject;

/**
 * Helps dealing with MongoDB DBObjects by providing some commonly used methods.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 19/02/15
 * @deprecated There is currently no known use case for this, may be removed without further notice.
 */
@Deprecated
public final class DBObjectHelper {
    private DBObjectHelper() {

    }

    /**
     * Attempts to retrieve an integer from given DBObject or returns a default value otherwise.
     *
     * @param object the container to retrieve from
     * @param key    the string identifier of the value to seek
     * @param def    the default value to return if the value is not present or not an integer
     * @return the retrieved integer value or the default value
     */
    public static int getInt(DBObject object, String key, int def) {
        if (!object.containsField(key)) {
            return def;
        }

        Object value = object.get(key);

        return value instanceof Integer ? (int) value : def;
    }

    /**
     * Attempts to retrieve an object from given DBObject or returns a default value otherwise.
     *
     * @param object the container to retrieve from
     * @param key    the string identifier of the value to seek
     * @param def    the default value to return if the value is not present or not of the correct type
     * @param clazz  the expected type of the value
     * @return the retrieved object value or the default value
     */
    @SuppressWarnings("unchecked")
    public static <T> T getOrDefault(DBObject object, String key, T def, Class<T> clazz) {
        if (!object.containsField(key)) {
            return def;
        }

        Object value = object.get(key);

        return clazz.isAssignableFrom(value.getClass()) ? (T) value : def;
    }
}
