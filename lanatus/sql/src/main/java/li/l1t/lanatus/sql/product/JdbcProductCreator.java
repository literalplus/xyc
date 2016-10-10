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

import li.l1t.lanatus.api.product.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Creates product instances from results of JDBC queries.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class JdbcProductCreator {
    public Product createFromCurrentRow(ResultSet rs) throws SQLException {
        return new SqlProduct(
                UUID.fromString(rs.getString("id")), rs.getString("module"),
                rs.getString("name"), rs.getString("displayname"), rs.getString("description"),
                rs.getString("icon"), rs.getInt("melonscost"), rs.getBoolean("active")
        );
    }
}
