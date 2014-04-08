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
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 22.3.14
 */
@Getter
public class ConcurrentSqlNumberHolder<T extends Number> extends SqlValueHolder<T> {
    /**
     * Modifier to be applied to the remote value.
     */
    protected T modifier;

    private final MathOperator<T> mathOperator;

    @Override
    public void processResultSet(@NonNull ResultSet resultSet) throws SQLException {
        this.updateValue(getMathOperator().getFromResultSet(getColumnName(), resultSet));
    }

    public ConcurrentSqlNumberHolder(@NonNull String columnName, @NonNull T initialValue, @NonNull MathOperator<T> mathOperator) {
        super(columnName, initialValue); //Can't update w/o MathOperator
        this.mathOperator = mathOperator;
        this.updateValue(initialValue); //
    }

    public ConcurrentSqlNumberHolder(@NonNull String columnName, @NonNull MathOperator<T> mathOperator){
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
        this.modifier = mathOperator.add(this.getModifier(), paramModifier);

        if (getValue() != null) {
            super.setValue(mathOperator.add(this.getValue(), paramModifier));
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
    protected T consumeModifier() {
        T storedModifier = getModifier();
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
        this.updateValue(mathOperator.getZero());
        return this;
    }

    @Override
    public boolean isModified() {
        return !modifier.equals(mathOperator.getZero());
    }

    /**
     * This sets the value of this object, indirectly.
     * It gets the difference between current and new value and sets that as modifier.
     * This is not recommended if you need a specific value in database - The remote may change and cause the modifier to set a value different from the requested one.
     * If you really need to set specific values, use {@link io.github.xxyy.common.sql.builder.SqlValueHolder}.
     * @param newValue New value to set
     */
    @Override
    public void setValue(@NonNull T newValue) {
        if (this.value != null) {
            this.modify(mathOperator.subtract(newValue, getValue()));
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
    public void updateValue(@Nullable T newValue) {
        this.modifier = mathOperator.getZero();
        super.updateValue(newValue);
    }

    /**
     * Gets an concurrent sql integer holder from an annotation.
     * @param source Annotation to get the column name from
     * @return An instance corresponding to the given column name.
     */
    @NonNull @SuppressWarnings("unchecked") //pls generic annotations
    public static ConcurrentSqlNumberHolder<?> fromAnnotation(@NonNull final SqlNumberCache source) {
        MathOperator<?> mathOperator = NumberHelper.getOperator(source.numberType());

        Validate.notNull(mathOperator, "Invalid Number class specified: "+source.numberType().getName());

        return new ConcurrentSqlNumberHolder(source.value(), mathOperator);
    }

    @NonNull
    public static ConcurrentSqlNumberHolder fromAnnotation(@NonNull final SqlValueCache source) {
        throw new UnsupportedOperationException("Pls, this is the wrong annotation type for this class.");
    }
}
