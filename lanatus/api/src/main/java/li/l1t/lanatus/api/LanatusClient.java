/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.api;

import li.l1t.lanatus.api.account.AccountRepository;
import li.l1t.lanatus.api.builder.melons.CreditMelonsBuilder;
import li.l1t.lanatus.api.builder.purchase.PurchaseBuilder;
import li.l1t.lanatus.api.position.PositionRepository;
import li.l1t.lanatus.api.product.ProductRepository;
import li.l1t.lanatus.api.purchase.PurchaseRepository;

import java.util.UUID;

/**
 * Represents a client implementation for the Lanatus API.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public interface LanatusClient {
    /**
     * @return the unique name of the module this client is for, must not be longer than 20
     * characters
     */
    String getModuleName();

    /**
     * @return the account repository associated with this client
     */
    AccountRepository accounts();

    /**
     * @return the position repository associated with this client
     */
    PositionRepository positions();

    /**
     * @return the purchase repository associated with this client
     */
    PurchaseRepository purchases();

    /**
     * @return the product repository associated with this client
     */
    ProductRepository products();

    /**
     * Starts a new purchase builder for given player.
     *
     * @param playerId the unique id of the player on whose behalf the purchase is to be made
     * @return a new purchase builder
     */
    PurchaseBuilder startPurchase(UUID playerId);

    /**
     * Creates a new builder that credits melons to given player.
     *
     * @param playerId the unique id of the player whose account should be credited
     * @return a new purchase builder
     */
    CreditMelonsBuilder creditMelons(UUID playerId);
}
