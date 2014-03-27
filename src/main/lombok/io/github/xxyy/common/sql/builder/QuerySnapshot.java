package io.github.xxyy.common.sql.builder;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a snapshot of a value intended to be written to a SQL database.
 * Note that this is just a snapshot and might change immediately after,
 * so it is required to be written to a database as soon as possible.
 * If it is of type {@link io.github.xxyy.common.sql.builder.QuerySnapshot.Type#INTEGER_MODIFICATION},
 * the source might have reset its write-cache and therefore the remote will be in an invalid state if the snapshot is not written.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.3.14
 */
@Data
public class QuerySnapshot {
    /**
     * Name of the column this snapshot belongs to.
     */
    @NonNull
    private final String columnName;
    /**
     * Value to be written or added to the column.
     * @see #getType()
     */
    @Nullable
    private final Object objectValue;
    /**
     * Type of this snapshot.
     */
    @NonNull
    private final Type type;

    /**
     * Represents a type of query.
     */
    public enum Type {
        /**
         * Represents an addition to an integer.
         */
        INTEGER_MODIFICATION("%1$s=%1$s+?"),
        /**
         * Represents an update of any column, meaning the previous value is lost.
         */
        OBJECT_UPDATE("%s=?");

        /**
         * This returns an operator string.
         * <b>Call {@link String#format(String, Object...)} with arg1 being the column name on this</b>
         */
        @Getter @NonNull
        private final String operator;

        public String getOperator(String columnName){
            return String.format(getOperator(), columnName);
        }

        Type(@NonNull String op){
            this.operator = op;
        }
    }

    /**
     * Represents something that produces QuerySnapshots.
     */
    public interface Factory {
        /**
         * This produces a query snapshot that is to be written to a SQL database.
         * Note that this snapshot is required to be written to a database as soon as possible. For additional information, see the JavaDoc of {@link io.github.xxyy.common.sql.builder.QuerySnapshot}.
         * This may return {@code null} if no changes are to be written.
         *
         * @return A snapshot of the current state of the thing this factory belongs to.
         */
        @Nullable
        QuerySnapshot produceSnapshot();
    }
}
