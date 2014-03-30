package io.github.xxyy.common.sql.builder;

import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an integer database column that can be concurrently modified.
 * It stores a modifier that can be applied to the remote value without overriding
 * it with a cached version.
 * <p/>
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

        if (getValue() != null) {
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

    @Nullable
    @Override
    public QuerySnapshot produceSnapshot() {
        if (!isModified()) {
            return null;
        }

        return new QuerySnapshot(getColumnName(), getModifier(), QuerySnapshot.Type.INTEGER_MODIFICATION);
    }

    @Override
    public void setValue(@NonNull Integer newValue) {
        if (getValue() != null) {
            this.modify(newValue - getValue());
        } else {
            this.modify(newValue);
        }
    }

    @Override
    public void updateValue(@NonNull Integer newValue) {
        this.modifier = 0;
        super.updateValue(newValue);
    }

    /**
     * Use {@link #fromAnnotation0(SqlValueCache, Class)}.
     *
     * @throws java.lang.UnsupportedOperationException
     */ //TÖDO: Better solution to this
    @NonNull
    public static <T> SqlIdentifierHolder<T> fromAnnotation(@NonNull final SqlValueCache source, @NonNull final Class<T> clazz) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets an instance from an annotation.
     *
     * @param source Annotation to get data from
     * @return The created object
     */
    @Nullable
    public static ConcurrentSqlIntHolder fromAnnotation0(@NonNull final SqlValueCache source, @NonNull final Class<?> clazz) {
        if (int.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz)) {
            return new ConcurrentSqlIntHolder(source.value());
        } else {
            throw new IllegalArgumentException("Clazz must be some kind of integer (int.class or Integer.class) - Given:" + clazz.getName());
        }
    }
}
