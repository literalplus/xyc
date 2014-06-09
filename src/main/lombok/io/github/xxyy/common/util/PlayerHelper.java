package io.github.xxyy.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Helps dealing with players!
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 10.6.14
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerHelper {

    /**
     * Checks if a player by that name is online on the server, by matching all online players' names against the given name.
     * @param playerName Name of the player to check
     * @return Whether a player by that name is online on the server.
     */
    public static boolean isOnline(String playerName) {
        return matchPlayers((plr) -> plr.getName().equals(playerName));
    }

    /**
     * Checks if a player by that name is online on the server, by matching all online players' names against the given name.
     * This method ignores casing.
     * @param playerName Name of the player to check
     * @return Whether a player by that name is online on the server.
     */
    public static boolean isOnlineIgnoreCase(String playerName) {
        return matchPlayers((plr) -> plr.getName().equalsIgnoreCase(playerName));
    }

    /**
     * Matches a predicate on all online players.
     * @param predicate Predicate to use
     * @return Whether any player on the server matches the given Predicate.
     */
    public static boolean matchPlayers(Predicate<? super Player> predicate) {
        return Arrays.asList(Bukkit.getOnlinePlayers()).parallelStream().anyMatch(predicate);
    }
}
