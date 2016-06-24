/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
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
