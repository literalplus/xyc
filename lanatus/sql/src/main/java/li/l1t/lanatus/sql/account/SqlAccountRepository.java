/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql.account;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import li.l1t.common.exception.InternalException;
import li.l1t.lanatus.api.account.AccountRepository;
import li.l1t.lanatus.api.account.AccountSnapshot;
import li.l1t.lanatus.api.account.MutableAccount;
import li.l1t.lanatus.api.exception.AccountConflictException;
import li.l1t.lanatus.sql.AbstractSqlLanatusRepository;
import li.l1t.lanatus.sql.SqlLanatusClient;
import li.l1t.lanatus.sql.account.snapshot.AccountSnapshotFactory;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * An account repository that uses SQL as a backend.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-29
 */
public class SqlAccountRepository extends AbstractSqlLanatusRepository implements AccountRepository {
    private final JdbcAccountFetcher<AccountSnapshot> snapshotFetcher = new JdbcAccountFetcher<>(
            new JdbcAccountCreator<>(new AccountSnapshotFactory()),
            client().getSql()
    );
    private final Cache<UUID, AccountSnapshot> snapshotCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public SqlAccountRepository(SqlLanatusClient client) {
        super(client);
    }

    @Override
    public AccountSnapshot find(UUID playerId) {
        try {
            return snapshotCache.get(playerId, () -> fetchSnapshot(playerId));
        } catch (ExecutionException e) {
            throw InternalException.wrap(e, "checked exception fetching an account snapshot?"); //this should not happen, Google pls
        }
    }

    @Override
    public AccountSnapshot refresh(AccountSnapshot account) {
        return fetchSnapshot(account.getPlayerId());
    }

    private AccountSnapshot fetchSnapshot(UUID playerId) {
        try {
            return snapshotFetcher.fetchSingle(playerId);
        } catch (SQLException e) {
            throw InternalException.wrap(e);
        }
    }

    @Override
    public MutableAccount findMutable(UUID playerId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(MutableAccount localCopy) throws AccountConflictException {
        throw new UnsupportedOperationException();
    }
}
