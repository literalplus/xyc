package io.github.xxyy.common.internal;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.xxyy.common.XyHelper;
import io.github.xxyy.common.XycConstants;
import io.github.xxyy.common.shared.uuid.UUIDRepositories;

/**
 * Bukkit plugin interface for XYC.
 * This is an internal class, not API.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 3.8.14
 */
public final class CommonPlugin extends JavaPlugin {

    private static CommonPlugin instance = null;

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
}
