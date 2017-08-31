/*
 * -- placeholder --
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
