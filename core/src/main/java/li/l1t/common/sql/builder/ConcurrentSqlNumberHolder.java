/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.common.sql.builder;

import li.l1t.common.sql.builder.annotation.SqlValueCache;
import li.l1t.common.util.math.MathOperator;
import li.l1t.common.util.math.NumberHelper;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>Represents an integer database column that can be concurrently modified.
 * It stores a modifier that can be applied to the remote value without overriding
 * it with a cached version.</p>
 * <p>
 * Please be careful with whom you share instances.
 * </p>
 * This class is considered thread-safe and does lock all read/write operations.
 * All locked operations use the same {@link java.util.concurrent.locks.ReentrantReadWriteLock}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 22.3.14
 * @deprecated Part of the deprecated QueryBuilder API. See {@link QueryBuilder} for details.
 */
@Deprecated
public class ConcurrentSqlNumberHolder<T extends Number> extends SqlValueHolder<T> {
    private final MathOperator<T> mathOperator;
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);
    /**
     * Modifier to be applied to the remote value.
     */
    protected T modifier;

    public ConcurrentSqlNumberHolder(@Nonnull String columnName, @Nonnull T initialValue, @Nonnull MathOperator<T> mathOperator) {
        super(columnName, initialValue); //Can't update w/o MathOperator
        this.mathOperator = mathOperator;
        this.updateValue(initialValue);
    }

    public ConcurrentSqlNumberHolder(@Nonnull String columnName, @Nonnull MathOperator<T> mathOperator) {
        this(columnName, mathOperator.getZero(), mathOperator);
    }

    @Nonnull
    public static ConcurrentSqlNumberHolder<?> fromAnnotation(@Nonnull final SqlValueCache source) {
        MathOperator<? extends Number> mathOperator = NumberHelper.getOperator(source.numberType());
        Validate.notNull(mathOperator, "Invalid Number class specified: " + source.numberType().getName());

        //noinspection ConstantConditions
        return new ConcurrentSqlNumberHolder<>(source.value().intern(), mathOperator);
    }

    @Override
    public void processResultSet(@Nonnull ResultSet resultSet) throws SQLException {
        this.updateValue(getMathOperator().getFromResultSet(getColumnName(), resultSet));
    }

    /**
     * Queues a modification of the value stored by this object.
     *
     * @param paramModifier Modifier to apply
     * @return This object, for convenient call chaining.
     */
    @Nonnull
    public SqlValueHolder modify(final T paramModifier) {
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();

        try {
            this.modifier = mathOperator.add(this.getModifierInternal(), paramModifier);

            if (getValue() != null) {
                super.setValue(mathOperator.add(this.getValue(), paramModifier));
            } else {
                super.setValue(paramModifier);
            }
        } finally {
            writeLock.unlock();
        }

        return this;
    }

    /**
     * Consumes the modifier and resets it to 0. Should be called when writing to a database.
     *
     * @return {@link #getModifier()}
     */
    protected T consumeModifier() {
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();

        try {
            T storedModifier = getModifierInternal();
            this.modifier = mathOperator.getZero();
            return storedModifier;

        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Resets the internal modifier of this object, meaning that any cached data is lost.
     *
     * @return This object, for convenient call chaining.
     */
    @Nonnull
    public SqlValueHolder reset() {
        this.updateValue(mathOperator.getZero());
        return this;
    }

    @Override
    public boolean isModified() {
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        readLock.unlock();

        try {
            return !modifier.equals(mathOperator.getZero());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public T getValue() {
        if ((this.value == null || this.value.equals(this.mathOperator.getZero())) && this.getDataSource() != null) {
            this.getDataSource().select(this);
        }

        return super.getValue();
    }

    /**
     * This sets the value of this object, indirectly.
     * It gets the difference between current and new value and sets that as modifier.
     * This is not recommended if you need a specific value in database - The remote may change and cause the modifier to set a value different from the requested one.
     * If you really need to set specific values, use {@link SqlValueHolder}.
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
        return consumeModifier(); //Aquires write lock
    }

    @Override
    public Type getType() {
        return Type.NUMBER_MODIFICATION;
    }

    /**
     * @return the difference between the last remote value and the stored value.
     */
    public T getModifier() {
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        readLock.lock();

        try {
            return getModifierInternal();
        } finally {
            readLock.unlock();
        }
    }

    protected T getModifierInternal() { //DOES NOT LOCK!! DO NOT USE IF NO READ LOCK IS PRESENT!
        return this.modifier;
    }

    @Override
    public void updateValue(@Nullable T newValue) {
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();

        try {
            updateValueInternal(newValue);
        } finally {
            writeLock.unlock();
        }
    }

    protected void updateValueInternal(T newValue) { //DOES NOT LOCK!! DO NOT USE IF NO WRITE LOCK IS PRESENT //why is this no javadoc?
        this.modifier = mathOperator.getZero();
        super.updateValue(newValue);
    }

    public MathOperator<T> getMathOperator() {
        return this.mathOperator;
    }
}
