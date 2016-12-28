/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.common.sql.sane.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Creates entity instances from JDBC result sets.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
public interface JdbcEntityCreator<T> {
    /**
     * Creates an entity instance from the current row in given result set. Note that this uses the
     * <b>current</b> row from the result set, so {@link ResultSet#next()} must have been called
     * already.
     *
     * @param rs the result set from the database
     * @return the created instance
     * @throws SQLException if a database error occurs
     */
    T createFromCurrentRow(ResultSet rs) throws SQLException;
}
