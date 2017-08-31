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

import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

/**
 * Simple implementation of {@link QuerySnapshot}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.3.14
 * @deprecated Part of the deprecated QueryBuilder API. See {@link QueryBuilder} for details.
 */
@Deprecated
public class SimpleQuerySnapshot implements QuerySnapshot {
    /**
     * Name of the column this snapshot belongs to.
     */
    @Nonnull
    private final String columnName;
    /**
     * Value to be written or added to the column.
     *
     * @see #getType()
     */
    @Nullable
    private final Object snapshot;
    /**
     * Type of this snapshot.
     */
    @Nonnull
    private final Type type;

    public SimpleQuerySnapshot(@Nonnull String columnName, @Nullable Object snapshot, @Nonnull Type type) {
        this.columnName = columnName;
        this.snapshot = snapshot;
        this.type = type;
    }

    @Nonnull
    public String getColumnName() {
        return this.columnName;
    }

    @Nullable
    public Object getSnapshot() {
        return this.snapshot;
    }

    @Nonnull
    public Type getType() {
        return this.type;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SimpleQuerySnapshot)) return false;
        final SimpleQuerySnapshot other = (SimpleQuerySnapshot) o;
        if (!other.canEqual(this)) return false;
        final Object this$columnName = this.columnName;
        final Object other$columnName = other.columnName;
        if (!this$columnName.equals(other$columnName))
            return false;
        final Object this$snapshot = this.snapshot;
        final Object other$snapshot = other.snapshot;
        if (this$snapshot == null ? other$snapshot != null : !this$snapshot.equals(other$snapshot)) return false;
        final Object this$type = this.type;
        final Object other$type = other.type;
        return this$type.equals(other$type);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $columnName = this.columnName;
        result = result * PRIME + ($columnName.hashCode());
        final Object $snapshot = this.snapshot;
        result = result * PRIME + ($snapshot == null ? 0 : $snapshot.hashCode());
        final Object $type = this.type;
        result = result * PRIME + ($type.hashCode());
        return result;
    }

    public boolean canEqual(Object other) {
        return other instanceof SimpleQuerySnapshot;
    }

    public String toString() {
        return "io.github.xxyy.common.sql.builder.SimpleQuerySnapshot(columnName=" + this.columnName + ", snapshot=" + this.snapshot + ", type=" + this.type + ")";
    }
}
