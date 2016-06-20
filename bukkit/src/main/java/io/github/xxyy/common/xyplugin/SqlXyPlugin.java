/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.xyplugin;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.xxyy.common.sql.SpigotSql;
import io.github.xxyy.common.sql.SqlConnectable;
import io.github.xxyy.common.sql.SqlConnectables;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides JDBC SQL support for plugins out of the box, using {@link io.github.xxyy.common.sql.SpigotSql}.
 * This also takes care of creating and destroying the managed SpigotSql.
 * <p>
 * <b>Note:</b> Read the {@link #getConnectable()} JavaDoc for information on how credentials are
 * managed.
 * </p>
 *
 * @author xxyy
 * @since practically forever
 */
public abstract class SqlXyPlugin extends AbstractXyPlugin implements SqlConnectable {
    /**
     * Lists all currently registered {@link SqlXyPlugin}s.
     */
    public final static List<SqlXyPlugin> INSTANCES = new ArrayList<>();

    /**
     * SafeSql managing SQL for this {@link SqlXyPlugin}. Was hsitorically public, but has been
     * replaced with {@link #getSql()} since XYC 3.1.0.
     */
    private SpigotSql ssql;

    public SqlXyPlugin() {

    }

    public SqlXyPlugin(JavaPluginLoader loader, PluginDescriptionFile descriptionFile, File dataFolder, File file) {
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
     * @return the SafeSql instance managed by this plugin.
     */
    @SuppressWarnings("deprecation")
    public SpigotSql getSql() {
        return this.ssql;
    }

    @SuppressWarnings("deprecation")
    protected final void loadSql() {
        SqlXyPlugin.INSTANCES.add(this);
        this.ssql = new SpigotSql(getConnectable(), this);
    }

    @SuppressWarnings("deprecation")
    protected final void unloadSql() {
        if (this.ssql != null) {
            this.ssql.preReload();
            this.ssql = null;
        }
        SqlXyPlugin.INSTANCES.remove(this);
    }

    /**
     * Gets the credentials used to connect to the JDBC SQL database used by this plugin. By
     * default, this uses the following values from the plugin configuration:
     * {@code sql.host}, {@code sql.database}, {@code sql.user}, and {@code sql.password} for the
     * respective credential. This method also sets configuration defaults so that users can set
     * the correct credentials easily.
     *
     * @return JDBC credentials which may be used to connect to the main database of this plugin
     */
    protected SqlConnectable getConnectable() {
        return SqlConnectables.fromCredentials(
                getConfig().getString("sql.host", "jdbc:mysql://localhost:3306/"),
                getConfig().getString("sql.database", "minecraft"),
                getConfig().getString("sql.user", "root"),
                getConfig().getString("sql.password", "myverysecurepassword")
        );
    }
}
