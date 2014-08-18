/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.sql.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Simple implementation of {@link io.github.xxyy.common.sql.builder.QuerySnapshot}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.3.14
 */
public class SimpleQuerySnapshot implements QuerySnapshot {
    /**
     * Name of the column this snapshot belongs to.
     */
    @NotNull
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
    @NotNull
    private final Type type;

    public SimpleQuerySnapshot(@NotNull String columnName, @Nullable Object snapshot, @NotNull Type type) {
        this.columnName = columnName;
        this.snapshot = snapshot;
        this.type = type;
    }

    @NotNull
    public String getColumnName() {
        return this.columnName;
    }

    @Nullable
    public Object getSnapshot() {
        return this.snapshot;
    }

    @NotNull
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
        if (!this$type.equals(other$type)) return false;
        return true;
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
