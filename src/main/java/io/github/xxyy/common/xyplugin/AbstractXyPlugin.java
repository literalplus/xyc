package io.github.xxyy.common.xyplugin;

import io.github.xxyy.common.version.PluginVersion;
import lombok.Getter;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
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
public abstract class AbstractXyPlugin extends JavaPlugin implements XyPlugable {

    public AbstractXyPlugin(){
    }

    public AbstractXyPlugin(JavaPluginLoader loader,  PluginDescriptionFile description, File dataFolder, File file){
        super(loader, description, dataFolder, file);
    }

    private static List<AbstractXyPlugin> INSTANCES = new ArrayList<>();
    private static List<AbstractXyPlugin> IMMUTABLE_INSTANCES = Collections.unmodifiableList(INSTANCES); //Changes with the wrapped List
    // but this is used faaar more often.
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
        this.getCommand(cmdName).setExecutor(exec);
    }

    /**
     * This method is used to access JavaPlugin#initialize(...) from XyPluginLoader. Do NOT invoke manually!
     *
     * @see JavaPlugin#initialize(org.bukkit.plugin.PluginLoader, org.bukkit.Server, org.bukkit.plugin.PluginDescriptionFile, java.io.File, java.io.File, java.lang.ClassLoader)
     */
    protected final void initialize0(final PluginLoader loader, final Server server, final PluginDescriptionFile description, final File dataFolder, final File file, final ClassLoader classLoader) {
        super.initialize(loader, server, description, dataFolder, file, classLoader);
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
