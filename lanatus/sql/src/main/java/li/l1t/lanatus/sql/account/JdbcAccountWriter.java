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

package li.l1t.lanatus.sql.account;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import li.l1t.common.sql.sane.AbstractSqlConnected;
import li.l1t.common.sql.sane.SaneSql;
import li.l1t.lanatus.api.account.AccountSnapshot;
import li.l1t.lanatus.api.account.MutableAccount;
import li.l1t.lanatus.api.exception.AccountConflictException;

import java.time.Instant;
import java.util.Optional;
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

    void write(MutableAccount account) throws AccountConflictException {
        Preconditions.checkNotNull(account, "account");
        Optional<AccountSnapshot> currentState = snapshotFetcher.fetchOptionally(account.getPlayerId());
        if (!currentState.isPresent()) {
            createNewAccount(account);
        } else if (localCopyIsDifferentFromRemote(account, currentState.get())) {
            attemptUpdate(account, currentState.get());
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
