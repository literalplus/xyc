package io.github.xxyy.common.sql.builder;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

/**
 * Stores a value from a SQL database.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 29.3.14
 */
@Getter
@RequiredArgsConstructor
public class SqlValueHolder<T> implements QuerySnapshot.Factory, QuerySnapshot {
    /**
     * Constructs a new SqlValueHolder.
     *
     * @param columnName   Column to target
     * @param initialValue Initial value - This is assumed to be fetched from database and set using {@link #updateValue(Object)}
     */
    public SqlValueHolder(final String columnName, final T initialValue) {
        this(columnName);
        this.updateValue(initialValue);
    }

    /**
     * Constructs a new SqlValueHolder.
     *
     * @param columnName   Column to target
     * @param initialValue Initial value - This is assumed to be fetched from database and set using {@link #updateValue(Object)}
     * @param dataSource   DataSource to use - May be null.
     */
    public SqlValueHolder(final String columnName, final T initialValue, final DataSource dataSource) {
        this(columnName, initialValue);
        this.setDataSource(dataSource);
    }

    /**
     * Name of the represented column
     */
    @NonNull
    private final String columnName;

    /**
     * DataSource where this holder might get data from if required.
     */
    @Nullable
    @Setter
    private DataSource dataSource;

    /**
     * Last value seen in the database.
     * null may mean null or that the value has not been fetched.
     *
     * @see #isFetched()
     */
    @Nullable
    protected T value;
    protected boolean modified;

    @Setter
    protected boolean fetched;

    /**
     * Gets an instance from an annotation.
     *
     * @param source Annotation to get data from
     * @return The created object
     */
    @NonNull
    public static <T> SqlValueHolder<T> fromAnnotation(@NonNull final SqlValueCache source) {
        return new SqlValueHolder<>(source.value(), null);
    }

    @Override
    @Nullable
    public QuerySnapshot produceSnapshot() {
        if (!isModified()) {
            return null;
        }

        return this;
    }

    /**
     * Sets the cached value of this storage.
     * Some subclasses might not support this.
     *
     * @param newValue New value to set
     * @throws java.lang.UnsupportedOperationException If this implementation does not support changing the value.
     * @see #updateValue(Object)
     * @see #supportsOverride()
     */
    public void setValue(@Nullable T newValue) {
        markModified();
        updateValue(newValue);
    }

    protected void markModified() {
        this.modified = true; //More efficient than storing a copy of the initial object

        if(this.dataSource != null){
            this.dataSource.registerChange(this);
        }
    }

    /**
     * Gets the value of this object, fetching it if not already present.
     * If no data source is set, it will not be fetched.
     *
     * @return value of the represented database column or {@code null} if not available.
     */
    public T getValue() {
        if (this.value == null && !isFetched() && this.getDataSource() != null) {
            getDataSource().select(this);
        }

        return this.value;
    }

    @Override
    public Object getSnapshot() {
        return getValue();
    }

    @Override
    public Type getType() {
        return Type.OBJECT_UPDATE;
    }

    /**
     * Updates the cached value of this storage.
     * This is not considered a modification (and therefore will never be written back to database).
     * This is intended to be used primarily when the remote value has changed.
     * Some subclasses might not support this.
     *
     * @param newValue New value to set
     * @throws java.lang.UnsupportedOperationException If this implementation does not support changing the value.
     * @see #setValue(Object)
     * @see #supportsOverride()
     */
    public void updateValue(@Nullable T newValue) {
        this.value = newValue;
        this.setFetched(true);
    }

    /**
     * @return whether this implementation supports {@link #setValue(Object)} and {@link #updateValue(Object)}.
     */
    public boolean supportsOverride() {
        return true;
    }

    /**
     * Represents a source which can fill data into associated SqlValueHolders.
     * It need not be able to fill others.
     */
    public interface DataSource {
        /**
         * Fetches data from this source and stores it in the given holder.
         * This might also fetch additional holders, if applicable.
         *
         * @param holder Holder to fetch
         * @return {@code true} if the holder has been populated with data.
         */
        boolean select(@NonNull SqlValueHolder<?> holder);

        /**
         * Called to notify the data source of a change in value,
         * fox example so it can schedule a write to the remote database.
         * @param holder Holder which issued the change
         */
        void registerChange(@NonNull SqlValueHolder<?> holder);
    }
}
