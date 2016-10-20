/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.lanatus.sql;

import li.l1t.common.sql.sane.SingleSql;
import li.l1t.common.sql.sane.SqlConnected;
import li.l1t.lanatus.api.LanatusConnected;
import li.l1t.lanatus.api.account.MutableAccount;
import li.l1t.lanatus.api.exception.AccountConflictException;

import java.util.UUID;

/**
 * Abstract base class for Lanatus-SQL tests providing commonly used methods.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-20
 */
public abstract class AbstractLanatusSqlTest implements LanatusConnected, SqlConnected {
    private SqlLanatusClient client = createClient();

    protected static SqlLanatusClient createClient() {
        return new SqlLanatusClient(LanatusSqlTestSuite.SETUP.sql(), "xyc-it");
    }

    @Override
    public SingleSql sql() {
        return LanatusSqlTestSuite.SETUP.sql();
    }

    @Override
    public SqlLanatusClient client() {
        return client;
    }

    protected UUID givenAPlayerWithMelons(int melonsCount) throws AccountConflictException {
        UUID playerId = UUID.randomUUID();
        MutableAccount mutable = client().accounts().findMutable(playerId);
        mutable.setMelonsCount(melonsCount);
        client().accounts().save(mutable);
        return playerId;
    }
}
