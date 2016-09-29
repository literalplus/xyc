/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql.account;

import com.google.common.collect.ImmutableList;
import li.l1t.lanatus.api.account.LanatusAccount;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

/**
 * Creates account snapshots from result sets returned by a JDBC database.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-29
 */
class JdbcAccountCreator<T extends LanatusAccount> {
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
    public T createFromCurrentRow(ResultSet rs) throws SQLException {
        return factory.newInstance(
                UUID.fromString(rs.getString("player_uuid")),
                rs.getInt("melons"),
                rs.getString("lastrank")
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

    /**
     * Creates a collection of account objects from a result set. Note that the result set must be
     * <b>before</b> the first row when calling this method, i.e. {@link ResultSet#next()} must not
     * have been called.
     *
     * @param rs the result set to retrieve data from
     * @return the immutable collection of accounts created from the rows of the result set, or an
     * empty immutable collection of the result set is empty
     * @throws SQLException
     */
    public Collection<T> createCollectionFromResultSet(ResultSet rs) throws SQLException {
        ImmutableList.Builder<T> builder = new ImmutableList.Builder<>();
        while (rs.next()) {
            builder.add(createFromCurrentRow(rs));
        }
        return builder.build();
    }
}
