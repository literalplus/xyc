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
        this.uniqueId = Preconditions.checkNotNull(uniqueId, "uniqueId");
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

    @Override
    public String toString() {
        return "SqlPurchase{" +
                "uniqueId=" + uniqueId +
                ", playerId=" + playerId +
                ", product=" + product +
                ", creationInstant=" + creationInstant +
                ", data='" + data + '\'' +
                ", comment='" + comment + '\'' +
                ", melonsCost=" + melonsCost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SqlPurchase)) return false;

        SqlPurchase that = (SqlPurchase) o;

        return uniqueId.equals(that.uniqueId);
    }

    @Override
    public int hashCode() {
        return uniqueId != null ? uniqueId.hashCode() : 0;
    }
}
