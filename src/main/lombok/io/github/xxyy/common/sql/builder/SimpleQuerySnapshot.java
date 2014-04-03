package io.github.xxyy.common.sql.builder;

import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * Simple implementation of {@link io.github.xxyy.common.sql.builder.QuerySnapshot}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.3.14
 */
@Data
public class SimpleQuerySnapshot implements QuerySnapshot {
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
    private final Object snapshot;
    /**
     * Type of this snapshot.
     */
    @NonNull
    private final Type type;

}
