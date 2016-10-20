/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
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
        Preconditions.checkArgument(melonsCount >= 0, "melonsCount must not be negative");
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
