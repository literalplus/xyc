/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.lib.com.mojang.api.profiles;

import java.util.UUID;

public interface ProfileRepository {
    Profile[] findProfilesByNames(String... names);

    /**
     * Finds the current (!) profile for a name at a specified time. So, for example, if the requested player was
     * named Albert at the given time, but has since changed their name to Berta, the returned profile will contain
     * the current name. (Berta, in that case)
     * <p>
     * Also note that, if you request the name at a time that the user profile did not yet exist, null will be
     * returned. Due to a possible Mojang bug, that is not the case if the user has already changed their name once.
     * </p>
     *
     * @param name     the name to request the profile for
     * @param unixTime the timestamp, in seconds since the begin of the Unix epoch
     * @return the profile, if found, null otherwise
     */
    Profile findProfileAtTime(String name, long unixTime);

    /**
     * Finds the name history of a profile. Returns null if there is no such profile.
     * @param uniqueId the unique id of the profile to find the name history for
     * @return an array of names or null if there is no such profile
     */
    NameData[] findNameHistory(UUID uniqueId); //returns an array because this is what Gson gives us. Cast at call site.
}
