package io.github.xxyy.common.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Map;
import java.util.UUID;

/**
 * Provides some static utility methods for dealing with Scoreboards.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 9.4.14
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScoreboardHelper {

    /**
     * Sets a fake score with a specified display name. Does not call {@link org.bukkit.Bukkit#getOfflinePlayer(String)}, but creates
     * a fake {@link io.github.xxyy.common.util.ScoreboardHelper.NamedOfflinePlayer} to save performance.
     * @param objective Objective to set
     * @param displayName Name to display
     * @param score Score to set
     */
    public static void setFakeScore(final Objective objective, final String displayName, final int score) {
        Score fakeScr = objective.getScore(new NamedOfflinePlayer(displayName));
        fakeScr.setScore(score);
    }

    /**
     * Represents a fake OfflinePlayer - This is mainly intended for use with Scoreboards.
     * All methods but {@link #getName()} throw an {@link java.lang.UnsupportedOperationException}, do nothing or return {@code null} (depends on method),
     * therefore these objects should only be passed to methods which just use {@link #getName()} (or extend and implement).
     */
    @RequiredArgsConstructor
    public static class NamedOfflinePlayer implements OfflinePlayer {
        private static final UnsupportedOperationException UNSUPPORTED_OPERATION_EXCEPTION = //Store the exception because it's always the same and stuff
                new UnsupportedOperationException("This fake OfflinePlayer was just created to return a name," +
                        " to trick Bukkit in order to save performance (Thnx, EvilSeph) - It only supports getName().");
        @Getter
        private final String name;

        @Override
        public boolean isOnline() {
            return false;
        }

        @Override
        public UUID getUniqueId() {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }

        @Override
        public boolean isBanned() {
            return false; //Ignore this, won't hurt
        }

        @Override
        public void setBanned(boolean b) {
            //Ignore this, won't hurt
        }

        @Override
        public boolean isWhitelisted() {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }

        @Override
        public void setWhitelisted(boolean b) {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }

        @Override
        public Player getPlayer() {
            return null; //Return null to possibly trick implementations into believing the player is just offline
        }

        @Override
        public long getFirstPlayed() {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }

        @Override
        public long getLastPlayed() {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }

        @Override
        public boolean hasPlayedBefore() {
            return false;
        }

        @Override
        public Location getBedSpawnLocation() {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }

        @Override
        public Map<String, Object> serialize() {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }

        @Override
        public boolean isOp() {
            throw UNSUPPORTED_OPERATION_EXCEPTION;
        }

        @Override
        public void setOp(boolean b) {
            //Ignore this, won't hurt
        }
    }
}
