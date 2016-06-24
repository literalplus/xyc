/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.util;

import li.l1t.common.misc.XyLocation;
import li.l1t.common.util.math.NumberHelper;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

/**
 * A class providing some static methods to deal with {@link Location}s.
 * All static methods of this class can also be accessed from instances of {@link XyLocation}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class LocationHelper {
    /**
     * Determines if a location {@code toCheck} is location in between or at one of the boundaries specified.
     * There is no special order of the boundaries required, they can even be equal.
     * This also requires all locations to be in the same world. (throwing an exception if the boundaries are in
     * different worlds and returning FALSE if toCheck is in another world)
     *
     * @param toCheck   the location to check
     * @param boundary1 the first boundary of the rectangle region toCheck has to be in
     * @param boundary2 the second boundary of the rectangle region toCheck has to be in
     * @return whether toCheck is in the rectangle represented by the boundaries
     * @see NumberHelper#isNumberBetween(int, int, int)
     */
    public static boolean isBlockBetween(@Nonnull Location toCheck, @Nonnull Location boundary1, @Nonnull Location boundary2) {
        Validate.notNull(toCheck.getWorld(), "toCheck's world cannot be null!");
        Validate.notNull(boundary1.getWorld(), "boundary1's world cannot be null!");
        Validate.isTrue(boundary1.getWorld().equals(boundary2.getWorld()), "boundary worlds cannot be different!");

        return toCheck.getWorld().equals(boundary1.getWorld()) &&
                NumberHelper.isNumberBetween(toCheck.getBlockX(), boundary1.getBlockX(), boundary2.getBlockX()) &&
                NumberHelper.isNumberBetween(toCheck.getBlockY(), boundary1.getBlockY(), boundary2.getBlockY()) &&
                NumberHelper.isNumberBetween(toCheck.getBlockZ(), boundary1.getBlockZ(), boundary2.getBlockZ());
    }

    /**
     * Randomises x,y and z coordinates of a Location, in a (square!) radius.
     *
     * @param original Location to randomise
     * @param radius   Maximum distance from the original location
     * @return a random location with at most radius distance from the original location
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
     *
     * @param section Configuration to read from
     * @return The read Location.
     * @see XyLocation#deserialize(java.util.Map)
     */
    public static Location fromConfiguration(@Nonnull ConfigurationSection section) {
        return fromDetailedConfiguration(section);
    }

    /**
     * Gets a Location from a ConfigurationSection.
     * The section must contain {@code x},{@code y} and {@code z} (int) and {@code pitch}, {@code yaw} (float) and {@code world} (String).
     *
     * @param section Configuration to read from
     * @return The read Location.
     * @see XyLocation#deserialize(java.util.Map)
     */
    public static Location fromDetailedConfiguration(@Nonnull ConfigurationSection section) {
        return XyLocation.deserialize(section.getValues(false));
    }

    /**
     * Creates a string representing a given location, featuring integer values of x,y and z as well as the world name.
     *
     * @param loc the location to pretty-print
     * @return a string representing the location
     */
    public static String prettyPrint(@Nullable Location loc) {
        return loc == null ? "null" :
                String.format("x=%d,y=%d,z=%d in %s",
                        loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName());
    }

    /**
     * Generates a /minecraft:tp command string to teleport a player to a given location.
     * If no target name is given, the command will be generated in such a way that it teleports the player executing it.
     *
     * @param loc        the location to teleport to
     * @param targetName the name of the player to target or NULL for the command's executor
     * @return a command string (including /) corresponding to the arguments
     */
    public static String createTpCommand(@Nonnull Location loc, @Nullable String targetName) {
        StringBuilder stringBuilder = new StringBuilder("/minecraft:tp ");

        if (targetName != null) {
            stringBuilder.append(targetName).append(" ");
        }

        return stringBuilder
                .append(loc.getBlockX()).append(" ")
                .append(loc.getBlockY()).append(" ")
                .append(loc.getBlockZ()).append(" ")
                .toString();
    }

    /**
     * Checks that two locations are in the same world and their integer block values are equal. This is here because
     * {@link org.bukkit.Location#equals(Object)} uses exact floating point values and such precision is rarely required.
     *
     * @param a the first location
     * @param b the location to compare
     * @return whether both locations are in the same world and their x,y,z blocks are the same.
     */
    public static boolean softEqual(Location a, Location b) {
        if (a.getWorld() != b.getWorld() && (a.getWorld() != null && !a.getWorld().equals(b.getWorld()))) {
            return false;
        }

        return a.getBlockX() == b.getBlockX() &&
                a.getBlockY() == b.getBlockY() &&
                a.getBlockZ() == b.getBlockZ();
    }

    /**
     * Serializes a location to a string. This is only accurate at block level.
     *
     * @param loc the location to serialize
     * @return a string uniquely identifying the location.
     * @see #deserialize(String)
     */
    public static String serialize(Location loc) {
        return loc.getWorld().getName() + ";" +
                loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ() + ";" +
                loc.getPitch() + ";" + loc.getYaw();
    }

    /**
     * Deserializes a location from a string.
     *
     * @param input the input string
     * @return the location specified by the input.
     * @throws java.lang.IllegalArgumentException if the input string is invalid
     * @see #serialize(org.bukkit.Location)
     */
    public static XyLocation deserialize(String input) {
        String[] arr = input.split(";");
        Validate.isTrue(arr.length == 6, "Invalid length");
        String worldName = arr[0];
        World world = Bukkit.getWorld(worldName);
        Validate.notNull(world, "Unknown world");

        return deserialize(input, world);
    }

    /**
     * Deserializes a location from a string. This accepts a world parameter to force the location to be in a specified
     * world.
     *
     * @param input the input string
     * @param world the world to force
     * @return the location specified by the input.
     * @throws java.lang.IllegalArgumentException if the input string is invalid
     * @see #serialize(org.bukkit.Location)
     */
    public static XyLocation deserialize(String input, World world) {
        String[] arr = input.split(";");
        Validate.isTrue(arr.length == 6, "Invalid length");
        Validate.notNull(world, "world");

        int x, y, z;
        float pitch, yaw;
        try {
            x = Integer.parseInt(arr[1]);
            y = Integer.parseInt(arr[2]);
            z = Integer.parseInt(arr[3]);
            pitch = Float.parseFloat(arr[4]);
            yaw = Float.parseFloat(arr[5]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format", e);
        }

        return new XyLocation(world, x, y, z, yaw, pitch);
    }
}
