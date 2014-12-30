/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.util;

import org.bukkit.scoreboard.Objective;

/**
 * Provides some static utility methods for dealing with Scoreboards.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 9.4.14
 */
public final class ScoreboardHelper {
    private ScoreboardHelper() {
    }

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
    public static void setFakeScore(final Objective objective, final String displayName, final int score, @SuppressWarnings("UnusedParameters") final boolean addToCache) {
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
