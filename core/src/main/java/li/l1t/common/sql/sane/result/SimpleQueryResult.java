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

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * A simple implementation of a query result.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
public class SimpleQueryResult extends AbstractCloseableResult implements QueryResult {
    private final ResultSet resultSet;

    public SimpleQueryResult(@Nonnull PreparedStatement statement, @Nonnull ResultSet resultSet) {
        super(statement);
        this.resultSet = Preconditions.checkNotNull(resultSet, "resultSet");
    }

    @Override
    public ResultSet rs() {
        return resultSet;
    }

    @Override
    public void close() {
        super.close(); //closing a prepared statement also closes its result set, so nothing to do here
    }
}
