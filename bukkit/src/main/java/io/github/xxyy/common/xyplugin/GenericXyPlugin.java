/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.xyplugin;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.xxyy.common.XyHelper;

import java.io.File;


/**
 * This can be used in actual plugins.
 * Registration in {@link XyHelper} has already been done.
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
