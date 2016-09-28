/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.api.product;

import java.util.UUID;

/**
 * Represents an immutable snapshot of a product that may be bought through Lanatus.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public interface Product {
    /**
     * @return the unique id of this product
     */
    UUID getUniqueId();

    /**
     * @return the Lanatus module handling this product
     */
    String getModule();

    /**
     * @return the internal name of this product
     */
    String getName();

    /**
     * @return the display name of this product
     */
    String getDisplayName();

    /**
     * @return the human-readable description of this product, may contain newlines
     */
    String getDescription();

    /**
     * @return the internal name of the icon to use for this product in Minecraft
     */
    String getIconName();

    /**
     * @return the amount of melons a player has to pay when purchasing this product
     */
    int getMelonsCost();

    /**
     * @return whether this product may be purchased currently
     */
    boolean isActive();
}
