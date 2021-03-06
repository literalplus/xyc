/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
