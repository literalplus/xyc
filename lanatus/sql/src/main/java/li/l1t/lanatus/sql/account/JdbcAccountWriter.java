/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.lanatus.sql.account;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import li.l1t.common.sql.sane.AbstractSqlConnected;
import li.l1t.common.sql.sane.SaneSql;
import li.l1t.lanatus.api.account.AccountSnapshot;
import li.l1t.lanatus.api.account.MutableAccount;
import li.l1t.lanatus.api.exception.AccountConflictException;

import java.time.Instant;
import java.util.UUID;

/**
 * Writes the state of a mutable account to database, attempting basic conflict resolution if the
 * state changed in database.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-10-07
 */
class JdbcAccountWriter extends AbstractSqlConnected {
    private final JdbcAccountFetcher<AccountSnapshot> snapshotFetcher;

    JdbcAccountWriter(SaneSql sql, JdbcAccountFetcher<AccountSnapshot> snapshotFetcher) {
        super(sql);
        this.snapshotFetcher = snapshotFetcher;
    }

    public void write(MutableAccount account) throws AccountConflictException {
        Preconditions.checkNotNull(account, "account");
        AccountSnapshot currentSnapshot = snapshotFetcher.fetchSingle(account.getPlayerId());
        attemptWrite(account, currentSnapshot);
    }

    private void attemptWrite(MutableAccount account, AccountSnapshot currentSnapshot) throws AccountConflictException {
        if (currentSnapshot.isDefault()) {
            createNewAccount(account);
        } else if (localCopyIsDifferentFromRemote(account, currentSnapshot)) {
            attemptUpdate(account, currentSnapshot);
        } //no changes otherwise
    }

    private void createNewAccount(MutableAccount account) {
        int rowsAffected = insertRaw(
                account.getPlayerId(), account.getMelonsCount(), account.getLastRank()
        );
        Verify.verify(rowsAffected == 1, "expected insert to affect single row, was: ", account, rowsAffected);
    }

    private int insertRaw(UUID playerId, int melonsCount, String lastRank) {
        return sql().updateRaw("INSERT INTO " + SqlAccountRepository.TABLE_NAME + " " +
                        "SET player_uuid=?, created=?, melons=?, lastrank=?",
                playerId.toString(), Instant.now(), melonsCount, lastRank
        );
    }

    private boolean localCopyIsDifferentFromRemote(MutableAccount account, AccountSnapshot currentSnapshot) {
        return !currentSnapshot.equals(account);
    }

    private void attemptUpdate(MutableAccount account, AccountSnapshot currentSnapshot) throws AccountConflictException {
        if (lastRankChangedConcurrentlyInDatabase(account, currentSnapshot)) {
            throw new AccountConflictException(currentSnapshot, account);
        } else {
            updateUsingMelonsDifference(account);
        }
    }

    private boolean lastRankChangedConcurrentlyInDatabase(MutableAccount account, AccountSnapshot currentSnapshot) {
        return !currentSnapshot.getLastRank().equals(account.getInitialState().getLastRank());
    }

    private void updateUsingMelonsDifference(MutableAccount account) {
        int rowsAffected = updateRaw(
                account.getPlayerId(), account.getLastRank(),
                findMelonDifference(account)
        );
        Verify.verify(rowsAffected == 1, "did not update single row when writing account", account, rowsAffected);
    }

    private int updateRaw(UUID playerId, String lastRank, int melonsDiff) {
        return sql().updateRaw("UPDATE " + SqlAccountRepository.TABLE_NAME + " " +
                        "SET melons=melons+?, lastrank=?" +
                        "WHERE player_uuid=?",
                melonsDiff, lastRank, playerId.toString()
        );
    }

    private int findMelonDifference(MutableAccount account) {
        return account.getMelonsCount() - account.getInitialState().getMelonsCount();
    }
}
