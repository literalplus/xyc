/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql.product;

import com.google.common.collect.ImmutableList;
import li.l1t.common.exception.DatabaseException;
import li.l1t.common.sql.sane.AbstractSqlConnected;
import li.l1t.common.sql.sane.SaneSql;
import li.l1t.common.sql.sane.result.QueryResult;
import li.l1t.lanatus.api.exception.NoSuchRowException;
import li.l1t.lanatus.api.product.Product;

import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

/**
 * Fetches products from a JDBC SQL data source.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class JdbcProductFetcher extends AbstractSqlConnected {
    private final JdbcProductCreator creator = new JdbcProductCreator();

    JdbcProductFetcher(SaneSql saneSql) {
        super(saneSql);
    }

    public Product fetchById(UUID productId) {
        try (QueryResult result = selectSingle(productId)) {
            if (proceedToNextRow(result)) {
                return creator.createFromCurrentRow(result.rs());
            } else {
                throw new NoSuchRowException("product with id " + productId);
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    private boolean proceedToNextRow(QueryResult result) throws SQLException {
        return result.rs().next();
    }

    private QueryResult selectSingle(UUID productId) {
        return select("WHERE id=?", productId.toString());
    }

    private QueryResult select(String whereClause, Object... parameters) {
        return sql().query(buildSelect(whereClause), parameters);
    }

    private String buildSelect(String whereClause) {
        return "SELECT id, module, name, displayname, description, item, melonscost, active " +
                "FROM " + SqlProductRepository.TABLE_NAME + " " +
                whereClause;
    }

    public Collection<Product> fetchByQuery(ProductQuery query) {
        try (QueryResult result = selectByQuery(query)) {
            return collectAll(result);
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    private QueryResult selectByQuery(ProductQuery query) {
        return select(query.getWhereClause(), query.getParameters());
    }

    private Collection<Product> collectAll(QueryResult result) throws SQLException {
        ImmutableList.Builder<Product> products = ImmutableList.builder();
        while (proceedToNextRow(result)) {
            products.add(creator.createFromCurrentRow(result.rs()));
        }
        return products.build();
    }
}
