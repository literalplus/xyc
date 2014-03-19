package io.github.xxyy.common.games.teams;

import io.github.xxyy.common.games.maps.MapSpawn;
import lombok.NonNull;

import javax.annotation.Nullable;
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
     * @param mapSpawn Spawn to add.
     */
    void addSpawn(@NonNull MapSpawn mapSpawn);
}
