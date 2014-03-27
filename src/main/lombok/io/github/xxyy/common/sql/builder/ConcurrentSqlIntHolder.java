package io.github.xxyy.common.sql.builder;

import lombok.*;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an integer database column that can be concurrently modified.
 * It stores a modifier that can be applied to the remote value without overriding
 * it with a cached version.
 *
 * Please be careful with whom you share instances, as {@link #setLastValue(int)}
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 22.3.14
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ConcurrentSqlIntHolder implements QuerySnapshot.Factory {
    /**
     * Name of the represented column
     */
    @NonNull
    private final String columnName;
    /**
     * Modifier to be applied to the remote value.
     */
    private int modifier;
    /**
     * Last value seen in the database.
     * Represents {@link java.lang.Integer#MIN_VALUE} if no value has been encountered or fetched.
     */
    @Setter
    private int lastValue = Integer.MIN_VALUE;

    /**
     * Queues a modification of the value stored by this object.
     * @param paramModifier Modifier to apply
     * @return This object, for convenient call chaining.
     */
    @NonNull
    public ConcurrentSqlIntHolder modify(final int paramModifier){
        this.modifier += paramModifier;
        return this;
    }

    /**
     * Consumes the modifier and resets it to 0. Should be called when writing to a database.
     * @return {@link #getModifier()}
     */
    protected int consume(){
        int storedModifier = getModifier();
        this.reset();
        return storedModifier;
    }

    /**
     * Resets the internal modifier of this object, meaning that any cached data is lost.
     * @return This object, for convenient call chaining.
     */
    @NonNull
    public ConcurrentSqlIntHolder reset(){
        this.modifier = 0;
        return this;
    }

    /**
     * @return whether any changes have been applied to the stored value.
     */
    public boolean isModified(){
        return modifier != 0;
    }

    @NonNull
    public static ConcurrentSqlIntHolder fromAnnotation(@NonNull final SqlIntegerCache source){
        return new ConcurrentSqlIntHolder(source.value());
    }

    @Override @Nullable
    public QuerySnapshot produceSnapshot() {
        if(!isModified()){
            return null;
        }

        return new QuerySnapshot(columnName, consume(), QuerySnapshot.Type.INTEGER_MODIFICATION);
    }
}
