/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
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
