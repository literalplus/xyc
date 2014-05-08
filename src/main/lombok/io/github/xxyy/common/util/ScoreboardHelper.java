package io.github.xxyy.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.scoreboard.Objective;

/**
 * Provides some static utility methods for dealing with Scoreboards.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 9.4.14
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScoreboardHelper {
    /**
     * Sets a fake score with a specified display name. Does not call {@link org.bukkit.Bukkit#getOfflinePlayer(String)}.
     *
     * @param objective   Objective to set
     * @param displayName Name to display
     * @param score       Score to set
     * @param addToCache  Kept for compatibility with old code. Ignored.
     * @deprecated Unused parameter. Kept for compatibility with existing code - Use
     * {@link #setFakeScore(org.bukkit.scoreboard.Objective, String, int)}.
     */
    @Deprecated
    public static void setFakeScore(final Objective objective, final String displayName, final int score, final boolean addToCache) {
        objective.getScore(displayName).setScore(score);
    }

    /**
     * Sets a fake score with a specified display name. Does not call {@link org.bukkit.Bukkit#getOfflinePlayer(String)}.
     *
     * @param objective   Objective to set
     * @param displayName Name to display
     * @param score       Score to set
     */
    public static void setFakeScore(final Objective objective, final String displayName, final int score) {
        //noinspection deprecation
        setFakeScore(objective, displayName, score, false);
    }
}
