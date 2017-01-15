/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.common.sql.builder;

import li.l1t.common.sql.builder.annotation.SqlValueCache;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

/**
 * Stores an identifier column value.
 * Does not support overriding values for obvious reasons.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 29.3.14
 * @deprecated Part of the deprecated QueryBuilder API. See {@link QueryBuilder} for details.
 */
@Deprecated
public class SqlIdentifierHolder<T> extends SqlValueHolder<T> {
    /**
     * Creates a new identifier holder.
     * This allows {@link #updateValue(Object)} (not {@link #setValue(Object)}!) to be used exactly <b>once</b>.
     * {@link #supportsOverride()} returns {@code true} until {@link #updateValue(Object)} is called.
     *
     * @param columnName Column to represent
     */
    public SqlIdentifierHolder(String columnName) {
        super(columnName);
    }

    /**
     * Creates a new identifier holder.
     *
     * @param columnName   Column to represent
     * @param initialValue Initial (and final) value.
     */
    public SqlIdentifierHolder(String columnName, T initialValue) {
        super(columnName, initialValue);
    }

    /**
     * Gets an instance from an annotation.
     *
     * @param source Annotation to get data from
     * @param <T>    the generic type of the newly generated holder
     * @return The created object
     */
    @Nonnull
    public static <T> SqlIdentifierHolder<T> fromAnnotation(@Nonnull final SqlValueCache source) {
        return new SqlIdentifierHolder<>(source.value().intern());
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void setValue(@Nullable T newValue) {
        throw new UnsupportedOperationException("Cannot change value of an identifier column!");
    }

    @Nonnull
    @Override
    public QuerySnapshot produceSnapshot() {
        return this; //Have to ignore #isModified()
    }

    @Override
    public void updateValue(@Nullable T newValue) {
        if (supportsOverride()) {
            super.updateValue(newValue);
            return;
        }

        throw new UnsupportedOperationException("Cannot change value of an identifier column!");
    }

    @Override
    public boolean supportsOverride() {
        return this.value == null;
    }

    @Override
    public Type getType() {
        return Type.OBJECT_IDENTIFIER;
    }
}
