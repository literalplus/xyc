package io.github.xxyy.common.sql.builder;

import lombok.NonNull;
import org.apache.commons.lang.Validate;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * This interface can be applied to fields which represent object values in a database.
 * This is intended to be used with {@link SqlValueHolder}.
 *
 *
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.3.14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqlValueCache {
    /**
     * @return the name of the column represented by the field.
     */
    @NonNull
    String value(); //Inconvenient name for easier syntax

    /**
     * @return The type of this cache.
     */
    @NonNull
    SqlValueCache.Type type() default Type.OBJECT_UPDATE;

    /**
     * @return whether annotation processors shall skip this field.
     */
    boolean skip() default false;

    public enum Type {
        /**
         * Updates objects with overriding values.
         * @see io.github.xxyy.common.sql.builder.SqlValueHolder
         */
        OBJECT_UPDATE {
            @NonNull @Override
            public SqlValueHolder<?> createHolder(@NonNull Set<SqlValueHolder<?>> set,
                                                  @NonNull Field sourceField, @NonNull SqlValueCache annotation,
                                                  @Nullable Object accessorInstance, SqlValueHolder.DataSource dataSource) throws IllegalAccessException {
                Validate.isTrue(!SqlValueHolder.class.isAssignableFrom(sourceField.getType()), "Field type must extend SqlValueHolder! (Given %s)", sourceField.getType());

                if(!sourceField.isAccessible()){
                    sourceField.setAccessible(true);
                }

                SqlValueHolder<?> holder = (SqlValueHolder<?>) sourceField.get(accessorInstance);

                if(holder == null){
                    holder = SqlValueHolder.fromAnnotation(annotation);
                    holder.setDataSource(dataSource);

                    if(!Modifier.isFinal(sourceField.getModifiers())) {
                        sourceField.set(accessorInstance, holder);
                    }
                }

                set.add(holder);

                return holder;
            }
        },
        /**
         * Stores modification of an integer and writes the modification to the remote,
         * allowing for concurrent modification.
         * @see io.github.xxyy.common.sql.builder.ConcurrentSqlIntHolder
         */
        INTEGER_MODIFICATION {
            @NonNull @Override
            public ConcurrentSqlIntHolder createHolder(@NonNull Set<SqlValueHolder<?>> set,
                                                       @NonNull Field sourceField, @NonNull SqlValueCache annotation,
                                                       @Nullable Object accessorInstance, SqlValueHolder.DataSource dataSource) throws IllegalAccessException {
                Validate.isTrue(!ConcurrentSqlIntHolder.class.isAssignableFrom(sourceField.getType()), "Field type must extend ConcurrentSqlIntHolder! (Given %s)", sourceField.getType());

                if(!sourceField.isAccessible()){
                    sourceField.setAccessible(true);
                }

                ConcurrentSqlIntHolder holder = (ConcurrentSqlIntHolder) sourceField.get(accessorInstance);

                if(holder == null){
                    holder = ConcurrentSqlIntHolder.fromAnnotation(annotation);
                    holder.setDataSource(dataSource);

                    if(!Modifier.isFinal(sourceField.getModifiers())) {
                        sourceField.set(accessorInstance, holder);
                    }
                }

                set.add(holder);

                return holder;
            }
        },
        /**
         * Stores modification of a double and writes the modification to the remote,
         * allowing for concurrent modification.
         * @see io.github.xxyy.common.sql.builder.ConcurrentSqlIntHolder
         */
        DOUBLE_MODIFICATION {
            @NonNull @Override
            public ConcurrentSqlDoubleHolder createHolder(@NonNull Set<SqlValueHolder<?>> set,
                                                          @NonNull Field sourceField, @NonNull SqlValueCache annotation,
                                                          @Nullable Object accessorInstance, SqlValueHolder.DataSource dataSource) throws IllegalAccessException {
                Validate.isTrue(!ConcurrentSqlDoubleHolder.class.isAssignableFrom(sourceField.getType()), "Field type must extend ConcurrentSqlDoubleHolder! (Given %s)", sourceField.getType());

                if(!sourceField.isAccessible()){
                    sourceField.setAccessible(true);
                }

                ConcurrentSqlDoubleHolder holder = (ConcurrentSqlDoubleHolder) sourceField.get(accessorInstance);

                if(holder == null){
                    holder = ConcurrentSqlDoubleHolder.fromAnnotation(annotation);
                    holder.setDataSource(dataSource);

                    if(!Modifier.isFinal(sourceField.getModifiers())) {
                        sourceField.set(accessorInstance, holder);
                    }
                }

                set.add(holder);

                return holder;
            }
        },
        /**
         * Is an unique identifier for a column.
         * @see io.github.xxyy.common.sql.builder.SqlIdentifierHolder
         */
        OBJECT_IDENTIFIER {
            @NonNull @Override
            public SqlIdentifierHolder<?> createHolder(@NonNull Set<SqlValueHolder<?>> set,
                                                       @NonNull Field sourceField, @NonNull SqlValueCache annotation,
                                                       @Nullable Object accessorInstance, SqlValueHolder.DataSource dataSource) throws IllegalAccessException {
                Validate.isTrue(!SqlIdentifierHolder.class.isAssignableFrom(sourceField.getType()), "Field type must extend SqlIdentifierHolder! (Given %s)", sourceField.getType());

                if(!sourceField.isAccessible()){
                    sourceField.setAccessible(true);
                }

                SqlIdentifierHolder<?> holder = (SqlIdentifierHolder<?>) sourceField.get(accessorInstance);
                holder.setDataSource(dataSource);

                if(holder == null){
                    holder = SqlIdentifierHolder.fromAnnotation(annotation);

                    if(!Modifier.isFinal(sourceField.getModifiers())) {
                        sourceField.set(accessorInstance, holder);
                    }
                }

                set.add(holder);

                return holder;
            }
        },
        /**
         * Is an unique identifier for a column.
         * @see io.github.xxyy.common.sql.builder.SqlIdentifierHolder
         */
        UUID_IDENTIFIER {
            @NonNull @Override
            public SqlIdentifierHolder<?> createHolder(@NonNull Set<SqlValueHolder<?>> set,
                                                       @NonNull Field sourceField, @NonNull SqlValueCache annotation,
                                                       @Nullable Object accessorInstance, SqlValueHolder.DataSource dataSource) throws IllegalAccessException {
                Validate.isTrue(!SqlUUIDHolder.class.isAssignableFrom(sourceField.getType()), "Field type must extend SqlUUIDHolder! (Given %s)", sourceField.getType());

                if(!sourceField.isAccessible()){
                    sourceField.setAccessible(true);
                }

                SqlUUIDHolder holder = (SqlUUIDHolder) sourceField.get(accessorInstance);
                holder.setDataSource(dataSource);

                if(holder == null){
                    holder = SqlUUIDHolder.fromAnnotation(annotation);

                    if(!Modifier.isFinal(sourceField.getModifiers())) {
                        sourceField.set(accessorInstance, holder);
                    }
                }

                set.add(holder);

                return holder;
            }
        };

        /**
         * Creates a new implementation corresponding to this type, if the given Field does not already contain one.
         * If it does, returns the value of the Field.
         * Also populates the given Field with the created instance (if {@link Field#get(Object)} returns a non-null value)
         * and puts the created instance into a Set.
         * @param set Set to save the instance to
         * @param sourceField Field to populate and get information from
         * @return An instance of {@link io.github.xxyy.common.sql.builder.SqlValueHolder} corresponding to the this type and Field.
         */
        @NonNull
        public abstract SqlValueHolder<?> createHolder(@NonNull Set<SqlValueHolder<?>> set, //TODO spaghetti code
                                                       @NonNull Field sourceField, @NonNull SqlValueCache annotation,
                                                       @Nullable Object accessorInstance, @Nullable SqlValueHolder.DataSource dataSource) throws IllegalAccessException;
    }
}
