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
                product.getIconName(), product.getMelonsCost(), product.isActive(), product.isPermanent()
        );
        Verify.verify(rowsAffected == 1, "expected insert to affect single row, was: ", product, rowsAffected);
    }

    private int insertRaw(UUID productId, String module, String displayName, String description,
                          String iconName, int melonsCost, boolean active, boolean permanent) {
        return sql().updateRaw("INSERT INTO " + SqlProductRepository.TABLE_NAME + " " +
                        "SET id=?, module=?, displayname=?, description=?, " +
                        "icon=?, melonscost=?, active=?, permanent=?",
                productId.toString(), module, displayName, description,
                iconName, melonsCost, active, permanent
        );
    }
}
