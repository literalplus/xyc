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

import li.l1t.lanatus.api.account.AccountSnapshot;
import li.l1t.lanatus.api.account.LanatusAccount;
import li.l1t.lanatus.sql.account.LanatusAccountFactory;

import java.util.UUID;

/**
 * Creates account snapshots by data.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-29
 */
public class AccountSnapshotFactory implements LanatusAccountFactory<SqlAccountSnapshot> {
    @Override
    public SqlAccountSnapshot newInstance(UUID playerId, int melonsCount, String lastRank) {
        return new SqlAccountSnapshot(playerId, melonsCount, lastRank);
    }

    @Override
    public SqlAccountSnapshot defaultInstance(UUID playerId) {
        return new SqlAccountSnapshot(
                playerId, LanatusAccount.INITIAL_MELONS_COUNT, LanatusAccount.DEFAULT_RANK, false
        );
    }

    public AccountSnapshot of(LanatusAccount account) {
        if (account instanceof AccountSnapshot) {
            return (AccountSnapshot) account;
        } else {
            return newInstance(account.getPlayerId(), account.getMelonsCount(), account.getLastRank());
        }
    }
}
