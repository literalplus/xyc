/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.games.teams;


import io.github.xxyy.common.games.maps.MapSpawn;
import io.github.xxyy.lib.intellij_annotations.NotNull;
import io.github.xxyy.lib.intellij_annotations.Nullable;

import java.util.List;

/**
 * Represents a team of players who have to work together in a game.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 05/03/14
 */
public interface GameTeam extends Team {
    /**
     * @return The list of spawns registered for this team.
     */
    @Nullable
    List<MapSpawn> getSpawns();

    /**
     * Adds a {@link io.github.xxyy.common.games.maps.MapSpawn} to this team's spawn list.
     *
     * @param mapSpawn Spawn to add.
     */
    void addSpawn(@NotNull MapSpawn mapSpawn);
}
