/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql.account;

import li.l1t.lanatus.api.account.LanatusAccount;

import java.util.UUID;

/**
 * Factory for LanatusAccount subclasses.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-29
 */
public interface LanatusAccountFactory<T extends LanatusAccount> {

    T newInstance(UUID playerId, int melonsCount, String lastRank);

    /**
     * Creates a new instance of an account with the default values provided in the {@link
     * LanatusAccount} interface.
     *
     * @param playerId the unique id of the player to instantiate a default account for
     * @return the created account object
     */
    default T defaultInstance(UUID playerId) {
        return newInstance(playerId, LanatusAccount.INITIAL_MELONS_COUNT, LanatusAccount.DEFAULT_RANK);
    }
}
