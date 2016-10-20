/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
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
