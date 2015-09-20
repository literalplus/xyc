/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.lib.com.mojang.api.profiles;

public interface ProfileRepository {
    Profile[] findProfilesByNames(String... names);

    /**
     * Finds the current (!) profile for a name at a specified time. So, for example, if the requested player was
     * named Albert at the given time, but has since changed their name to Berta, the returned profile will contain
     * the current name. (Berta, in that case)
     * @param name the name to request the profile for
     * @param unixTime the timestamp, in seconds since the begin of the Unix epoch
     * @return the profile, if found
     */
    Profile findProfileAtTime(String name, long unixTime);
}
