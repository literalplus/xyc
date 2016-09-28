/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.lanatus.api.account;

import java.time.Instant;
import java.util.UUID;

/**
 * An immutable snapshot of the state of a Lanatus account.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public interface AccountSnapshot {
    /**
     * @return the instant at which this snapshot was taken
     */
    Instant getSnapshotInstant();

    /**
     * @return the unique id of the player the account belongs to
     */
    UUID getPlayerId();

    /**
     * @return the amount of melons in the account at the time this snapshot was taken
     */
    int getMelonsCount();

    /**
     * @return the most recently known rank of the account at the time this snapshot was taken
     */
    String getLastRank();
}
