/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
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
