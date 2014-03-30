package io.github.xxyy.common.sql.builder;

import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Provides static utility methods for {@link io.github.xxyy.common.sql.builder.SqlValueHolder} and implementations.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 29.3.14
 */
public class SqlHolders {
    @NonNull
    public static Set<SqlValueHolder> fromClass(@NonNull Class<?> clazz) {
        Set<SqlValueHolder> result = new LinkedHashSet<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(SqlValueCache.class)) {
                SqlValueCache annotation = field.getAnnotation(SqlValueCache.class);
                switch (annotation.type()) {
                    case OBJECT_UPDATE:
                        result.add(SqlValueHolder.fromAnnotation(annotation, field.getType()));
                        break;
                    case OBJECT_IDENTIFIER:
                        result.add(SqlIdentifierHolder.fromAnnotation(annotation, field.getType()));
                        break;
                    case INTEGER_MODIFICATION:
                        result.add(ConcurrentSqlIntHolder.fromAnnotation0(annotation, field.getType()));
                        break;
                    default:
                        throw new AssertionError(annotation.type());
                }
            }
        }

        return result;
    }
}
