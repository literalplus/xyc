package io.github.xxyy.common.sql.builder;

import lombok.NonNull;

/**
 * Stores an identifier column value.
 * Does not support overriding values for obvious reasons.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 29.3.14
 */
public class SqlIdentifierHolder<T> extends SqlValueHolder<T> {
    /**
     * Creates a new identifier holder.
     * This allows {@link #updateValue(Object)} (not {@link #setValue(Object)}!) to be used exactly <b>once</b>.
     * {@link #supportsOverride()} returns {@code true} until {@link #updateValue(Object)} is called.
     * @param columnName Column to represent
     */
    public SqlIdentifierHolder(String columnName) {
        super(columnName);
    }

    /**
     * Creates a new identifier holder.
     * @param columnName Column to represent
     * @param initialValue Initial (and final) value.
     */
    public SqlIdentifierHolder(String columnName, T initialValue){
        super(columnName, initialValue);
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void setValue(@NonNull T newValue) {
        throw new UnsupportedOperationException("Cannot change value of an identifier column!");
    }

    @NonNull @Override
    public QuerySnapshot produceSnapshot() {
        return this; //Have to ignore #isModified()
    }

    @Override
    public void updateValue(@NonNull T newValue) {
        if(supportsOverride()){
            super.updateValue(newValue);
        }

        throw new UnsupportedOperationException("Cannot change value of an identifier column!");
    }

    @Override
    public boolean supportsOverride(){
        return getSnapshot() == null;
    }

    @Override
    public Type getType(){
        return Type.OBJECT_IDENTIFIER;
    }

    /**
     * Gets an instance from an annotation.
     * @param source Annotation to get data from
     * @return The created object
     */
    @NonNull
    public static <T> SqlIdentifierHolder<T> fromAnnotation(@NonNull final SqlValueCache source){
        return new SqlIdentifierHolder<>(source.value());
    }
}
