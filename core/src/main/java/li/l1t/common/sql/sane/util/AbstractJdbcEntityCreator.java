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
import java.util.UUID;

/**
 * Abstract base class for JDBC entity creators providing common methods.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
public abstract class AbstractJdbcEntityCreator<T> implements JdbcEntityCreator<T> {
    protected UUID uuid(ResultSet rs, String column) throws SQLException {
        return UUID.fromString(rs.getString(column));
    }
}
