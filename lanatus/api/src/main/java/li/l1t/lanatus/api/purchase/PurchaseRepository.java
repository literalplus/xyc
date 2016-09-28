/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.api.purchase;

import li.l1t.lanatus.api.exception.NoSuchRowException;

import java.util.Collection;
import java.util.UUID;

/**
 * A repository for purchases made through Lanatus.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public interface PurchaseRepository {
    /**
     * Finds a purchase by its unique id.
     *
     * @param purchaseId the unique id of the purchase
     * @return the purchase with given unique id
     * @throws NoSuchRowException if there is no such purchase
     */
    Purchase findById(UUID purchaseId) throws NoSuchRowException;

    /**
     * Finds the collection of purchases a player has made.
     *
     * @param playerId the unique id of the player
     * @return the collection of purchases, or an empty collection for none
     */
    Collection<Purchase> findByPlayer(UUID playerId);
}
