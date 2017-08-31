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

package li.l1t.lanatus.sql.builder.melons;

import com.google.common.base.Preconditions;
import li.l1t.common.exception.DatabaseException;
import li.l1t.lanatus.api.LanatusClient;
import li.l1t.lanatus.api.LanatusConnected;
import li.l1t.lanatus.api.builder.CreditMelonsBuilder;
import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.sql.SqlLanatusClient;

import java.util.UUID;

/**
 * Fluent builder interface for crediting of melons with a JDBC SQL data source.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-18
 */
public class SqlCreditMelonsBuilder implements CreditMelonsBuilder, LanatusConnected {
    private final UUID playerId;
    private final SqlLanatusClient client;
    private boolean executed = false;
    private int melonsCount = 0;
    private String comment = "";

    public SqlCreditMelonsBuilder(UUID playerId, SqlLanatusClient client) {
        this.playerId = playerId;
        this.client = client;
    }

    @Override
    public boolean hasBeenExecuted() {
        return executed;
    }

    @Override
    public void build() throws IllegalStateException, DatabaseException {
        Product product = findOrRegisterProduct();
        client().startPurchase(playerId)
                .withProduct(product)
                .withMelonsCost(melonsCount * -1)
                .withComment(comment)
                .build();
        executed = true;
    }

    private Product findOrRegisterProduct() {
        return client().products().registration(PRODUCT_ID)
                .inModule("la-core")
                .withDisplayName("Melonen")
                .withIcon("melon")
                .withMelonsCost(1)
                .withPermanent(false)
                .register();
    }

    @Override
    public CreditMelonsBuilder withMelonsCount(int melonsCount) {
        this.melonsCount = melonsCount;
        return this;
    }

    @Override
    public CreditMelonsBuilder withComment(String comment) {
        Preconditions.checkNotNull(comment, "comment");
        this.comment = comment;
        return this;
    }

    @Override
    public LanatusClient client() {
        return client;
    }
}
