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

package li.l1t.common.sql.builder.annotation;

import li.l1t.common.sql.builder.*;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * This interface can be applied to fields which represent object values in a database.
 * This is intended to be used with {@link SqlValueHolder}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.3.14
 * @deprecated Part of the deprecated QueryBuilder API. See {@link QueryBuilder} for details.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Deprecated
public @interface SqlValueCache { //TO\DO In case I ever open-source XYC, this prime example of generics abuse has to be fixed

    /**
     * @return the name of the column represented by the field.
     */
    String value(); //Inconvenient name for easier syntax

    /**
     * @return The type of this cache.
     */
    SqlValueCache.Type type() default Type.OBJECT_UPDATE;

    /**
     * This is kept here to erase the need for a second annotation (and checking for it!).
     * It helps doubling performance, ok?
     *
     * @return Expected number type for the holder, if {@link ConcurrentSqlNumberHolder} and {@link Type#NUMBER_MODIFICATION}.
     */
    Class<? extends Number> numberType() default Integer.class;

    enum Type { //Yes, I know this thing is abusing generix very awfully..Dunno how to do it in any other way ._.
        /**
         * Updates objects with overriding values.
         *
         * @see SqlValueHolder
         */
        OBJECT_UPDATE {
            @Override
            @SuppressWarnings("unchecked") //Need to fix this somehow?
            protected Class getExpectedClass() { //Yea, this is totally valid and good practice!!!!!!1
                return SqlValueHolder.class;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected <T extends SqlValueHolder<?>> AnnotationToHolderFactory<T> getFactory() {
                return annotation -> (T) SqlValueHolder.fromAnnotation(annotation);
            }
        },
        /**
         * Stores modification of a number and writes the modification to the remote,
         * allowing for concurrent modification.
         *
         * @see ConcurrentSqlNumberHolder
         */
        NUMBER_MODIFICATION {
            @Override
            @SuppressWarnings("unchecked") //Need to fix this somehow?
            protected Class getExpectedClass() {
                return ConcurrentSqlNumberHolder.class;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected <T extends SqlValueHolder<?>> AnnotationToHolderFactory<T> getFactory() {
                return annotation -> (T) ConcurrentSqlNumberHolder.fromAnnotation(annotation);
            }
        },
        /**
         * Is an unique identifier for a column.
         *
         * @see SqlIdentifierHolder
         */
        OBJECT_IDENTIFIER {
            @Override
            @SuppressWarnings("unchecked") //Need to fix this somehow?
            protected Class getExpectedClass() { //Yea, this is totally valid and good practice!!!!!!1
                return SqlIdentifierHolder.class;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected <T extends SqlValueHolder<?>> AnnotationToHolderFactory<T> getFactory() { //dat language syntax overhead thing... Can't wait for Lambdas <3
                return annotation -> (T) SqlIdentifierHolder.fromAnnotation(annotation);
            }
        },
        /**
         * Is an unique identifier for a column.
         *
         * @see SqlIdentifierHolder
         */
        UUID_IDENTIFIER {
            @Override
            @SuppressWarnings("unchecked") //Need to fix this somehow?
            protected <T extends SqlValueHolder<?>> Class<T> getExpectedClass() {
                return (Class<T>) SqlUUIDHolder.class;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected <T extends SqlValueHolder<?>> AnnotationToHolderFactory<T> getFactory() {
                return annotation -> (T) SqlUUIDHolder.fromAnnotation(annotation);
            }
        };

        private static <T extends SqlValueHolder<?>> T skeletonCreateHolder(Field sourceField,
                                                                            SqlValueCache annotation, Object accessorInstance,
                                                                            SqlValueHolder.DataSource dataSource, Class<?> expectedClass,
                                                                            AnnotationToHolderFactory<T> factory) throws IllegalAccessException {
            Validate.isTrue(expectedClass.isAssignableFrom(sourceField.getType()), "Field is of invalid type! (Given %s)", sourceField.getType());

            @SuppressWarnings("unchecked") //Checked above - See Validate.isTrue(...)
                    T holder = (T) sourceField.get(accessorInstance);

            if (holder == null) {
                holder = factory.fromAnnotation(annotation);
                holder.setDataSource(dataSource);

                if (!Modifier.isFinal(sourceField.getModifiers())) {
                    sourceField.set(accessorInstance, holder);
                }
            }

            return holder;
        }

        protected abstract <T extends SqlValueHolder<?>> Class<T> getExpectedClass();

        protected abstract <T extends SqlValueHolder<?>> AnnotationToHolderFactory<T> getFactory();

        /**
         * Creates a new implementation corresponding to this type, if the given Field does not already contain one.
         * If it does, returns the value of the Field.
         * Also populates the given Field with the created instance (if {@link Field#get(Object)} returns a non-null value)
         * and puts the created instance into a Set.
         *
         * @param sourceField      Field to populate and get information from
         * @param accessorInstance the instance required to access the class holding given field, or NULL if the field is static
         * @param annotation       the annotation providing information about what kind of holder to generate
         * @param dataSource       the data source to use to fetch data from a database
         * @return An instance of {@link SqlValueHolder} corresponding to the this type and Field.
         * @throws java.lang.IllegalAccessException if the field couldn't be accessed
         */
        @Nonnull
        public SqlValueHolder<?> createHolder(@Nonnull Field sourceField, @Nonnull SqlValueCache annotation,
                                              @Nullable Object accessorInstance, @Nullable SqlValueHolder.DataSource dataSource) throws IllegalAccessException {
            return skeletonCreateHolder(sourceField, annotation, accessorInstance, dataSource, getExpectedClass(), getFactory()); //generix hax, kthx
        }
    }

    /**
     * Makes {@link SqlValueHolder}s from annotations.
     */
    interface AnnotationToHolderFactory<T> {
        @Nonnull
        T fromAnnotation(@Nonnull SqlValueCache annotation);
    }
}
