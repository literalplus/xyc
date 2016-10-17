/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql.position;

import li.l1t.lanatus.api.exception.NoSuchProductException;
import li.l1t.lanatus.api.position.Position;
import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.api.product.ProductRepository;
import li.l1t.lanatus.sql.common.AbstractJdbcEntityCreator;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Creates position instances from JDBC result sets.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class JdbcPositionCreator extends AbstractJdbcEntityCreator<Position> {
    private final ProductRepository productRepository;

    JdbcPositionCreator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Position createFromCurrentRow(ResultSet rs) throws SQLException {
        return new SqlPosition(
                uuid(rs, "purchase_id"), uuid(rs, "player_uuid"),
                findProduct(rs), rs.getString("data")
        );
    }

    private Product findProduct(ResultSet rs) throws SQLException, NoSuchProductException {
        return productRepository.findById(uuid(rs, "product_id"));
    }
}
