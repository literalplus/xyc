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

import com.google.common.base.Preconditions;
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
 * @author xxyy
 */
public abstract class AbstractXyPlugin extends JavaPlugin implements XyPluggable {

    private static List<AbstractXyPlugin> INSTANCES = new ArrayList<>();
    private static List<AbstractXyPlugin> IMMUTABLE_INSTANCES = Collections.unmodifiableList(INSTANCES); //Changes with the wrapped List
    // but this is used far more often.
    private final PluginVersion pluginVersion = PluginVersion.ofClass(getClass());
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
        Preconditions.checkNotNull(task, "task");
        getServer().getScheduler().runTaskAsynchronously(this, task);
    }

    @Override
    public void serverThread(Runnable task) {
        Preconditions.checkNotNull(task, "task");
        getServer().getScheduler().runTask(this, task);
    }
}
