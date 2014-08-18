/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.util;

import com.google.common.base.Charsets;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Helps dealing with UUIDs.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 4.8.14
 */
public class UUIDHelper {
    /**
     * A Pattern that matches valid Java UUIDs.
     */
    public static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");

    /**
     * Performs a match using {@link #UUID_PATTERN} to check whether {@code input} is a valid UUID as accepted by the
     * Java {@link java.util.UUID} impl.
     *
     * @param input Input string to check
     * @return Whether the input string is a valid Java UUID.
     */
    public static boolean isValidUUID(String input) {
        return UUID_PATTERN.matcher(input).matches();
    }

    /**
     * Creates an "offline" UUID as Minecraft would use for "cracked" players.
     *
     * @param offlineName The offline player's name, case-sensitive.
     * @return the offline UUID for given name.
     */
    public static UUID getOfflineUUID(String offlineName) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + offlineName).getBytes(Charsets.UTF_8));
    }
}
