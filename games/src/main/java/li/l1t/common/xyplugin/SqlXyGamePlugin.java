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
