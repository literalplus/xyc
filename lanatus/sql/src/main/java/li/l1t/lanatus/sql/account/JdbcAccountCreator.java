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

package li.l1t.lanatus.sql.account;

import li.l1t.common.sql.sane.util.AbstractJdbcEntityCreator;
import li.l1t.lanatus.api.account.LanatusAccount;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Creates account snapshots from result sets returned by a JDBC database.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-29
 */
class JdbcAccountCreator<T extends LanatusAccount> extends AbstractJdbcEntityCreator<T> {
    private final LanatusAccountFactory<T> factory;

    public JdbcAccountCreator(LanatusAccountFactory<T> factory) {
        this.factory = factory;
    }

    /**
     * Creates a new account object from the data contained in a result set. Note that this uses the
     * <b>current</b> row from the result set, so {@link ResultSet#next()} must have been called
     * already.
     *
     * @param rs the result set to retrieve data from
     * @return the account object for given data
     * @throws SQLException             if a database error occur or the result set is not
     *                                  positioned at a valid row
     * @throws IllegalArgumentException if the player UUID in the result set is in a wrong format
     */
    @Override
    public T createFromCurrentRow(ResultSet rs) throws SQLException {
        return factory.newInstance(
                uuid(rs, "player_uuid"), rs.getInt("melons"), rs.getString("lastrank")
        );
    }

    /**
     * Creates a default account object for a player.
     *
     * @param playerId the unique id of the player
     * @return the created account object
     * @see LanatusAccountFactory#defaultInstance(UUID)
     */
    public T createDefault(UUID playerId) {
        return factory.defaultInstance(playerId);
    }
}
