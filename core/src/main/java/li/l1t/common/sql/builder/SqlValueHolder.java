/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package li.l1t.common.sql.builder;

import li.l1t.common.sql.builder.annotation.SqlValueCache;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Stores a value from a SQL database.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 29.3.14
 * @deprecated Part of the deprecated QueryBuilder API. See {@link QueryBuilder} for details.
 */
@Deprecated
public class SqlValueHolder<T> implements QuerySnapshot.Factory, QuerySnapshot {
    /**
     * Name of the represented column
     */
    @Nonnull
    private final String columnName;
    /**
     * Last value seen in the database.
     * null may mean null or that the value has not been fetched.
     *
     * @see #isFetched()
     */
    @Nullable
    protected T value;
    protected boolean modified;
    protected boolean fetched;
    /**
     * DataSource where this holder might get data from if required.
     */
    @Nullable
    private DataSource dataSource;

    /**
     * Constructs a new SqlValueHolder.
     *
     * @param columnName   Column to target
     * @param initialValue Initial value - This is assumed to be fetched from database and set using {@link #updateValue(Object)}
     */
    public SqlValueHolder(@Nonnull final String columnName, @Nullable final T initialValue) {
        this(columnName);
        this.value = initialValue;
    }

    /**
     * Constructs a new SqlValueHolder.
     *
     * @param columnName   Column to target
     * @param initialValue Initial value - This is assumed to be fetched from database and set using {@link #updateValue(Object)}
     * @param dataSource   DataSource to use - May be null.
     */
    public SqlValueHolder(@Nonnull final String columnName, @Nullable final T initialValue, @Nonnull final DataSource dataSource) {
        this(columnName, initialValue);
        this.setDataSource(dataSource);
    }

    @java.beans.ConstructorProperties({"columnName"})
    public SqlValueHolder(@Nonnull String columnName) {
        this.columnName = columnName;
    }

    /**
     * Gets an instance from an annotation.
     *
     * @param source Annotation to get data from
     * @param <T>    generic type to be held by the resulting holder
     * @return The created object
     */
    @Nonnull
    public static <T> SqlValueHolder<T> fromAnnotation(@Nonnull final SqlValueCache source) {
        return new SqlValueHolder<>(source.value().intern(), null);
    }

    @Override
    @Nullable
    public QuerySnapshot produceSnapshot() {
        if (!isModified()) {
            return null;
        }

        return this;
    }

    protected void markModified() {
        this.modified = true; //More efficient than storing a copy of the initial object

        if (this.dataSource != null) {
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
        fetchIfNecessary();
        return this.value;
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
        this.value = newValue;
    }

    /**
     * Fetches this holder's value, if not already present, from the underlying data source.
     *
     * @throws java.lang.IllegalStateException if the value could not be fetched
     */
    public void fetchIfNecessary() {
        if (this.value == null && !isFetched() && this.getDataSource() != null) {
            getDataSource().select(this);
        }

        if (!isFetched()) {
            throw new IllegalStateException("Could not fetch " + this.toString() + "!");
        }
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
     * Processes data from a ResultSet and reads values corresponding to this holder into cache.
     * This should only be called if it is certain that the ResultSet contains this holder's column.
     *
     * @param resultSet ResultSet to process
     * @throws SQLException If a database error occurs
     */
    @SuppressWarnings("unchecked")
    public void processResultSet(@Nonnull ResultSet resultSet) throws SQLException {
        this.updateValue((T) resultSet.getObject(this.getColumnName()));
    }

    /**
     * @return whether this implementation supports {@link #setValue(Object)} and {@link #updateValue(Object)}.
     */
    public boolean supportsOverride() {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "columnName='" + columnName + '\'' +
                ", value=" + value +
                ", modified=" + modified +
                ", fetched=" + fetched +
                '}';
    }

    @Nonnull
    public String getColumnName() {
        return this.columnName;
    }

    @Nullable
    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(@Nullable DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isModified() {
        return this.modified;
    }

    public boolean isFetched() {
        return this.fetched;
    }

    public void setFetched(boolean fetched) {
        this.fetched = fetched;
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
        boolean select(@Nonnull SqlValueHolder<?> holder);

        /**
         * Called to notify the data source of a change in value,
         * fox example so it can schedule a write to the remote database.
         *
         * @param holder Holder which issued the change
         */
        void registerChange(@Nonnull SqlValueHolder<?> holder);
    }
}
