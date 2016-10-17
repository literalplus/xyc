/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.api.builder.purchase;

import li.l1t.common.exception.DatabaseException;
import li.l1t.lanatus.api.exception.NoSuchProductException;
import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.api.purchase.Purchase;

import java.util.UUID;

/**
 * Fluent builder interface for purchases, allowing to purchase products on behalf of a player.
 * Instances cannot be reused, and attempting to change purchase data after executing it will throw
 * {@link IllegalStateException}.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-17
 */
public interface PurchaseBuilder {
    /**
     * @return the generated unique id for the purchase to be created from this builder
     */
    UUID getPurchaseId();

    /**
     * @return the executed purchase made from this builder
     * @throws IllegalStateException if the purchase has not yet been executed
     */
    Purchase getPurchase();

    /**
     * @return whether the purchase built by this builder has been executed
     */
    boolean hasBeenExecuted();

    /**
     * Executes the purchase defined by the current state of this builder and saves its result to
     * the database. Note that this method can only be executed once per builder instance. The
     * created purchase may be obtained using {@link #getPurchase()}.
     *
     * @throws IllegalStateException  {@linkplain #hasBeenExecuted() if the purchase has already
     *                                been built}
     * @throws NoSuchProductException if the product set on this builder does not exist or no
     *                                product has been set
     * @throws DatabaseException      if a database error occurs
     */
    void build() throws IllegalStateException, NoSuchProductException, DatabaseException;

    /**
     * @param product the product to purchase, may not be null
     * @return this builder
     * @throws IllegalStateException if the purchase has already been built
     */
    PurchaseBuilder withProduct(Product product);

    /**
     * @param productId the unique id of the product to purchase, may not be null
     * @return this builder
     * @throws IllegalStateException if the purchase has already been built
     */
    PurchaseBuilder withProductId(UUID productId);

    /**
     * Sets the melons cost of this builder, overriding the default cost of the product. Setting the
     * cost to {@link Integer#MIN_VALUE} (the default) causes the product's default cost to be
     * used.
     *
     * @param melonsCost the melons cost of the purchase, may be negative or zero
     * @return this builder
     * @throws IllegalStateException if the purchase has already been built
     */
    PurchaseBuilder withMelonsCost(int melonsCost);

    /**
     * @param data the arbitrary string describing specifics of the product to its module
     * @return this builder
     * @throws IllegalStateException if the purchase has already been built
     */
    PurchaseBuilder withData(String data);

    /**
     * Attaches an arbitrary string to the purchase for internal reference.
     *
     * @param comment the purchase comment
     * @return this builder
     * @throws IllegalStateException if the purchase has already been built
     */
    PurchaseBuilder withComment(String comment);
}
