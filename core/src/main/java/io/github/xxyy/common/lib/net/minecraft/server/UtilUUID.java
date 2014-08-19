package io.github.xxyy.common.lib.net.minecraft.server;

import com.google.common.base.Charsets;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * The UtilUUID class used by minecraft to get UUIDs from Strings.
 * Source: https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/UtilUUID.java
 *
 * @author Mojang
 * @since 09/03/14
 */
public class UtilUUID {

    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");

    /**
     * Checks whether a UUID is valid.
     *
     * @param s the string to validate
     * @return whether the input is a valid UUID, as accepted by Java
     * @deprecated Use {@link io.github.xxyy.common.util.UUIDHelper#isValidUUID(String)}
     */
    @Deprecated
    public static boolean isValidUUID(String s) {
        return UUID_PATTERN.matcher(s).matches();
    }

    /**
     * Gets an UUID from an input string.
     *
     * @param s the input
     * @return an UUID or NULL if the input is invalid
     * @deprecated USe {@link io.github.xxyy.common.util.UUIDHelper#getFromString(String)}
     */
    @Deprecated
    public static UUID getFromString(String s) {
        if (s == null) {
            return null;
        } else if (isValidUUID(s)) {
            return UUID.fromString(s);
        } else {
            if (s.length() == 32) {
                String s1 = s.substring(0, 8) + "-" + s.substring(8, 12) + "-" + s.substring(12, 16) + "-" + s.substring(16, 20) + "-" + s.substring(20, 32);

                if (isValidUUID(s1)) {
                    return UUID.fromString(s1);
                }
            }

            return null;
        }
    }

    /**
     * Gets the offline UUID for a name
     *
     * @param offlineName the name to get the id for
     * @return the offline UUID generated from the input
     * @deprecated Use {@link io.github.xxyy.common.util.UUIDHelper#getOfflineUUID(String)}
     */
    @Deprecated
    public static UUID offlineUUID(String offlineName) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + offlineName).getBytes(Charsets.UTF_8));
    }
}
