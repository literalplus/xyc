package io.github.xxyy.common.games.maps;

import io.github.xxyy.common.games.teams.GameTeam;
import io.github.xxyy.common.games.teams.TeamRegistry;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a map spawn.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 05/03/14
 */
@Data
@RequiredArgsConstructor
public class MapSpawn implements ConfigurationSerializable {
    /**
     * Team this spawn belongs to.
     */
    @NonNull
    private final GameTeam team;

    /**
     * Location this spawn is at.
     */
    @NonNull
    private final Location location;

    /**
     * Constructs a MapSpawn from a source (YAML) map.
     * Example of a source YAML:
     * <code>
     * team: YELLOW<br>
     * location:<br>
     *.   world: world<br>
     *.   x: 12<br>
     *.   y: 24<br>
     *.   z: 56<br>
     *.   pitch: 56<br>
     *.   yaw: 90<br>
     * </code>
     *
     * @param sourceMap Map to use as data source
     * @return A MapSpawn object corresponding to the given data.
     * @throws java.lang.IllegalArgumentException If (a) The source map does not contain all necessary data (All data is necessary)
     *                                            (b) An unknown team is given (c) An unknown world is given (c) One of the location types is not a number.
     */
    public static MapSpawn valueOf(@NonNull Map<String, Object> sourceMap) {
        Validate.isTrue(sourceMap.containsKey("team") &&
                sourceMap.containsKey("location.x") &&
                sourceMap.containsKey("location.y") &&
                sourceMap.containsKey("location.z") &&
                sourceMap.containsKey("location.pitch") &&
                sourceMap.containsKey("location.world") &&
                sourceMap.containsKey("location.yaw"), "Invalid source map: Does not contain all data!");

        GameTeam team = TeamRegistry.getTeam(sourceMap.get("team").toString(), GameTeam.class);
        Validate.notNull(team, "Unknown team saved: " + sourceMap.get("team"));

        World world = Bukkit.getWorld(sourceMap.get("location.world").toString());
        Validate.notNull(world, "Unknown world specified!");

        return new MapSpawn(team, new Location(
                world,
                tryGetLocationDouble(sourceMap, "x"),
                tryGetLocationDouble(sourceMap, "y"),
                tryGetLocationDouble(sourceMap, "z"),
                tryGetLocationFloat(sourceMap, "pitch"),
                tryGetLocationFloat(sourceMap, "yaw") //All these tryGetXXX methods throw IllegalArgumentExceptions for invalid types
        ));
    }

    private static double tryGetLocationDouble(Map<String, Object> map, String key) { //Forgive me for it not being generic enough, but IMO this improves readability a lot. It's private too.
        key = "location." + key; //TÖDO: Bad practice - Reassigning parameters
        Object obj = map.get(key);
        Validate.isTrue(obj instanceof Number, "A value in your location map at " + key + " is not a double:", obj);
        return (double) obj;
    }

    private static float tryGetLocationFloat(Map<String, Object> map, String key) { //Forgive me for it not being generic enough, but IMO this improves readability a lot. It's private too.
        key = "location." + key; //TÖDO: Bad practice - Reassigning parameters
        Object obj = map.get(key);
        Validate.isTrue(obj instanceof Number, "A value in your location map at " + key + " is not a float!", obj);
        return (float) obj;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("team", getTeam().getName());

        map.put("location.world", getLocation().getWorld().getName());
        map.put("location.x", getLocation().getX());
        map.put("location.y", getLocation().getY());
        map.put("location.z", getLocation().getZ());
        map.put("location.pitch", getLocation().getPitch());
        map.put("location.yaw", getLocation().getYaw());

        return map;
    }
}
