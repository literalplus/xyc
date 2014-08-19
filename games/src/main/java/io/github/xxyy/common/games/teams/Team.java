/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without permission from the
 *  original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.games.teams;

import org.bukkit.entity.Player;

import io.github.xxyy.lib.intellij_annotations.NotNull;

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
    Player getPlayer(@NotNull String plrName);

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
    boolean hasPlayer(@NotNull String plrName);

    /**
     * Removes a {@link org.bukkit.entity.Player} instance from this team, regardless if its in the team or not.
     *
     * @param plr {@link org.bukkit.entity.Player} to remove
     */
    void removePlayer(@NotNull Player plr);

    /**
     * Tries to remove a {@link org.bukkit.entity.Player} from this team by its name.
     *
     * @param plrName Name of the player to remove
     * @return A {@link org.bukkit.entity.Player} object if there was such player, or {@code null} otherwise.
     */
    Player removePlayer(@NotNull String plrName);

    /**
     * Adds a {@link org.bukkit.entity.Player} to this team.
     *
     * @param plr {@link org.bukkit.entity.Player} to add
     * @return {@code true} if the player list changed as a result of this call.
     */
    boolean addPlayer(Player plr);

    String getName();
}
