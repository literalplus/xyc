/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.sql.builder;

import li.l1t.common.sql.builder.annotation.SqlValueCache;
import li.l1t.common.util.UUIDHelper;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Holds an UUID that is stored as String in database.
 * Supports {@link #updateValue(Object)}, but not {@link #setValue(Object)}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 3.4.14
 * @deprecated Part of the deprecated QueryBuilder API. See {@link QueryBuilder} for details.
 */
@Deprecated
public class SqlUUIDHolder extends SqlIdentifierHolder<UUID> {
    public SqlUUIDHolder(String columnName) {
        super(columnName);
    }

    @Nonnull
    public static SqlUUIDHolder fromAnnotation(@Nonnull SqlValueCache annotation) {
        return new SqlUUIDHolder(annotation.value().intern());
    }

    @Override
    public Object getSnapshot() {
        return getValue().toString();
    }

    @Override
    public boolean isModified() {
        return super.modified;
    }

    @Override
    public boolean supportsOverride() {
        return true;
    }

    @Override
    public void processResultSet(@Nonnull ResultSet resultSet) throws SQLException {
        this.updateValue(UUIDHelper.getFromString(
                resultSet.getString(this.getColumnName())
        ));
    }
}
