package io.github.xxyy.common.xyplugin;

import io.github.xxyy.common.XyHelper;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;


/**
 * This can be used in actual plugins.
 * Registration in {@link XyHelper} has already been done.
 * Use {@link XyPluggable#enable()} for your JavaPlugin#onEnable() logic.
 * Use {@link XyPluggable#disable()} for your JavaPlugin#onDisable() logic.

 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class GenericXyPlugin extends AbstractXyPlugin {
    public GenericXyPlugin(JavaPluginLoader loader,  PluginDescriptionFile description, File dataFolder, File file){
        super(loader, description, dataFolder, file);
    }

    public GenericXyPlugin() {

    }

    @Override
    public void loadImplementation(){ }
    
    @Override
    public void unloadImplementation(){ }
}
