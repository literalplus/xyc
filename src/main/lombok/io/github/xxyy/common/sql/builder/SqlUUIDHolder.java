package io.github.xxyy.common.sql.builder;

import lombok.NonNull;

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

    public void updateValue(String uuidString){
        this.updateValue(io.github.xxyy.common.lib.net.minecraft.server.UtilUUID.getFromString(uuidString));
    }

    @NonNull
    public static SqlUUIDHolder fromAnnotation(@NonNull SqlValueCache annotation){
        return new SqlUUIDHolder(annotation.value());
    }
}
