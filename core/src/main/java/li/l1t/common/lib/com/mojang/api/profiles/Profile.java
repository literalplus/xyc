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

/**
 * Represents a Minecraft profile.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 14.7.14
 */
public interface Profile {
    String getId();

    UUID getUniqueId();

    String getName();

    boolean isDemo();
}
