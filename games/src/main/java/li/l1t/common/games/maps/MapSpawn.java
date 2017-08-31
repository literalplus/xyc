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
    public MapSpawn(@Nonnull GameTeam team, @Nonnull Location location) {
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
        return other.canEqual(this) && this.team.equals(other.team) && this.location.equals(other.location);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.team.hashCode();
        result = result * PRIME + this.location.hashCode();
        return result;
    }

    public boolean canEqual(Object other) {
        return other instanceof MapSpawn;
    }

    public String toString() {
        return "li.l1t.common.games.maps.MapSpawn(team=" + this.team + ", location=" + this.location + ")";
    }
}
