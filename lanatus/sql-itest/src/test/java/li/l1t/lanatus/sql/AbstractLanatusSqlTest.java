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

package li.l1t.lanatus.sql;

import li.l1t.common.sql.sane.SingleSql;
import li.l1t.common.sql.sane.SqlConnected;
import li.l1t.lanatus.api.LanatusConnected;
import li.l1t.lanatus.api.account.AccountSnapshot;
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

    protected AccountSnapshot findAccount(UUID playerId) {
        return client().accounts().find(playerId).orElseThrow(AssertionError::new);
    }
}
