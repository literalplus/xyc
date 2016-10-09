/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.sql.sane.statement;

import java.sql.PreparedStatement;

/**
 * Provides prepared statements filled with parameters.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
public interface StatementProvider {
    /**
     * Creates a new statement filled with some parameters. {@code ?} placeholders in the query
     * string are substituted by the corresponding parameter (in order) by the database driver
     * natively, providing strong defense against SQL Injection attacks. Concatenation of data to
     * the query string should be avoided.
     *
     * @param sql        the query string for the database
     * @param parameters the parameters for the query string
     * @return the created statement
     */
    PreparedStatement create(String sql, Object... parameters);
}
