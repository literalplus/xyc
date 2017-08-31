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

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.base.VerifyException;
import li.l1t.common.sql.sane.AbstractSqlConnected;
import li.l1t.common.sql.sane.SaneSql;

import java.time.Instant;
import java.util.UUID;

/**
 * Writes new purchases to the database. Note that this does <b>not</b> support updating existing
 * purchases.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-18
 */
class JdbcPurchaseWriter extends AbstractSqlConnected {
    JdbcPurchaseWriter(SaneSql sql) {
        super(sql);
    }

    public void write(SqlPurchase purchase) throws VerifyException {
        Preconditions.checkNotNull(purchase, "purchase");
        int rowsAffected = insertRaw(
                purchase.getUniqueId(), purchase.getPlayerId(), purchase.getProduct().getUniqueId(),
                purchase.getData(), purchase.getComment(), purchase.getMelonsCost()
        );
        Verify.verify(rowsAffected == 1, "expected insert to affect single row, was: ", purchase, rowsAffected);
    }

    private int insertRaw(UUID purchaseId, UUID playerId, UUID productId, String data, String comment, int melonsCost) {
        return sql().updateRaw("INSERT INTO " + SqlPurchaseRepository.TABLE_NAME + " " +
                        "SET id=?, player_uuid=?, product_id=?, created=?, " +
                        "data=?, comment=?, melonscost=?",
                purchaseId.toString(), playerId.toString(), productId.toString(), Instant.now(),
                data, comment, melonsCost
        );
    }
}
