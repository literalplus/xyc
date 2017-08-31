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

package li.l1t.lanatus.sql.purchase;

import li.l1t.common.sql.sane.util.AbstractJdbcEntityCreator;
import li.l1t.lanatus.api.exception.NoSuchPurchaseException;
import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.api.product.ProductRepository;
import li.l1t.lanatus.api.purchase.Purchase;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Creates purchase objects from JDBC SQL result sets.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class JdbcPurchaseCreator extends AbstractJdbcEntityCreator<Purchase> {
    private final ProductRepository productRepository;

    JdbcPurchaseCreator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Purchase createFromCurrentRow(ResultSet rs) throws SQLException {
        return new SqlPurchase(
                uuid(rs, "id"), uuid(rs, "player_uuid"),
                findProduct(rs), rs.getTimestamp("created").toInstant(),
                rs.getString("data"), rs.getString("comment"), rs.getInt("melonscost")
        );
    }

    private Product findProduct(ResultSet rs) throws SQLException, NoSuchPurchaseException {
        return productRepository.findById(uuid(rs, "product_id"));
    }

}
