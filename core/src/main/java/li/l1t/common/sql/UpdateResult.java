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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents the result of a SQL UPDATE statement.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.8.14
 */
public class UpdateResult implements AutoCloseable {
    private final int affectedRows;
    private ResultSet generatedKeys;

    public UpdateResult(int affectedRows, ResultSet generatedKeys) {
        this.affectedRows = affectedRows;
        this.generatedKeys = generatedKeys;
    }

    public ResultSet getGeneratedKeys() {
        return generatedKeys;
    }

    /**
     * @return {@link #getGeneratedKeys()}
     */
    public ResultSet gk() {
        return getGeneratedKeys();
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    public void vouchForGeneratedKeys() throws SQLException {
        if (generatedKeys == null) {
            throw new SQLException("No generated keys!");
        }
    }

    @Override
    public void close() throws SQLException {
        generatedKeys.close();
    }

    public boolean tryClose() {
        try {
            close();
        } catch (SQLException e) {
            return false;
        }

        return true;
    }
}
