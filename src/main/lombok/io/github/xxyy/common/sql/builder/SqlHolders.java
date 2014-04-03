package io.github.xxyy.common.sql.builder;

import com.google.common.collect.Lists;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Provides static utility methods for {@link io.github.xxyy.common.sql.builder.SqlValueHolder} and implementations.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 29.3.14
 */
public class SqlHolders {
    /**
     * Adds all annotated SqlHolders of a class to a set and populates the fields with the
     * {@link io.github.xxyy.common.sql.builder.SqlValueHolder} implementation corresponding to the annotated {@link SqlValueCache#type()}.
     *
     * @param clazz            Class to process
     * @param accessorInstance Instance of clazz used to access and set fields.
     *                         May be null if, and only if all {@link io.github.xxyy.common.sql.builder.SqlValueCache} annotated fields are static.
     * @param dataSource Optional DataSource that can be used by SqlValueHolders to acquire data.
     * @return SqlHolders of {@code clazz}.
     * @throws java.lang.NullPointerException If an {@link io.github.xxyy.common.sql.builder.SqlValueCache} annotated field is encountered and {@code accessorInstance} is null.
     * @throws java.lang.IllegalAccessException If an annotated field could not be accessed.
     */
    @NonNull
    public static Set<SqlValueHolder<?>> processClass(@NonNull Class<?> clazz, @Nullable Object accessorInstance,
                                                      @Nullable SqlValueHolder.DataSource dataSource) throws IllegalAccessException {
        Set<SqlValueHolder<?>> result = new LinkedHashSet<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(SqlValueCache.class)) {
                SqlValueCache annotation = field.getAnnotation(SqlValueCache.class);
                if (!annotation.skip()) {
                    if (accessorInstance == null && !Modifier.isStatic(field.getModifiers())) {
                        throw new NullPointerException(String.format("Encountered a non-static field marked for processing, but no accessor instance given! (At field %s)", field.getName()));
                    }

                    annotation.type().createHolder(result, field, annotation, accessorInstance, dataSource);
                }
            }
        }

        return result;
    }

    public static void updateFromResultSet(@NonNull Collection<SqlValueHolder<?>> holders, @NonNull ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        List<String> availableNames = Lists.newLinkedList();
        int columnCount = rsmd.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            availableNames.add(rsmd.getColumnName(i));
        }

        for(SqlValueHolder holder : holders){ //Seems like the only solution that supports wildcards
            if(holder != null && availableNames.contains(holder.getColumnName())){
                holder.updateValue(resultSet.getObject(holder.getColumnName())); //TODO: What to do if the cast fails?
                                                                                 //Current implementation saves comparing classes in this performance-critical part
            }
        }
    }
}
