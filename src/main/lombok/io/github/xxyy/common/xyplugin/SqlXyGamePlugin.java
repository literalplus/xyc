package io.github.xxyy.common.xyplugin;

import io.github.xxyy.common.games.GameLib;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

/**
 * A game plugin that uses SQL.
 * 
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class SqlXyGamePlugin extends SqlXyPlugin implements XyGamePlugin
{
    public SqlXyGamePlugin(){

    }

    public SqlXyGamePlugin(JavaPluginLoader loader, PluginDescriptionFile descriptionFile, File dataFolder, File file){
        super(loader, descriptionFile, dataFolder, file);
    }

    @Override
    public void loadImplementation()
    {
        this.loadGame();
    }

    @Override
    public void unloadImplementation()
    {
        this.unloadGame();
    }
    
    /**
     * In this method, initialise your plugin's configuration.
     * This is called <b>before</b> {@link SqlXyGamePlugin#enable()} in order
     * to allow XYGameLib to initialize it's tables with an actual connection
     * to a database (and that a config can be generated on the first start of your plugin.
     */
    protected abstract void initConfig();
    
    protected final void loadGame(){
        this.initConfig();
        this.loadSql();
        GameLib.registerPlugin(this);
    }
    
    protected final void unloadGame(){
        this.unloadSql();
    }
}
