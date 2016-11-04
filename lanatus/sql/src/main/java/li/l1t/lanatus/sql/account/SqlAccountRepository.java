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
