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
