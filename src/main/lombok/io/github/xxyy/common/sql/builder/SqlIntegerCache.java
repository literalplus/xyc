package io.github.xxyy.common.sql.builder;

import lombok.NonNull;

/**
 * This interface can be applied to fields which represent integer values in a database.
 * This is intended to be used with {@link ConcurrentSqlIntHolder}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.3.14
 */
public @interface SqlIntegerCache {
    /**
     * @return the name of the column represented by the field.
     */
    @NonNull
    String value(); //Inconvenient name for easier syntax
}
