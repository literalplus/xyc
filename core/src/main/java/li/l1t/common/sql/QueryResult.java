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

package li.l1t.common.sql;

import li.l1t.common.util.Closer;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Stores a {@link java.sql.PreparedStatement} and {@link java.sql.ResultSet} for an already
 * executed SQL query.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 12/02/14
 */
public class QueryResult implements AutoCloseable {
    @Nullable
    private PreparedStatement preparedStatement;
    @Nullable
    private ResultSet resultSet;
    /**
     * Return value of {@link java.sql.PreparedStatement#executeUpdate()}. Set to -1 if the
     * statement was a query.
     */
    private int updateReturn;

    public QueryResult(PreparedStatement statement, int updateReturn) {
        this(statement, null, updateReturn);
    }

    public QueryResult(PreparedStatement statement, ResultSet rs) {
        this(statement, rs, -1);
    }

    public QueryResult(@Nullable PreparedStatement preparedStatement, @Nullable ResultSet resultSet, int updateReturn) {
        this.preparedStatement = preparedStatement;
        this.resultSet = resultSet;
        this.updateReturn = updateReturn;
    }

    public ResultSet rs() {
        return resultSet;
    }

    public QueryResult assertHasResultSet() {
        if (resultSet == null) {
            throw new IllegalStateException("QueryResult does not have ResultSet when required!");
        }

        return this;
    }

    public QueryResult vouchForResultSet() throws SQLException {
        if (resultSet == null) {
            throw new SQLException("No ResultSet associated with QueryResult when asked to vouch!");
        }

        return this;
    }

    @Override
    public void close() {
        if (preparedStatement == null) {
            if (resultSet != null) {
                Closer.close(resultSet);
                resultSet = null;
            }
            return;
        }
        Closer.close(preparedStatement);
        preparedStatement = null;
        resultSet = null;
    }

    public void tryClose() {
        try {
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public PreparedStatement getStatement() {
        return this.preparedStatement;
    }

    @Nullable
    public ResultSet getResultSet() {
        return this.resultSet;
    }

    public int getUpdateReturn() {
        return this.updateReturn;
    }
}
