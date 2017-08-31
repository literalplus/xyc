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

package li.l1t.common.games.teams;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Represents a team of players.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 05/03/14
 */
public interface Team {
    Collection<Player> getPlayers();

    void clearPlayers();

    /**
     * Tries to get a player from this team.
     *
     * @param plrName Name of the player to get
     * @return A {@link org.bukkit.entity.Player} instance if a player by the name {@code plrName} is in this team or {@code null} if there is no such player.
     */
    Player getPlayer(@Nonnull String plrName);

    /**
     * Checks whether this team contains a specific player.
     *
     * @param plr Player to look up
     * @return Whether this team has such player.
     */
    boolean hasPlayer(Player plr);

    /**
     * Checks whether this team contains a player by its name.
     * Internally calls {@link #getPlayer(String)}.
     *
     * @param plrName Name of the player to get
     * @return Whether this team contains a player by the name of {@code plrName}
     */
    boolean hasPlayer(@Nonnull String plrName);

    /**
     * Removes a {@link org.bukkit.entity.Player} instance from this team, regardless if its in the team or not.
     *
     * @param plr {@link org.bukkit.entity.Player} to remove
     */
    void removePlayer(@Nonnull Player plr);

    /**
     * Tries to remove a {@link org.bukkit.entity.Player} from this team by its name.
     *
     * @param plrName Name of the player to remove
     * @return A {@link org.bukkit.entity.Player} object if there was such player, or {@code null} otherwise.
     */
    Player removePlayer(@Nonnull String plrName);

    /**
     * Adds a {@link org.bukkit.entity.Player} to this team.
     *
     * @param plr {@link org.bukkit.entity.Player} to add
     * @return {@code true} if the player list changed as a result of this call.
     */
    boolean addPlayer(Player plr);

    String getName();
}
