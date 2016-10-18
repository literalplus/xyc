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

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.base.VerifyException;
import li.l1t.common.sql.sane.AbstractSqlConnected;
import li.l1t.common.sql.sane.SaneSql;

import java.util.UUID;

/**
 * Writes new products to the database. Note that this does <b>not</b> support updating of existing
 * products.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-18
 */
class JdbcProductWriter extends AbstractSqlConnected {
    JdbcProductWriter(SaneSql sql) {
        super(sql);
    }

    public void write(SqlProduct product) throws VerifyException {
        Preconditions.checkNotNull(product, "product");
        int rowsAffected = insertRaw(
                product.getUniqueId(), product.getModule(), product.getDisplayName(), product.getDescription(),
                product.getIconName(), product.getMelonsCost(), product.isActive()
        );
        Verify.verify(rowsAffected == 1, "expected insert to affect single row, was: ", product, rowsAffected);
    }

    private int insertRaw(UUID productId, String module, String displayName, String description,
                          String iconName, int melonsCost, boolean active) {
        return sql().updateRaw("INSERT INTO " + SqlProductRepository.TABLE_NAME + " " +
                        "SET id=?, module=?, displayname=?, description=?, " +
                        "icon=?, melonscost=?, active=?",
                productId.toString(), module, displayName, description, iconName, melonsCost, active
        );
    }
}
