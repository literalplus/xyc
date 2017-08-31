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
 * Represents a snapshot of a value intended to be written to a SQL database.
 * Note that this is just a snapshot and might change immediately after,
 * so it is required to be written to a database as soon as possible.
 * If it is of type {@link QuerySnapshot.Type#NUMBER_MODIFICATION},
 * the source might have reset its write-cache and therefore the remote will be in an invalid state if the snapshot is not written.
 * Values might change over time depending on implementation.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2.4.14
 * @deprecated Part of the deprecated QueryBuilder API. See {@link QueryBuilder} for details.
 */
@Deprecated
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
    enum Type {
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
        @Nonnull
        private final String operator;

        Type(@Nonnull String op) {
            this.operator = op;
        }

        public String getOperator(String columnName) {
            return String.format(getOperator(), columnName);
        }

        @Nonnull
        public String getOperator() {
            return this.operator;
        }
    }

    /**
     * Represents something that produces QuerySnapshots.
     */
    interface Factory {
        /**
         * This produces a query snapshot that is to be written to a SQL database.
         * Note that this snapshot is required to be written to a database as soon as possible. For additional information, see the JavaDoc of {@link SimpleQuerySnapshot}.
         * This may return {@code null} if no changes are to be written.
         *
         * @return A snapshot of the current state of the thing this factory belongs to.
         */
        @Nullable
        QuerySnapshot produceSnapshot();
    }
}
