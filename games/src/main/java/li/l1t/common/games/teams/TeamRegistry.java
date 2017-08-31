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

import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages instances of {@link Team}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 05/03/14
 */
public final class TeamRegistry {
    private static final Map<String, Team> TEAM_MAP = new HashMap<>(7, 1F); //There are not many games where you'd need more than 6 teams
    private static final PropertyChangeSupport PROPERTY_CHANGE_SUPPORT =
            new PropertyChangeSupport(new Object());

    private TeamRegistry() {
    }

    /**
     * @return All registered teams.
     */
    public static Collection<Team> getTeams() {
        return TEAM_MAP.values();
    }

    /**
     * Register a team.
     *
     * @param team Team to register
     */
    public static void registerTeam(@Nonnull Team team) {
        TEAM_MAP.put(team.getName(), team);

        PROPERTY_CHANGE_SUPPORT.firePropertyChange("teams", null, team);
    }

    /**
     * Get a team by its name.
     *
     * @param teamName Name of the team to get
     * @return A {@link Team} or null if there's no such team.
     */
    public static Team getTeam(@Nonnull String teamName) {
        return TEAM_MAP.get(teamName);
    }

    @SuppressWarnings("unchecked") //The class is actually checked, so no errors can occur. Sorry, Mr. Compiler
    public static <T> T getTeam(@Nonnull String teamName, Class<T> clazz) {
        Team team = getTeam(teamName);

        if (team == null) {
            return null;
        }

        Validate.isTrue(clazz.isAssignableFrom(team.getClass()), "The team denoted by the name " + teamName + " is of an invalid type!");

        return (T) team;
    }

    /**
     * Unregister a team by its name.
     *
     * @param teamName Name of the team to unregister.
     * @return Whether the team list changed as a result of this call.
     */
    public static boolean unregisterTeam(@Nonnull String teamName) {
        Team oldValue = TEAM_MAP.remove(teamName);

        if (oldValue != null) {
            PROPERTY_CHANGE_SUPPORT.firePropertyChange("teams", oldValue, null);
        }

        return oldValue != null;
    }

    /**
     * Unregister a team.
     *
     * @param team Team to unregister.
     * @return Whether the team list changed as a result of this call.
     */
    public static boolean unregisterTeam(@Nonnull Team team) {
        return unregisterTeam(team.getName());
    }

    /**
     * Add a listener that is called every time the team list changes.
     *
     * @param listener Listener to add
     */
    public static void addTeamListChangeListener(PropertyChangeListener listener) {
        PROPERTY_CHANGE_SUPPORT.addPropertyChangeListener(listener);
    }

    /**
     * Remove a listener that is called every time the team list changes.
     *
     * @param listener Listener to remove
     */
    public static void removeTeamListChangeListener(PropertyChangeListener listener) {
        PROPERTY_CHANGE_SUPPORT.removePropertyChangeListener(listener);
    }
}
