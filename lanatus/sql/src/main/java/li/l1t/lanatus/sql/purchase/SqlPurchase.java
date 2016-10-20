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

import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.api.purchase.Purchase;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a purchase backed by a JDBC SQL data store.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class SqlPurchase implements Purchase {
    private final UUID uniqueId;
    private final UUID playerId;
    private final Product product;
    private final Instant creationInstant;
    private final String data;
    private final String comment;
    private final int melonsCost;

    SqlPurchase(UUID uniqueId, UUID playerId, Product product, Instant creationInstant,
                String data, String comment, int melonsCost) {
        this.uniqueId = uniqueId;
        this.playerId = playerId;
        this.product = product;
        this.creationInstant = creationInstant;
        this.data = data;
        this.comment = comment;
        this.melonsCost = melonsCost;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
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
    public Instant getCreationInstant() {
        return creationInstant;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public int getMelonsCost() {
        return melonsCost;
    }
}
