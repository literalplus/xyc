/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.internal;

import li.l1t.common.XyHelper;
import li.l1t.common.XycConstants;
import li.l1t.common.shared.uuid.UUIDRepositories;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Bukkit plugin interface for XYC.
 * This is an internal class, not API.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 3.8.14
 */
public final class CommonPlugin extends JavaPlugin {

    private static CommonPlugin instance = null;

    public static CommonPlugin instance() {
        return instance;
    }

    public static boolean hasInstance() {
        return instance() != null;
    }

    /**
     * Makes sure that XYC is running standalone and is enabled.
     *
     * @return {@link #instance()}
     * @throws java.lang.IllegalStateException if it's not
     */
    public static CommonPlugin validateEnabled() throws IllegalStateException {
        if (!hasInstance() || !instance().isEnabled()) {
            throw new IllegalStateException("XYC must be running standalone and be enabled to use this feature!");
        }

        return instance();
    }

    @Override
    public void onEnable() {
        instance = this;

        UUIDRepositories.addRepository(UUIDRepositories.MOJANG_UUID_REPOSITORY, this);

        getLogger().info("XYC version " + XycConstants.VERSION);
        XyHelper.getLocale(); //Force init
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
