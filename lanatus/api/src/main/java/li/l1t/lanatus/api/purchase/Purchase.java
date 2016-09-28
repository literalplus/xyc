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

import li.l1t.lanatus.api.position.Position;
import li.l1t.lanatus.api.product.Product;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a purchase made through Lanatus. Note that purchases need not correspond to an actual
 * {@link Position}. This may be the case for one-time bonuses as well as for purchases which have
 * been undone.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public interface Purchase {
    /**
     * @return the unique id of this purchase
     */
    UUID getUniqueId();

    /**
     * @return the unique id of the player whose account the purchase applies to
     */
    UUID getPlayerId();

    /**
     * @return the product associated with this purchase
     */
    Product getProduct();

    /**
     * @return the instant this purchase was made at
     */
    Instant getCreationInstant();

    /**
     * @return the instant this purchase was last changed at
     */
    Instant getUpdateInstant();

    /**
     * @return the arbitrary string describing specifics of the product to its module
     */
    String getData();

    /**
     * @return the arbitrary string attached to this purchase for reference
     */
    String getComment();

    /**
     * @return the amount of melons that was actually spent on this purchase, may be negative if
     * melons were given to the associated player
     */
    int getMelonsCost();
}
