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

import li.l1t.common.collections.cache.OptionalCache;
import li.l1t.common.collections.cache.OptionalGuavaCache;
import li.l1t.lanatus.api.account.AccountRepository;
import li.l1t.lanatus.api.account.AccountSnapshot;
import li.l1t.lanatus.api.account.MutableAccount;
import li.l1t.lanatus.api.exception.AccountConflictException;
import li.l1t.lanatus.sql.AbstractSqlLanatusRepository;
import li.l1t.lanatus.sql.SqlLanatusClient;
import li.l1t.lanatus.sql.account.mutable.MutableAccountFactory;
import li.l1t.lanatus.sql.account.snapshot.AccountSnapshotFactory;

import java.util.Optional;
import java.util.UUID;

/**
 * An account repository that uses SQL as a backend.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-29
 */
public class SqlAccountRepository extends AbstractSqlLanatusRepository implements AccountRepository {
    public static final String TABLE_NAME = "mt_main.lanatus_player";
    private final AccountSnapshotFactory snapshotFactory = new AccountSnapshotFactory();
    private final JdbcAccountFetcher<AccountSnapshot> snapshotFetcher = new JdbcAccountFetcher<>(
            new JdbcAccountCreator<>(snapshotFactory),
            client().sql()
    );
    private final OptionalCache<UUID, AccountSnapshot> snapshotCache = new OptionalGuavaCache<>();
    private final JdbcAccountFetcher<MutableAccount> mutableFetcher = new JdbcAccountFetcher<>(
            new JdbcAccountCreator<>(new MutableAccountFactory()),
            client().sql()
    );
    private final JdbcAccountWriter accountWriter = new JdbcAccountWriter(client().sql(), snapshotFetcher);

    public SqlAccountRepository(SqlLanatusClient client) {
        super(client);
    }

    @Override
    public Optional<AccountSnapshot> find(UUID playerId) {
        return getOrFetchSnapshot(playerId);
    }

    @Override
    public AccountSnapshot findOrDefault(UUID playerId) {
        return getOrFetchSnapshot(playerId)
                .orElseGet(() -> snapshotFactory.defaultInstance(playerId));
    }

    private Optional<AccountSnapshot> getOrFetchSnapshot(UUID playerId) {
        return snapshotCache.getOrCompute(playerId, snapshotFetcher::fetchOptionally);
    }

    @Override
    public AccountSnapshot refresh(AccountSnapshot account) {
        UUID playerId = account.getPlayerId();
        snapshotCache.invalidateKey(playerId);
        return findOrDefault(playerId);
    }

    @Override
    public MutableAccount findMutable(UUID playerId) {
        return mutableFetcher.fetchOrDefault(playerId);
    }

    @Override
    public void save(MutableAccount localCopy) throws AccountConflictException {
        accountWriter.write(localCopy);
        snapshotCache.invalidateKey(localCopy.getPlayerId());
    }

    @Override
    public void clearCache() {
        snapshotCache.clear();
    }

    @Override
    public void clearCachesFor(UUID playerId) {
        snapshotCache.invalidateKey(playerId);
    }
}
