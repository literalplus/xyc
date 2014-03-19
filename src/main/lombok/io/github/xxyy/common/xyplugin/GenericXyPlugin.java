package io.github.xxyy.common.xyplugin;

import io.github.xxyy.common.XyHelper;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * This can be used in actual plugins.
 * Registration in {@link XyHelper} has already been done.
 * Use {@link XyPlugable#enable()} for your JavaPlugin#onEnable() logic.
 * Use {@link XyPlugable#disable()} for your JavaPlugin#onDisable() logic.
 * 
 * @see {@link AbstractXyPlugin}
 * @see {@link XyPlugable}
 * @see {@link JavaPlugin}
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class GenericXyPlugin extends AbstractXyPlugin {
    @Override
    public void loadImplementation(){ }
    
    @Override
    public void unloadImplementation(){ }
}
