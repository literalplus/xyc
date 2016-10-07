/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql.account.mutable.snapshot;

import li.l1t.lanatus.api.account.AccountSnapshot;
import li.l1t.lanatus.api.account.MutableAccount;
import li.l1t.lanatus.sql.account.LanatusAccountFactory;
import li.l1t.lanatus.sql.account.snapshot.AccountSnapshotFactory;

import java.util.UUID;

/**
 * Creates account snapshots by data.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-29
 */
public class MutableAccountFactory implements LanatusAccountFactory<MutableAccount> {
    private final AccountSnapshotFactory snapshotFactory = new AccountSnapshotFactory();

    @Override
    public MutableAccount newInstance(UUID playerId, int melonsCount, String lastRank) {
        AccountSnapshot snapshot = snapshotFactory.newInstance(playerId, melonsCount, lastRank);
        return new MutableSqlAccount(snapshot);
    }
}
