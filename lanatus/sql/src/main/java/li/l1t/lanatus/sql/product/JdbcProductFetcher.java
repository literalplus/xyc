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

package li.l1t.lanatus.sql.product;

import li.l1t.common.exception.DatabaseException;
import li.l1t.common.sql.sane.SaneSql;
import li.l1t.common.sql.sane.result.QueryResult;
import li.l1t.lanatus.api.exception.NoSuchProductException;
import li.l1t.lanatus.api.product.Product;
import li.l1t.common.sql.sane.util.JdbcEntityCreator;

import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

/**
 * Fetches products from a JDBC SQL data source.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class JdbcProductFetcher extends li.l1t.common.sql.sane.util.AbstractJdbcFetcher<Product> {
    JdbcProductFetcher(JdbcEntityCreator<Product> creator, SaneSql saneSql) {
        super(creator, saneSql);
    }

    public Product fetchById(UUID productId) {
        try (QueryResult result = selectSingle(productId)) {
            if (proceedToNextRow(result)) {
                return entityFromCurrentRow(result);
            } else {
                throw new NoSuchProductException("product with id " + productId);
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    private QueryResult selectSingle(UUID productId) {
        return select("WHERE id=?", productId.toString());
    }

    @Override
    protected String buildSelect(String whereClause) {
        return "SELECT id, module, displayname, description, icon, melonscost, active, permanent " +
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
}
