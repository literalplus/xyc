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
import li.l1t.common.exception.DatabaseException;
import li.l1t.common.sql.sane.SingleSql;
import li.l1t.common.sql.sane.SqlConnected;
import li.l1t.common.sql.sane.scoped.ScopedSession;
import li.l1t.lanatus.api.LanatusConnected;
import li.l1t.lanatus.api.account.MutableAccount;
import li.l1t.lanatus.api.builder.PurchaseBuilder;
import li.l1t.lanatus.api.exception.AccountConflictException;
import li.l1t.lanatus.api.exception.NoSuchProductException;
import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.api.purchase.Purchase;
import li.l1t.lanatus.sql.SqlLanatusClient;

import java.time.Instant;
import java.util.UUID;

/**
 * Implementation of a builder that builds purchases stored in a JDBC SQL database.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-18
 */
public class SqlPurchaseBuilder implements PurchaseBuilder, LanatusConnected, SqlConnected {
    private final UUID purchaseId = UUID.randomUUID();
    private final UUID playerId;
    private final SqlLanatusClient client;
    private final JdbcPurchaseWriter purchaseWriter;
    private Purchase purchase;
    private UUID productId;
    private int melonsCost = Integer.MAX_VALUE;
    private String data = "";
    private String comment = "";

    public SqlPurchaseBuilder(UUID playerId, SqlLanatusClient client) {
        this.client = Preconditions.checkNotNull(client, "client");
        this.playerId = Preconditions.checkNotNull(playerId, "playerId");
        this.purchaseWriter = new JdbcPurchaseWriter(sql());
    }

    @Override
    public UUID getPurchaseId() {
        return purchaseId;
    }

    @Override
    public boolean hasBeenBuilt() {
        return purchase != null;
    }

    private void checkAlreadyExecuted() {
        Preconditions.checkState(hasBeenBuilt(), "purchase not built yet");
    }

    @Override
    public Purchase getPurchase() {
        checkAlreadyExecuted();
        return purchase;
    }

    private void checkNotYetExecuted() {
        Preconditions.checkState(!hasBeenBuilt(), "purchase already built");
    }

    @Override
    public void build() throws IllegalStateException, NoSuchProductException, DatabaseException {
        checkNotYetExecuted();
        try (ScopedSession scoped = sql().scoped().tx()) {
            makeSurePlayerAccountExists();
            Product product = findProduct();
            SqlPurchase purchase = createPurchase(product);
            purchaseWriter.write(purchase);
            MutableAccount account = client().accounts().findMutable(playerId); //needs to exist for purchase
            account.modifyMelonsCount(purchase.getMelonsCost() * -1);
            client().accounts().save(account);
            if (product.isPermanent()) {
                client().positions().createFromPurchase(purchase);
            }
            scoped.commitIfLast();
            this.purchase = purchase; //don't mess up hasBeenBuilt() if a method throws an exception above
        } catch (AccountConflictException e) {
            throw new DatabaseException(e);
        }
    }

    private void makeSurePlayerAccountExists() throws AccountConflictException {
        MutableAccount account = client().accounts().findMutable(playerId);
        if (account.getInitialState().existed()) {
            client().accounts().save(account);
        }
    }

    private Product findProduct() {
        Preconditions.checkState(productId != null, "no product set");
        return client().products().findById(productId);
    }

    private SqlPurchase createPurchase(Product product) {
        return new SqlPurchase(
                purchaseId, playerId, product, Instant.now(),
                data, comment, findMelonsCost(product)
        );
    }

    private int findMelonsCost(Product product) {
        if (melonsCost == PRODUCT_DEFAULT_COST) {
            return product.getMelonsCost();
        } else {
            return melonsCost;
        }
    }

    @Override
    public PurchaseBuilder withProduct(Product product) {
        Preconditions.checkNotNull(product, "product");
        return withProductId(product.getUniqueId());
    }

    @Override
    public PurchaseBuilder withProductId(UUID productId) {
        Preconditions.checkNotNull(productId, "productId");
        checkNotYetExecuted();
        this.productId = productId;
        return this;
    }

    @Override
    public PurchaseBuilder withMelonsCost(int melonsCost) {
        checkNotYetExecuted();
        this.melonsCost = melonsCost;
        return this;
    }

    @Override
    public PurchaseBuilder withData(String data) {
        Preconditions.checkNotNull(data, "data");
        checkNotYetExecuted();
        this.data = data;
        return this;
    }

    @Override
    public PurchaseBuilder withComment(String comment) {
        Preconditions.checkNotNull(comment, "comment");
        checkNotYetExecuted();
        this.comment = comment;
        return this;
    }

    @Override
    public SqlLanatusClient client() {
        return client;
    }

    @Override
    public SingleSql sql() {
        return (SingleSql) client.sql();
    }
}
