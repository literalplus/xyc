/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.util;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.github.xxyy.common.util.math.NumberHelper;

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
     * @param toCheck   the location to check
     * @param boundary1 the first boundary of the rectangle region toCheck has to be in
     * @param boundary2 the second boundary of the rectangle region toCheck has to be in
     * @return whether toCheck is in the rectangle represented by the boundaries
     * @see io.github.xxyy.common.util.math.NumberHelper#isNumberBetween(int, int, int)
     */
    public static boolean isBlockBetween(Location toCheck, Location boundary1, Location boundary2) {
        return NumberHelper.isNumberBetween(toCheck.getBlockX(), boundary1.getBlockX(), boundary2.getBlockX()) &&
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
     * @throws java.lang.IllegalArgumentException If the section does not contain all values or the world is not found.
     */
    public static Location fromConfiguration(@NotNull ConfigurationSection section) {
        Validate.isTrue(section.contains("world") && section.contains("x") && section.contains("y") && section.contains("z"),
                "The given section does not contain all of x,y, world and z!");

        World world = Bukkit.getWorld(section.getString("world"));
        Validate.notNull(world, "World is null");

        return new Location(world, section.getInt("x") + 0.5D, section.getInt("y") + 0.5D, section.getInt("z"));
    }

    /**
     * Gets a Location from a ConfigurationSection.
     * The section must contain {@code x},{@code y} and {@code z} (int) and {@code pitch}, {@code yaw} (float) and {@code world} (String).
     *
     * @param section Configuration to read from
     * @return The read Location.
     * @throws java.lang.IllegalArgumentException If the section does not contain all values.
     */
    public static Location fromDetailedConfiguration(@NotNull ConfigurationSection section) {
        Validate.isTrue(section.contains("pitch") && section.contains("yaw"),
                "The given section does not contain pitch and yaw!");

        Location location = fromConfiguration(section);
        location.setPitch(((Double) section.getDouble("pitch")).floatValue());
        location.setYaw(((Double) section.getDouble("yaw")).floatValue());

        return location;
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
    public static String createTpCommand(@NotNull Location loc, @Nullable String targetName) {
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
}
