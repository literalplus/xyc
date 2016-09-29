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
public class AccountSnapshotFactory implements LanatusAccountFactory<AccountSnapshot> {
    @Override
    public AccountSnapshot newInstance(UUID playerId, int melonsCount, String lastRank) {
        return new SqlAccountSnapshot(playerId, melonsCount, lastRank);
    }

    @Override
    public AccountSnapshot defaultInstance(UUID playerId) {
        return newInstance(playerId, LanatusAccount.INITIAL_MELONS_COUNT, LanatusAccount.DEFAULT_RANK);
    }
}
