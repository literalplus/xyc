/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.sql.sane.result;

import com.google.common.base.Preconditions;
import li.l1t.common.util.Closer;

import java.sql.PreparedStatement;

/**
 * Abstract base class for closeable results.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
abstract class AbstractCloseableResult implements CloseableResult {
    private final PreparedStatement statement;

    AbstractCloseableResult(PreparedStatement statement) {
        this.statement = Preconditions.checkNotNull(statement, "statement");
    }

    @Override
    public void close() throws Exception {
        statement.close();
    }

    @Override
    public void tryClose() {
        Closer.close(this);
    }

    @Override
    public PreparedStatement getStatement() {
        return statement;
    }
}
