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
