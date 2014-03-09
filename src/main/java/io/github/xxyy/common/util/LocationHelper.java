package io.github.xxyy.common.util;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;

/**
 * A class providing some static methods to deal with {@link Location}s.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class LocationHelper {
    /**
     * Determines if a location <code>toCheck</code> is between or
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
     * Determines if a number <code>toCheck</code> is between or
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
     * Rendomises x,y and z coordinates of a Location, in a radius.
     *
     * @param original Location to randomise
     * @param radius   Maximum distace from the original location
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
}
