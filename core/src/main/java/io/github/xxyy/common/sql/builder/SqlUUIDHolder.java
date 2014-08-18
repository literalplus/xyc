/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without permission from the
 *  original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.sql.builder;

import org.jetbrains.annotations.NotNull;

import io.github.xxyy.common.lib.net.minecraft.server.UtilUUID;
import io.github.xxyy.common.sql.builder.annotation.SqlValueCache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Holds an UUID that is stored as String in database.
 * Supports {@link #updateValue(Object)}, but not {@link #setValue(Object)}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 3.4.14
 */
public class SqlUUIDHolder extends SqlIdentifierHolder<UUID> {
    public SqlUUIDHolder(String columnName) {
        super(columnName);
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
    public void processResultSet(@NotNull ResultSet resultSet) throws SQLException {
        //noinspection UnnecessaryFullyQualifiedName
        this.updateValue(UtilUUID.getFromString(
                resultSet.getString(this.getColumnName())
        ));
    }

    @NotNull
    public static SqlUUIDHolder fromAnnotation(@NotNull SqlValueCache annotation) {
        return new SqlUUIDHolder(annotation.value().intern());
    }
}
