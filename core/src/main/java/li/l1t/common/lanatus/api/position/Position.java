/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.lanatus.api.position;

import li.l1t.common.lanatus.api.product.Product;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a single item actually owned by a player that was purchased through Lanatus. This
 * usually corresponds to a persistent premium feature, since one-time bonuses are not assigned
 * positions.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public interface Position {
    /**
     * @return the unique id of the purchase this position corresponds to
     */
    UUID getPurchaseId();

    /**
     * @return the unique id of the player owning this position
     */
    UUID getPlayerId();

    /**
     * @return the product this position corresponds to
     */
    Product getProduct();

    /**
     * @return the instant this position was purchased at
     */
    Instant getPurchaseInstant();

    /**
     * @return the arbitrary data describing specifics of this position to its module
     */
    String getData();
}
