/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.games.maps;

import li.l1t.common.games.teams.GameTeam;
import li.l1t.common.games.teams.TeamRegistry;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a map spawn.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 05/03/14
 */
public class MapSpawn implements ConfigurationSerializable {
    /**
     * Team this spawn belongs to.
     */
    @Nonnull
    private final GameTeam team;

    /**
     * Location this spawn is at.
     */
    @Nonnull
    private final Location location;

    @java.beans.ConstructorProperties({"team", "location"})
    public MapSpawn(GameTeam team, Location location) {
        this.team = team;
        this.location = location;
    }

    /**
     * Constructs a MapSpawn from a source (YAML) map.
     * Example of a source YAML:
     * {@code
     * team: YELLOW<br>
     * location:<br>
     * .   world: world<br>
     * .   x: 12<br>
     * .   y: 24<br>
     * .   z: 56<br>
     * .   pitch: 56<br>
     * .   yaw: 90<br>
     * }
     *
     * @param sourceMap Map to use as data source
     * @return A MapSpawn object corresponding to the given data.
     * @throws java.lang.IllegalArgumentException If (a) The source map does not contain all necessary data (All data is necessary)
     *                                            (b) An unknown team is given (c) An unknown world is given (c) One of the location types is not a number.
     */
    public static MapSpawn valueOf(@Nonnull Map<String, Object> sourceMap) {
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

    @Nonnull
    public GameTeam getTeam() {
        return this.team;
    }

    @Nonnull
    public Location getLocation() {
        return this.location;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MapSpawn)) return false;
        final MapSpawn other = (MapSpawn) o;
        if (!other.canEqual(this)) return false;
        final Object this$team = this.team;
        final Object other$team = other.team;
        if (this$team == null ? other$team != null : !this$team.equals(other$team)) return false;
        final Object this$location = this.location;
        final Object other$location = other.location;
        return this$location == null ? other$location == null : this$location.equals(other$location);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $team = this.team;
        result = result * PRIME + ($team == null ? 0 : $team.hashCode());
        final Object $location = this.location;
        result = result * PRIME + ($location == null ? 0 : $location.hashCode());
        return result;
    }

    public boolean canEqual(Object other) {
        return other instanceof MapSpawn;
    }

    public String toString() {
        return "io.github.xxyy.common.games.maps.MapSpawn(team=" + this.team + ", location=" + this.location + ")";
    }
}
