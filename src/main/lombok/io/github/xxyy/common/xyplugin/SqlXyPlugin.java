package io.github.xxyy.common.xyplugin;

import io.github.xxyy.common.sql.SafeSql;
import io.github.xxyy.common.sql.SqlConnectable;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * MUST be implemented if a plugin wants to use SafeSql. If you only need a single query, use XyHelper.commonutil Thou shall execute: ssql = new
 * SafeSql(this); ssql.con=sql.makeConnection(); ssql.dbName = "%DBNAME%";
 *
 *
 */
public abstract class SqlXyPlugin extends AbstractXyPlugin implements SqlConnectable {
    /**
     * Lists all currently registered {@link SqlXyPlugin}s.
     */
    public final static List<SqlXyPlugin> INSTANCES = new ArrayList<>();

    /**
     * SafeSql managing SQL for this {@link SqlXyPlugin}.
     *
     * @deprecated Was not intended to be exposed. Please use {@link SqlXyPlugin#getSql()}. May be removed or marked protected in future updates.
     */
    @Deprecated
    public SafeSql ssql;

    public SqlXyPlugin(){

    }

    public SqlXyPlugin(JavaPluginLoader loader,  PluginDescriptionFile descriptionFile,  File dataFolder, File file){
        super(loader, descriptionFile, dataFolder, file);
    }

    @Override
    public void loadImplementation() {
        this.loadSql();
    }

    @Override
    public void unloadImplementation() {
        this.unloadSql();
    }

    /**
     * SafeSql instance managed by this plugin.
     */
    public SafeSql getSql() {
        return this.ssql;
    }

    protected final void loadSql() {
        SqlXyPlugin.INSTANCES.add(this);
        this.ssql = new SafeSql(this);
    }

    protected final void unloadSql() {
        if (this.ssql != null) {
            this.ssql.preReload();
            this.ssql = null;
        }
        SqlXyPlugin.INSTANCES.remove(this);
    }
}
