package io.github.xxyy.common.sql.builder.annotation;

import io.github.xxyy.common.sql.builder.ConcurrentSqlNumberHolder;
import io.github.xxyy.common.sql.builder.SqlValueHolder;
import org.apache.commons.lang.Validate;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * Stores meta info for {@link io.github.xxyy.common.sql.builder.ConcurrentSqlNumberHolder}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 3.4.14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface SqlNumberCache {
    /**
     * @return the name of the column represented by the field.
     */
    String value();

    /**
     * @return The Number class for this cache. Must be the actual Object, not the primitive.
     * @see io.github.xxyy.common.util.math.NumberHelper#getOperator(Class)
     */
    Class<? extends Number> numberType() default Integer.class;

    /**
     * @return whether annotation processors shall skip this field.
     */
    boolean skip() default false;

    public static class Type {
        public static ConcurrentSqlNumberHolder<?> createHolder(Set<SqlValueHolder<?>> set, Field sourceField,
                                                                           SqlNumberCache annotation, Object accessorInstance,
                                                                           SqlValueHolder.DataSource dataSource) throws IllegalAccessException {
            Validate.isTrue(ConcurrentSqlNumberHolder.class.isAssignableFrom(sourceField.getType()), "Field is of invalid type! (Given %s)", sourceField.getType());

            if (!sourceField.isAccessible()) {
                sourceField.setAccessible(true);
            }

            @SuppressWarnings("unchecked") //Checked above - See Validate.isTrue(...)
            ConcurrentSqlNumberHolder<?> holder = (ConcurrentSqlNumberHolder<?>) sourceField.get(accessorInstance);

            if (holder == null) {
                holder = ConcurrentSqlNumberHolder.fromAnnotation(annotation);
                holder.setDataSource(dataSource);

                if (!Modifier.isFinal(sourceField.getModifiers())) {
                    sourceField.set(accessorInstance, holder);
                }
            }

            set.add(holder);

            return holder;
        }
    }
}
