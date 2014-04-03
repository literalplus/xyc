package io.github.xxyy.common.sql.builder;

import lombok.Getter;
import lombok.NonNull;

/**
 * Represents an integer database column that can be concurrently modified.
 * It stores a modifier that can be applied to the remote value without overriding
 * it with a cached version.
 *
 * Please be careful with whom you share instances.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 22.3.14
 */
@Getter
public class ConcurrentSqlIntHolder extends SqlValueHolder<Integer> {
    /**
     * Modifier to be applied to the remote value.
     */
    protected int modifier = 0;

    public ConcurrentSqlIntHolder(String columnName) {
        super(columnName);
    }

    public ConcurrentSqlIntHolder(String columnName, Integer initialValue) {
        super(columnName, initialValue);
    }

    /**
     * Queues a modification of the value stored by this object.
     *
     * @param paramModifier Modifier to apply
     * @return This object, for convenient call chaining.
     */
    @NonNull
    public SqlValueHolder modify(final int paramModifier) {
        this.modifier += paramModifier;

        if (getSnapshot() != null) {
            super.setValue(getValue() + paramModifier);
        } else {
            super.setValue(paramModifier);
        }

        return this;
    }

    /**
     * Consumes the modifier and resets it to 0. Should be called when writing to a database.
     *
     * @return {@link #getModifier()}
     */
    protected int consumeModifier() {
        int storedModifier = getModifier();
        this.reset();
        return storedModifier;
    }

    /**
     * Resets the internal modifier of this object, meaning that any cached data is lost.
     *
     * @return This object, for convenient call chaining.
     */
    @NonNull
    public SqlValueHolder reset() {
        this.updateValue(0);
        this.modifier = 0;
        return this;
    }

    @Override
    public boolean isModified() {
        return modifier != 0;
    }

    @Override
    public void setValue(@NonNull Integer newValue) {
        if (getSnapshot() != null) {
            this.modify(newValue - getValue());
        } else {
            this.modify(newValue);
        }
    }

    @Override
    public Object getSnapshot() {
        return consumeModifier();
    }

    @Override
    public Type getType() {
        return Type.NUMBER_MODIFICATION;
    }

    @Override
    public void updateValue(@NonNull Integer newValue) {
        this.modifier = 0;
        super.updateValue(newValue);
    }

    /**
     * Gets an concurrent sql integer holder from an annotation.
     * @param source Annotation to get the column name from
     * @return An instance corresponding to the given column name.
     */
    @NonNull
    public static ConcurrentSqlIntHolder fromAnnotation(@NonNull final SqlValueCache source) {
        return new ConcurrentSqlIntHolder(source.value());
    }
}
