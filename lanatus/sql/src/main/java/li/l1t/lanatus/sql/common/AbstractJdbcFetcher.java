/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql.common;

import com.google.common.collect.ImmutableList;
import li.l1t.common.sql.sane.AbstractSqlConnected;
import li.l1t.common.sql.sane.SaneSql;
import li.l1t.common.sql.sane.result.QueryResult;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Abstract base class for fetchers that fetch data from JDBC SQL data sources.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
public abstract class AbstractJdbcFetcher<T> extends AbstractSqlConnected {
    protected final JdbcEntityCreator<T> creator;

    public AbstractJdbcFetcher(JdbcEntityCreator<T> creator, SaneSql saneSql) {
        super(saneSql);
        this.creator = creator;
    }

    protected boolean proceedToNextRow(QueryResult result) throws SQLException {
        return result.rs().next();
    }

    protected QueryResult select(String whereClause, Object... parameters) {
        return sql().query(buildSelect(whereClause), parameters);
    }

    protected abstract String buildSelect(String whereClause);

    protected Collection<T> collectAll(QueryResult result) throws SQLException {
        ImmutableList.Builder<T> products = ImmutableList.builder();
        while (proceedToNextRow(result)) {
            products.add(entityFromCurrentRow(result));
        }
        return products.build();
    }

    protected T entityFromCurrentRow(QueryResult result) throws SQLException {
        return creator.createFromCurrentRow(result.rs());
    }
}
