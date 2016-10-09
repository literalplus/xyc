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

import com.google.common.base.Preconditions;

/**
 * Abstract base class for things that are connected to SQL databases using {@link SaneSql}, with
 * the instance provided at construction time.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
public abstract class AbstractSqlConnected implements SqlConnected {
    private final SaneSql saneSql;

    protected AbstractSqlConnected(SaneSql saneSql) {
        this.saneSql = Preconditions.checkNotNull(saneSql, "saneSql");
    }

    @Override
    public SaneSql sql() {
        return saneSql;
    }
}
