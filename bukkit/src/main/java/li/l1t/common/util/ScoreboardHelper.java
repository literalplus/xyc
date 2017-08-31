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

package li.l1t.common.util;

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
