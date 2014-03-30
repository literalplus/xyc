package io.github.xxyy.common.sql.builder;

import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This interface can be applied to fields which represent object values in a database.
 * This is intended to be used with {@link SqlValueHolder}.
 *
 *
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.3.14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqlValueCache {
    /**
     * @return the name of the column represented by the field.
     */
    @NonNull
    String value(); //Inconvenient name for easier syntax

    /**
     * @return The type of this cache.
     */
    @NonNull
    SqlValueCache.Type type() default Type.OBJECT_UPDATE;

    public enum Type {
        /**
         * Updates objects with overriding values.
         * @see io.github.xxyy.common.sql.builder.SqlValueHolder
         */
        OBJECT_UPDATE,
        /**
         * Stores modification of an integer and writes the modification to the remote,
         * allowing for concurrent modification.
         * @see io.github.xxyy.common.sql.builder.ConcurrentSqlIntHolder
         */
        INTEGER_MODIFICATION,
        /**
         * Is an unique identifier for a column.
         * @see io.github.xxyy.common.sql.builder.SqlIdentifierHolder
         */
        OBJECT_IDENTIFIER
    }
}
