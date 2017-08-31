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

package li.l1t.common.sql.sane.util;

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
    protected final JdbcEntityCreator<? extends T> creator;

    protected AbstractJdbcFetcher(JdbcEntityCreator<? extends T> creator, SaneSql saneSql) {
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
