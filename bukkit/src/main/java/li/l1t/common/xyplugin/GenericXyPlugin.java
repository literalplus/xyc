/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.xyplugin;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;


/**
 * A generic extension of {@link AbstractXyPlugin}, made for use in actual plugins.
 * Use {@link XyPluggable#enable()} for your JavaPlugin#onEnable() logic.
 * Use {@link XyPluggable#disable()} for your JavaPlugin#onDisable() logic.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class GenericXyPlugin extends AbstractXyPlugin {
    public GenericXyPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    public GenericXyPlugin() {

    }

    @Override
    public void loadImplementation() {
    }

    @Override
    public void unloadImplementation() {
    }
}
