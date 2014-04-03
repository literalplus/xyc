package io.github.xxyy.common.sql.builder;

import lombok.NonNull;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

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
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.3.14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqlValueCache {
    /**
     * @return the name of the column represented by the field.
     */
    @NonNull String value(); //Inconvenient name for easier syntax

    /**
     * @return The type of this cache.
     */
    @NonNull SqlValueCache.Type type() default Type.OBJECT_UPDATE;

    /**
     * @return whether annotation processors shall skip this field.
     */
    boolean skip() default false;

    public enum Type { //Yes, I know this thing is abusing generix very awfully..Dunno how to do it in any other way ._.
        /**
         * Updates objects with overriding values.
         *
         * @see io.github.xxyy.common.sql.builder.SqlValueHolder
         */
        OBJECT_UPDATE {
            @Override @SuppressWarnings("unchecked") //Need to fix this somehow?
            protected <T extends SqlValueHolder<?>> Class getExpectedClass() { //Yea, this is totally valid and good practice!!!!!!1
                return SqlValueHolder.class;
            }

            @Override
            protected <T extends SqlValueHolder<?>> AnnotationToHolderFactory<T> getFactory() {
                return new AnnotationToHolderFactory<T>() {
                    @NotNull @Override @SuppressWarnings("unchecked") //Need to fix this somehow?
                    public T fromAnnotation(@NotNull SqlValueCache annotation) {
                        return (T) SqlValueHolder.fromAnnotation(annotation);
                    }
                };
            }
        },
        /**
         * Stores modification of an integer and writes the modification to the remote,
         * allowing for concurrent modification.
         *
         * @see io.github.xxyy.common.sql.builder.ConcurrentSqlIntHolder
         */
        INTEGER_MODIFICATION {
            @Override @SuppressWarnings("unchecked") //Need to fix this somehow?
            protected <T extends SqlValueHolder<?>> Class<T> getExpectedClass() {
                return (Class<T>) ConcurrentSqlIntHolder.class;
            }

            @Override
            protected <T extends SqlValueHolder<?>> AnnotationToHolderFactory<T> getFactory() {
                return new AnnotationToHolderFactory<T>() {
                    @NotNull @Override @SuppressWarnings("unchecked") //Need to fix this somehow?
                    public T fromAnnotation(@NotNull SqlValueCache annotation) {
                        return (T) ConcurrentSqlIntHolder.fromAnnotation(annotation);
                    }
                };
            }
        },
        /**
         * Stores modification of a double and writes the modification to the remote,
         * allowing for concurrent modification.
         *
         * @see io.github.xxyy.common.sql.builder.ConcurrentSqlIntHolder
         */
        DOUBLE_MODIFICATION {
            @Override @SuppressWarnings("unchecked") //Need to fix this somehow?
            protected <T extends SqlValueHolder<?>> Class<T> getExpectedClass() {
                return (Class<T>) ConcurrentSqlDoubleHolder.class;
            }

            @Override
            protected <T extends SqlValueHolder<?>> AnnotationToHolderFactory<T> getFactory() {
                return new AnnotationToHolderFactory<T>() {
                    @NotNull @Override @SuppressWarnings("unchecked") //Need to fix this somehow?
                    public T fromAnnotation(@NotNull SqlValueCache annotation) {
                        return (T) ConcurrentSqlDoubleHolder.fromAnnotation(annotation);
                    }
                };
            }
        },
        /**
         * Is an unique identifier for a column.
         *
         * @see io.github.xxyy.common.sql.builder.SqlIdentifierHolder
         */
        OBJECT_IDENTIFIER {
            @Override @SuppressWarnings("unchecked") //Need to fix this somehow?
            protected <T extends SqlValueHolder<?>> Class getExpectedClass() { //Yea, this is totally valid and good practice!!!!!!1
                return SqlIdentifierHolder.class;
            }

            @Override
            protected <T extends SqlValueHolder<?>> AnnotationToHolderFactory<T> getFactory() { //dat language syntax overhead thing... Can't wait for Lambdas <3
                return new AnnotationToHolderFactory<T>() {
                    @NotNull @Override @SuppressWarnings("unchecked") //Need to fix this somehow?
                    public T fromAnnotation(@NotNull SqlValueCache annotation) {
                        return (T) SqlIdentifierHolder.fromAnnotation(annotation);
                    }
                };
            }
        },
        /**
         * Is an unique identifier for a column.
         *
         * @see io.github.xxyy.common.sql.builder.SqlIdentifierHolder
         */
        UUID_IDENTIFIER {
            @Override @SuppressWarnings("unchecked") //Need to fix this somehow?
            protected <T extends SqlValueHolder<?>> Class<T> getExpectedClass() {
                return (Class<T>) SqlUUIDHolder.class;
            }

            @Override
            protected <T extends SqlValueHolder<?>> AnnotationToHolderFactory<T> getFactory() {
                return new AnnotationToHolderFactory<T>() {
                    @NotNull @Override @SuppressWarnings("unchecked") //Need to fix this somehow?
                    public T fromAnnotation(@NotNull SqlValueCache annotation) {
                        return (T) SqlUUIDHolder.fromAnnotation(annotation);
                    }
                };
            }
        };

        protected abstract <T extends SqlValueHolder<?>> Class<T> getExpectedClass();

        protected abstract <T extends SqlValueHolder<?>> AnnotationToHolderFactory<T> getFactory();

        private static <T extends SqlValueHolder<?>> T skeletonCreateHolder(Set<SqlValueHolder<?>> set, Field sourceField, 
                                                                   SqlValueCache annotation, Object accessorInstance,
                                                                   SqlValueHolder.DataSource dataSource, Class<?> expectedClass,
                                                                   @NonNull AnnotationToHolderFactory<T> factory) throws IllegalAccessException {
            Validate.isTrue(expectedClass.isAssignableFrom(sourceField.getType()), "Field is of invalid type! (Given %s)", sourceField.getType());

            if (!sourceField.isAccessible()) {
                sourceField.setAccessible(true);
            }

            @SuppressWarnings("unchecked") //Checked above - See Validate.isTrue(...)
            T holder = (T) sourceField.get(accessorInstance);

            if (holder == null) {
                holder = factory.fromAnnotation(annotation);
                holder.setDataSource(dataSource);

                if (!Modifier.isFinal(sourceField.getModifiers())) {
                    sourceField.set(accessorInstance, holder);
                }
            }

            set.add(holder);

            return holder;
        }

        /**
         * Creates a new implementation corresponding to this type, if the given Field does not already contain one.
         * If it does, returns the value of the Field.
         * Also populates the given Field with the created instance (if {@link Field#get(Object)} returns a non-null value)
         * and puts the created instance into a Set.
         *
         * @param set         Set to save the instance to
         * @param sourceField Field to populate and get information from
         * @return An instance of {@link io.github.xxyy.common.sql.builder.SqlValueHolder} corresponding to the this type and Field.
         */
        @NonNull
        public SqlValueHolder<?> createHolder(@NonNull Set<SqlValueHolder<?>> set,
                                              @NonNull Field sourceField, @NonNull SqlValueCache annotation,
                                              @Nullable Object accessorInstance, @Nullable SqlValueHolder.DataSource dataSource) throws IllegalAccessException {
            return skeletonCreateHolder(set, sourceField, annotation, accessorInstance, dataSource, getExpectedClass(), getFactory()); //generix hax, kthx
        }
    }

    /**
     * Makes {@link io.github.xxyy.common.sql.builder.SqlValueHolder}s from annotations.
     */
    static interface AnnotationToHolderFactory<T> {
        @NotNull
        T fromAnnotation(@NotNull SqlValueCache annotation);
    }
}
