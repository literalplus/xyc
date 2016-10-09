/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.sql.sane;

/**
 * Something that has a connection to a SQL data source using the {@link SaneSql} API.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
public interface SqlConnected {
    /**
     * @return the {@link SaneSql} instance currently used by this object
     * @throws IllegalStateException if no connection to the data source can be established in this
     *                               state
     */
    SaneSql sql();
}
