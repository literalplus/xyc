/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.api.position;


import li.l1t.lanatus.api.LanatusRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * A repository for positions.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public interface PositionRepository extends LanatusRepository {
    /**
     * Finds the position belonging to a purchase, if any.
     *
     * @param purchaseId the purchase to search for
     * @return an Optional containing the associated purchase if it exists, otherwise an empty
     * Optional
     */
    Optional<Position> findByPurchase(UUID purchaseId);

    /**
     * Finds all positions currently associated with given player.
     *
     * @param playerId the unique id of the player
     * @return the collection of positions, or an empty collection if none
     */
    Collection<Position> findAllByPlayer(UUID playerId);
}
