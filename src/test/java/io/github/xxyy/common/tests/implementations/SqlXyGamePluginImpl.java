package io.github.xxyy.common.tests.implementations;

import io.github.xxyy.common.games.GameLib;
import io.github.xxyy.common.xyplugin.SqlXyGamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

/**
* @author <a href="http://xxyy.github.io/">xxyy</a>
* @since 08/03/14
*/
@SuppressWarnings("deprecation")
public class SqlXyGamePluginImpl extends SqlXyGamePlugin {

    public SqlXyGamePluginImpl(){
        super(new JavaPluginLoader(Bukkit.getServer()),
                new PluginDescriptionFile("XYC","dev-unit_test", SqlXyGamePluginImpl.class.getName()),
                new File("target/tests/XYC-plugin-data/"), new File("target/xyc_imp.xy.jar"));
    }

    public void initConfig() {
    }

    @Override
    public String getChatPrefix() {
        return "[TST]";
    }

    @Override
    public void disable() {
    }

    @Override
    public void enable() {
    }

    @Override
    public String getSqlDb() {
        return GameLib.XY_DB_NAME;
    }

    @Override
    public String getSqlHost() {
        return "";
    }

    @Override
    public String getSqlPwd() {
        return "";
    }

    @Override
    public String getSqlUser() {
        return "";
    }

    @Override
    public void setError(String desc, String caller) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
