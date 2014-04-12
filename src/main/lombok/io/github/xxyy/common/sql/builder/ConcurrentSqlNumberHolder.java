package io.github.xxyy.common.sql.builder;

import io.github.xxyy.common.sql.builder.annotation.SqlNumberCache;
import io.github.xxyy.common.sql.builder.annotation.SqlValueCache;
import io.github.xxyy.common.util.math.MathOperator;
import io.github.xxyy.common.util.math.NumberHelper;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents an integer database column that can be concurrently modified.
 * It stores a modifier that can be applied to the remote value without overriding
 * it with a cached version.
 *
 * Please be careful with whom you share instances.
 *
 * This class is considered thread-safe and does lock all read/write operations.
 * All locked operations use the same {@link java.util.concurrent.locks.ReentrantReadWriteLock}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 22.3.14
 */
public class ConcurrentSqlNumberHolder<T extends Number> extends SqlValueHolder<T> {
    /**
     * Modifier to be applied to the remote value.
     */
    protected T modifier;

    @Getter
    private final MathOperator<T> mathOperator;

//    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);

    @Override
    public void processResultSet(@NonNull ResultSet resultSet) throws SQLException {
        this.updateValue(getMathOperator().getFromResultSet(getColumnName(), resultSet));
    }

    public ConcurrentSqlNumberHolder(@NonNull String columnName, @NonNull T initialValue, @NonNull MathOperator<T> mathOperator) {
        super(columnName, initialValue); //Can't update w/o MathOperator
        this.mathOperator = mathOperator;
        this.updateValue(initialValue);
    }

    public ConcurrentSqlNumberHolder(@NonNull String columnName, @NonNull MathOperator<T> mathOperator) {
        this(columnName, mathOperator.getZero(), mathOperator);
    }

    /**
     * Queues a modification of the value stored by this object.
     *
     * @param paramModifier Modifier to apply
     * @return This object, for convenient call chaining.
     */
    @NonNull
    public SqlValueHolder modify(final T paramModifier) {
//        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
//        writeLock.lock();

//        try {
        this.modifier = mathOperator.add(this.getModifierInternal(), paramModifier);

        if (getValue() != null) {
            super.setValue(mathOperator.add(this.getValue(), paramModifier));
        } else {
            super.setValue(paramModifier);
        }
//        } finally {
//            writeLock.unlock();
//        }

        return this;
    }

    /**
     * Consumes the modifier and resets it to 0. Should be called when writing to a database.
     *
     * @return {@link #getModifier()}
     */
    protected T consumeModifier() {
//        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
//        writeLock.lock();

//        try {
            T storedModifier = getModifierInternal();
            this.modifier = mathOperator.getZero();
            return storedModifier;

//        } finally {
//            writeLock.unlock();
//        }
    }

    /**
     * Resets the internal modifier of this object, meaning that any cached data is lost.
     *
     * @return This object, for convenient call chaining.
     */
    @NonNull
    public SqlValueHolder reset() {
        this.updateValue(mathOperator.getZero());
        return this;
    }

    @Override
    public boolean isModified() {
//        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
//        readLock.unlock();
//
//        try {
            return !modifier.equals(mathOperator.getZero());
//        } finally {
//            readLock.unlock();
//        }
    }

    /**
     * This sets the value of this object, indirectly.
     * It gets the difference between current and new value and sets that as modifier.
     * This is not recommended if you need a specific value in database - The remote may change and cause the modifier to set a value different from the requested one.
     * If you really need to set specific values, use {@link io.github.xxyy.common.sql.builder.SqlValueHolder}.
     *
     * @param newValue New value to set
     */
    @Override
    public void setValue(@Nullable T newValue) {
        if (newValue == null) {
            this.setValue(mathOperator.getZero());
        }

        if (this.value != null) {
            this.modify(mathOperator.subtract(newValue, getValue()));
        } else {
            this.modify(newValue);
        }
    }

    @Override
    public Object getSnapshot() {
//        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
//        readLock.lock();
//
//        try {
            return consumeModifier();
//        } finally {
//            readLock.unlock();
//        }
    }

    @Override
    public Type getType() {
        return Type.NUMBER_MODIFICATION;
    }

    /**
     * @return the difference between the last remote value and the stored value.
     */
    public T getModifier() {
//        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
//        readLock.lock();
//
//        try {
            return getModifierInternal();
//        } finally {
//            readLock.unlock();
//        }
    }

    protected T getModifierInternal() { //DOES NOT LOCK!! DO NOT USE IF NO READ LOCK IS PRESENT!
        return this.modifier;
    }

    @Override
    public void updateValue(@Nullable T newValue) {
//        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
//        writeLock.lock();
//
//        try {
            updateValueInternal(newValue);
//        } finally {
//            writeLock.unlock();
//        }
    }

    protected void updateValueInternal(T newValue) { //DOES NOT LOCK!! DO NOT USE IF NO WRITE LOCK IS PRESENT
        this.modifier = mathOperator.getZero();
        super.updateValue(newValue);
    }

    /**
     * Gets an concurrent sql integer holder from an annotation.
     *
     * @param source Annotation to get the column name from
     * @return An instance corresponding to the given column name.
     */
    @NonNull
    @SuppressWarnings("unchecked") //pls generic annotations
    public static ConcurrentSqlNumberHolder<?> fromAnnotation(@NonNull final SqlNumberCache source) {
        MathOperator<?> mathOperator = NumberHelper.getOperator(source.numberType());

        Validate.notNull(mathOperator, "Invalid Number class specified: " + source.numberType().getName());

        return new ConcurrentSqlNumberHolder(source.value(), mathOperator);
    }

    @NonNull
    public static ConcurrentSqlNumberHolder fromAnnotation(@NonNull final SqlValueCache source) {
        throw new UnsupportedOperationException("Pls, this is the wrong annotation type for this class.");
    }
}
