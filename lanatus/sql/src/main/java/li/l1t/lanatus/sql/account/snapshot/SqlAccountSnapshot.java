/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql.account.snapshot;

import com.google.common.base.Preconditions;
import li.l1t.lanatus.api.account.AccountSnapshot;
import li.l1t.lanatus.api.account.LanatusAccount;

import java.time.Instant;
import java.util.UUID;

/**
 * Implementation of an immutable account.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-29
 */
class SqlAccountSnapshot implements AccountSnapshot {
    private final Instant snapshotInstant = Instant.now();
    private final UUID playerId;
    private final int melonsCount;
    private final String lastRank;

    public SqlAccountSnapshot(UUID playerId, int melonsCount, String lastRank) {
        Preconditions.checkNotNull(playerId, "playerId");
        this.playerId = playerId;
        this.melonsCount = melonsCount;
        this.lastRank = lastRank == null ? LanatusAccount.DEFAULT_RANK : lastRank;
    }

    @Override
    public Instant getSnapshotInstant() {
        return snapshotInstant;
    }

    @Override
    public UUID getPlayerId() {
        return playerId;
    }

    @Override
    public int getMelonsCount() {
        return melonsCount;
    }

    @Override
    public String getLastRank() {
        return lastRank;
    }

    @Override
    public String toString() {
        return "SqlAccountSnapshot{" +
                playerId +
                " was '" + lastRank +
                "' with " + melonsCount +
                " melons @[" + snapshotInstant +
                "]}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SqlAccountSnapshot that = (SqlAccountSnapshot) o;
        return melonsCount == that.melonsCount &&
                playerId.equals(that.playerId) &&
                lastRank.equals(that.lastRank);
    }

    @Override
    public int hashCode() {
        int result = playerId.hashCode();
        result = 31 * result + melonsCount;
        result = 31 * result + lastRank.hashCode();
        return result;
    }
}
