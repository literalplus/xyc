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
