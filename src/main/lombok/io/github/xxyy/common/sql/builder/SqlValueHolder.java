package io.github.xxyy.common.sql.builder;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

/**
 * Stores a value from a SQL database.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 29.3.14
 */
@Getter
@RequiredArgsConstructor
public class SqlValueHolder<T> implements QuerySnapshot.Factory {
    public SqlValueHolder(final String columnName, final T initialValue){
        this(columnName);
        this.setValue(initialValue);
    }

    /**
     * Name of the represented column
     */
    @NonNull
    private final String columnName;

    /**
     * Last value seen in the database.
     * Represents {@link Integer#MIN_VALUE} if no value has been encountered or fetched.
     */
    @Nullable
    protected T value;
    protected boolean modified;

    /**
     * Gets an instance from an annotation.
     * @param source Annotation to get data from
     * @return The created object
     */
    @NonNull
    public static <T> SqlValueHolder<T> fromAnnotation(@NonNull final SqlValueCache source, @NonNull final Class<T> clazz){
        return new SqlValueHolder<>(source.value());
    }

    @Override @Nullable
    public QuerySnapshot produceSnapshot() {
        if(!isModified()){
            return null;
        }

        return new QuerySnapshot(columnName, this.getValue(), QuerySnapshot.Type.OBJECT_UPDATE);
    }

    /**
     * Sets the cached value of this storage.
     * Some subclasses might not support this.
     * @param newValue New value to set
     * @see #updateValue(Object)
     * @see #supportsOverride()
     * @throws java.lang.UnsupportedOperationException If this implementation does not support changing the value.
     */
    public void setValue(@NonNull T newValue){
        this.modified = true; //More efficient than storing a copy of the initial object
        updateValue(newValue);
    }

    /**
     * Updates the cached value of this storage.
     * This is not considered a modification (and therefore will never be written back to database).
     * This is intended to be used primarily when the remote value has changed.
     * Some subclasses might not support this.
     * @param newValue New value to set
     * @see #setValue(Object)
     * @see #supportsOverride()
     * @throws java.lang.UnsupportedOperationException If this implementation does not support changing the value.
     */
    public void updateValue(@NonNull T newValue){
        this.value = newValue;
    }

    /**
     * @return whether this implementation supports {@link #setValue(Object)} and {@link #updateValue(Object)}.
     */
    public boolean supportsOverride(){
        return true;
    }
}
