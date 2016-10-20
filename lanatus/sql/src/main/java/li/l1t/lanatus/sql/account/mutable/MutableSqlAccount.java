/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.lanatus.sql.account.mutable;

import com.google.common.base.Preconditions;
import li.l1t.lanatus.api.account.AccountSnapshot;
import li.l1t.lanatus.api.account.MutableAccount;
import li.l1t.lanatus.api.exception.NotEnoughMelonsException;

import java.util.UUID;

/**
 * Implementation of a mutable account backed by a SQL datastore. Note that this implementation is
 * explicitly not thread-safe.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-29
 */
class MutableSqlAccount implements MutableAccount {
    private final AccountSnapshot initialState;
    private int melonsCount;
    private String lastRank;

    MutableSqlAccount(AccountSnapshot initialState) {
        this.initialState = Preconditions.checkNotNull(initialState, "initialState");
        this.melonsCount = initialState.getMelonsCount();
        this.lastRank = initialState.getLastRank();
    }

    @Override
    public AccountSnapshot getInitialState() {
        return initialState;
    }

    @Override
    public UUID getPlayerId() {
        return getInitialState().getPlayerId();
    }

    @Override
    public int getMelonsCount() {
        return melonsCount;
    }

    @Override
    public void setMelonsCount(int melonsCount) {
        if (melonsCount < 0) {
            throw new NotEnoughMelonsException(this, Math.abs(melonsCount));
        }
        this.melonsCount = melonsCount;
    }

    @Override
    public void modifyMelonsCount(int melonsModifier) {
        setMelonsCount(this.melonsCount + melonsModifier);
    }

    @Override
    public String getLastRank() {
        return lastRank;
    }

    @Override
    public void setLastRank(String lastRank) {
        Preconditions.checkNotNull(lastRank, "lastRank");
        this.lastRank = lastRank;
    }

    @Override
    public String toString() {
        return "MutableSqlAccount{" +
                initialState.getPlayerId() +
                " - rank '" + lastRank +
                "' with " + melonsCount +
                " melons @[" + initialState +
                "]}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableSqlAccount that = (MutableSqlAccount) o;
        return initialState.equals(that.initialState);
    }

    @Override
    public int hashCode() {
        return initialState.hashCode();
    }
}
