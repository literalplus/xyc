package io.github.xxyy.common.util;

import lombok.NonNull;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

/**
 * A class providing some static methods to deal with {@link Location}s.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class LocationHelper {
    /**
     * Determines if a location {@code toCheck} is between or
     * located on one of the boundaries specified.
     * There is no special order of the boundaries required,
     * they can even be equal.
     *
     * @see CommandHelper#isNumberBetween(int, int, int)
     */
    public static boolean isBlockBetween(Location toCheck, Location boundary1, Location boundary2) {
        return CommandHelper.isNumberBetween(toCheck.getBlockX(), boundary1.getBlockX(), boundary2.getBlockX()) &&
                CommandHelper.isNumberBetween(toCheck.getBlockY(), boundary1.getBlockY(), boundary2.getBlockY()) &&
                CommandHelper.isNumberBetween(toCheck.getBlockZ(), boundary1.getBlockZ(), boundary2.getBlockZ());
    }

    /**
     * Determines if a number {@code toCheck} is between or
     * equal to one the boundaries specified.
     * There is no special order of the boundaries required,
     *
     * @deprecated Use {@link CommandHelper#isNumberBetween(int, int, int)} instead
     */
    @Deprecated
    public static boolean isNumberBetween(int toCheck, int boundary1, int boundary2) {
        return CommandHelper.isNumberBetween(toCheck, boundary1, boundary2);
    }

    /**
     * Randomises x,y and z coordinates of a Location, in a radius.
     *
     * @param original Location to randomise
     * @param radius   Maximum distance from the original location
     */
    public static Location randomiseLocation(Location original, int radius) {
        int modX = RandomUtils.nextInt(radius);
        int modY = RandomUtils.nextInt(radius);
        int modZ = RandomUtils.nextInt(radius);
        if (RandomUtils.nextBoolean()) {
            modX = modX * -1;
        } // so that randomisation goes
        if (RandomUtils.nextBoolean()) {
            modY = modY * -1;
        } // in both directions!!
        if (RandomUtils.nextBoolean()) {
            modZ = modZ * -1;
        } // ^^^^^^^^^^^^^^^^
        return new Location(original.getWorld(),
                original.getBlockX() + modX, // create new object so that
                original.getBlockY() + modY, // original can be reused
                original.getBlockZ() + modZ);
    }

    /**
     * Gets a Location from a ConfigurationSection.
     * The section must contain {@code x},{@code y} and {@code z} (int) and {@code world} (String).
     * @param section Configuration to read from
     * @return The read Location.
     * @throws java.lang.IllegalArgumentException If the section does not contain all values.
     */
    public static Location fromConfiguration(@NonNull ConfigurationSection section){
        Validate.isTrue(section.contains("world") && section.contains("x") && section.contains("y") && section.contains("z"),
                "The given section does not contain all of x,y and z!");

        return new Location(Bukkit.getWorld(section.getString("world")), section.getInt("x"), section.getInt("y"), section.getInt("z"));
    }
}
