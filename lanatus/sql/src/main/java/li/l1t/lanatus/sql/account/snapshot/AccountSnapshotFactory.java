/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
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
