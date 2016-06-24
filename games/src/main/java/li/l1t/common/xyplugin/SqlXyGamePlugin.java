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

import li.l1t.common.games.GameLib;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

/**
 * A game plugin that uses SQL.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class SqlXyGamePlugin extends SqlXyPlugin implements XyGamePlugin {
    public SqlXyGamePlugin() {

    }

    public SqlXyGamePlugin(JavaPluginLoader loader, PluginDescriptionFile descriptionFile, File dataFolder, File file) {
        super(loader, descriptionFile, dataFolder, file);
    }

    @Override
    public void loadImplementation() {
        this.loadGame();
    }

    @Override
    public void unloadImplementation() {
        this.unloadGame();
    }

    /**
     * In this method, initialise your plugin's configuration.
     * This is called <b>before</b> {@link SqlXyGamePlugin#enable()} in order
     * to allow XYGameLib to initialize its tables with an actual connection
     * to a database (and that a config can be generated on the first start of your plugin).
     */
    protected abstract void initConfig();

    protected final void loadGame() {
        this.initConfig();
        this.loadSql();
        GameLib.registerPlugin(this);
    }

    protected final void unloadGame() {
        this.unloadSql();
    }
}
