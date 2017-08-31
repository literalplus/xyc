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
