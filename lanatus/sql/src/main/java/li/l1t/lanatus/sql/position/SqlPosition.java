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

import li.l1t.lanatus.api.position.Position;
import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.api.purchase.Purchase;

import java.util.UUID;

/**
 * Represents a position backed by a SQL data store.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class SqlPosition implements Position {
    private final UUID purchaseId;
    private final UUID playerId;
    private final Product product;
    private final String data;

    SqlPosition(UUID purchaseId, UUID playerId, Product product, String data) {
        this.purchaseId = purchaseId;
        this.playerId = playerId;
        this.product = product;
        this.data = data;
    }

    SqlPosition(Purchase purchase) {
        this(purchase.getUniqueId(), purchase.getPlayerId(), purchase.getProduct(), purchase.getData());
    }

    @Override
    public UUID getPurchaseId() {
        return purchaseId;
    }

    @Override
    public UUID getPlayerId() {
        return playerId;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "SqlPosition{" +
                "purchaseId=" + purchaseId +
                ", playerId=" + playerId +
                ", product=" + product +
                ", data='" + data + '\'' +
                '}';
    }
}
