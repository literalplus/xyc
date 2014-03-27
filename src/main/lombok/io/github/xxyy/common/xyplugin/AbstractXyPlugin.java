package io.github.xxyy.common.xyplugin;

import io.github.xxyy.common.version.PluginVersion;
import lombok.Getter;
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
 *
 *
 * Plugins SHOULD implement XyLocalizable for localization
 *
 * @author xxyy
 */
public abstract class AbstractXyPlugin extends JavaPlugin implements XyPluggable {

    public AbstractXyPlugin(){
    }

    public AbstractXyPlugin(JavaPluginLoader loader,  PluginDescriptionFile description, File dataFolder, File file){
        super(loader, description, dataFolder, file);
    }

    private static List<AbstractXyPlugin> INSTANCES = new ArrayList<>();
    private static List<AbstractXyPlugin> IMMUTABLE_INSTANCES = Collections.unmodifiableList(INSTANCES); //Changes with the wrapped List
    // but this is used far more often.
    @Getter
    private final PluginVersion pluginVersion = PluginVersion.ofClass(getClass());

    /**
     * @return All currently enabled AbstractXyPlugins.
     */
    public static List<AbstractXyPlugin> getInstances() {
        return IMMUTABLE_INSTANCES;
    }

    /**
     * Returns the chat prefix for this XyPlugin. This is used by xy_common on multiple points.
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
     */
    protected final <T extends CommandExecutor> void setExec(final T exec, final String cmdName) {
        Validate.notNull(this.getCommand(cmdName), "Command "+cmdName+" is not registered for this plugin: "+this.toString());
        this.getCommand(cmdName).setExecutor(exec);
    }

    /**
     * This sets the TabCompleter and CommandExecutor for a command, using exec for both. Convenience method.
     */
    protected final <T extends CommandExecutor & TabCompleter> void setExecAndCompleter(final T exec, final String cmdName) {
        this.setExec(exec, cmdName);
        this.getCommand(cmdName).setTabCompleter(exec);
    }

    protected abstract void unloadImplementation();
}
