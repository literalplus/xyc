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

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * A simple implementation of an update result.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
public class SimpleUpdateResult extends AbstractCloseableResult implements UpdateResult {
    private final ResultSet generatedKeys;
    private final int affectedRowCount;

    public SimpleUpdateResult(@Nonnull PreparedStatement statement, @Nonnull ResultSet generatedKeys,
                              int affectedRowCount) {
        super(statement);
        this.generatedKeys = Preconditions.checkNotNull(generatedKeys, "generatedKeys");
        this.affectedRowCount = affectedRowCount;
    }

    @Override
    public void close() {
        Closer.close(generatedKeys);
        super.close();
    }

    @Override
    public ResultSet gk() {
        return generatedKeys;
    }

    @Override
    public int getAffectedRowCount() {
        return affectedRowCount;
    }
}
