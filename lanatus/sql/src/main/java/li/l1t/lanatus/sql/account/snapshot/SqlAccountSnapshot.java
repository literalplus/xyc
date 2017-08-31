/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
public class SqlAccountSnapshot implements AccountSnapshot {
    private final Instant snapshotInstant = Instant.now();
    private final UUID playerId;
    private final int melonsCount;
    private final String lastRank;
    private final boolean existed;

    SqlAccountSnapshot(UUID playerId, int melonsCount, String lastRank) {
        this(playerId, melonsCount, lastRank, true);
    }

    SqlAccountSnapshot(UUID playerId, int melonsCount, String lastRank, boolean existed) {
        this.existed = existed;
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
    public boolean existed() {
        return existed;
    }

    @Override
    public String toString() {
        return "SqlAccountSnapshot{" +
                (existed ? "[new] " : "") +
                playerId +
                " was '" + lastRank +
                "' with " + melonsCount +
                " melons @[" + snapshotInstant +
                "]}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof LanatusAccount)) return false;
        LanatusAccount that = (LanatusAccount) o;
        return melonsCount == that.getMelonsCount() &&
                playerId.equals(that.getPlayerId()) &&
                lastRank.equals(that.getLastRank());
    }

    @Override
    public int hashCode() {
        int result = playerId.hashCode();
        result = 31 * result + melonsCount;
        result = 31 * result + lastRank.hashCode();
        return result;
    }
}
