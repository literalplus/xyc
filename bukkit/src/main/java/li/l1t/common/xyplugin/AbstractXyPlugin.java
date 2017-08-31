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

import li.l1t.common.util.task.BukkitTaskService;
import li.l1t.common.util.task.TaskService;
import li.l1t.common.version.PluginVersion;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract implementation of the {@link XyPluggable} interface inheriting from
 * {@link org.bukkit.plugin.java.JavaPlugin}.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 */
public abstract class AbstractXyPlugin extends JavaPlugin implements XyPluggable {
    private static List<AbstractXyPlugin> INSTANCES = new ArrayList<>();
    private static List<AbstractXyPlugin> IMMUTABLE_INSTANCES = Collections.unmodifiableList(INSTANCES); //Changes with the wrapped List
    // but this is used far more often.
    private final PluginVersion pluginVersion = PluginVersion.ofClass(getClass());
    private final TaskService tasks = new BukkitTaskService(this);

    public AbstractXyPlugin() {
    }

    public AbstractXyPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    /**
     * @return All currently enabled AbstractXyPlugins.
     */
    public static List<AbstractXyPlugin> getInstances() {
        return IMMUTABLE_INSTANCES;
    }

    /**
     * @return the chat prefix for this XyPlugin. This is used by xy_common at multiple points.
     */
    public abstract String getChatPrefix();

    @Override
    public final void onDisable() {
        //here may be some logic eventually
        this.preUnloadImplementation();
        this.disable();
        this.unloadImplementation();
        INSTANCES.remove(this);
    }

    @Override
    public final void onEnable() {
        INSTANCES.add(this);
        this.loadImplementation();
        this.enable();
        this.postLoadImplementation();
    }

    protected abstract void loadImplementation();

    /**
     * Override this method to execute implementation code after {@link AbstractXyPlugin#enable()} is called.
     */
    protected void postLoadImplementation() {
    }

    /**
     * Override this method to execute implementation code before {@link AbstractXyPlugin#disable()} is called.
     */
    protected void preUnloadImplementation() {
    }

    /**
     * This sets the CommandExecutor for a command, using exec for both. Convenience method.
     *
     * @param cmdName the name of the base command to set the executor for
     * @param exec    the executor to set
     * @param <T>     generic type of the executor
     */
    protected final <T extends CommandExecutor> void setExec(final T exec, final String cmdName) {
        Validate.notNull(this.getCommand(cmdName), "Command " + cmdName + " is not registered for this plugin: " + this.toString());
        this.getCommand(cmdName).setExecutor(exec);
    }

    /**
     * This sets the TabCompleter and CommandExecutor for a command, using exec for both. Convenience method.
     *
     * @param cmdName the name of the base command to set the executor and completer for
     * @param exec    the executor and completer to set
     * @param <T>     generic type of the executor and completer
     */
    protected final <T extends CommandExecutor & TabCompleter> void setExecAndCompleter(final T exec, final String cmdName) {
        this.setExec(exec, cmdName);
        this.getCommand(cmdName).setTabCompleter(exec);
    }

    protected abstract void unloadImplementation();

    @Override
    public PluginVersion getPluginVersion() {
        return this.pluginVersion;
    }

    @Override
    public void async(Runnable task) {
        tasks.async(task);
    }

    @Override
    public void serverThread(Runnable task) {
        tasks.serverThread(task);
    }

    @Override
    public TaskService tasks() {
        return tasks;
    }
}
