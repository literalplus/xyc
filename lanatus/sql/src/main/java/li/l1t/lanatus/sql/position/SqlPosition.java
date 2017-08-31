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

package li.l1t.lanatus.sql.position;

import com.google.common.base.Preconditions;
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
        this.purchaseId = Preconditions.checkNotNull(purchaseId, "purchaseId");
        this.playerId = Preconditions.checkNotNull(playerId, "playerId");
        this.product = Preconditions.checkNotNull(product, "product");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SqlPosition)) return false;
        SqlPosition that = (SqlPosition) o;
        if (!purchaseId.equals(that.purchaseId)) return false;
        if (!playerId.equals(that.playerId)) return false;
        return product.equals(that.product);
    }

    @Override
    public int hashCode() {
        int result = purchaseId.hashCode();
        result = 31 * result + playerId.hashCode();
        result = 31 * result + (product != null ? product.hashCode() : 0);
        return result;
    }
}
