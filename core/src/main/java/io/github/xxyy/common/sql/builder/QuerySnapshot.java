/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.sql.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a snapshot of a value intended to be written to a SQL database.
 * Note that this is just a snapshot and might change immediately after,
 * so it is required to be written to a database as soon as possible.
 * If it is of type {@link QuerySnapshot.Type#NUMBER_MODIFICATION},
 * the source might have reset its write-cache and therefore the remote will be in an invalid state if the snapshot is not written.
 * Values might change over time depending on implementation.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2.4.14
 */
public interface QuerySnapshot {
    /**
     * @return the name of the column this snapshot belongs to.
     */
    String getColumnName();

    /**
     * @return the value to be written to the column associated with this snapshot.
     */
    Object getSnapshot();

    /**
     * @return the kind of value to be written.
     */
    Type getType();

    /**
     * Represents a type of query.
     */
    public enum Type {
        /**
         * Represents an addition to an integer.
         */
        NUMBER_MODIFICATION("%1$s=%1$s+?"),
        /**
         * Represents an update of any column, meaning the previous value is lost.
         */
        OBJECT_UPDATE("%s=?"),
        /**
         * Represents an object used to identify a column where other data is fetched from or written to.
         */
        OBJECT_IDENTIFIER("%s=?"),
        /**
         * Represents an object used to identify a column where other data is fetched from or written to.
         * This is used to select columns where the value is distinct from {@link #getSnapshot()}.
         */
        NEGATED_OBJECT_IDENTIFIER("%s!=?");

        /**
         * This returns an operator string.
         * <b>Call {@link String#format(String, Object...)} with arg1 being the column name on this</b>
         */
        @NotNull
        private final String operator;

        public String getOperator(String columnName) {
            return String.format(getOperator(), columnName);
        }

        Type(@NotNull String op) {
            this.operator = op;
        }

        @NotNull
        public String getOperator() {
            return this.operator;
        }
    }

    /**
     * Represents something that produces QuerySnapshots.
     */
    public interface Factory {
        /**
         * This produces a query snapshot that is to be written to a SQL database.
         * Note that this snapshot is required to be written to a database as soon as possible. For additional information, see the JavaDoc of {@link io.github.xxyy.common.sql.builder.SimpleQuerySnapshot}.
         * This may return {@code null} if no changes are to be written.
         *
         * @return A snapshot of the current state of the thing this factory belongs to.
         */
        @Nullable
        QuerySnapshot produceSnapshot();
    }
}
