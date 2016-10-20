/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.api.account;

import java.util.UUID;

/**
 * Represents an account in Lanatus. Implementations must specify mutability behaviour.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-29
 */
public interface LanatusAccount {
    /**
     * The name of the default rank that is assigned to accounts at creation time.
     */
    String DEFAULT_RANK = "default";

    /**
     * The amount of melons accounts have at creation time.
     */
    int INITIAL_MELONS_COUNT = 0;

    /**
     * @return the unique id of the player the account belongs to
     */
    UUID getPlayerId();

    /**
     * @return the amount of melons in this account
     */
    int getMelonsCount();

    /**
     * @return the most recently known rank of this account
     */
    String getLastRank();
}
