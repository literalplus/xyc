/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql;

import li.l1t.lanatus.api.LanatusRepository;

/**
 * Abstract base class for SQL LanatusRepository implementations providing convenience methods for
 * database access.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-29
 */
public abstract class AbstractSqlLanatusRepository implements LanatusRepository {
    private final SqlLanatusClient client;

    public AbstractSqlLanatusRepository(SqlLanatusClient client) {
        this.client = client;
    }

    @Override
    public SqlLanatusClient client() {
        return client;
    }
}
