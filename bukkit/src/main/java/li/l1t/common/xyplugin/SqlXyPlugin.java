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

import li.l1t.common.sql.SpigotSql;
import li.l1t.common.sql.SqlConnectable;
import li.l1t.common.sql.SqlConnectables;
import li.l1t.common.sql.sane.SaneSql;
import li.l1t.common.sql.sane.SingleSql;
import li.l1t.common.sql.sane.SqlConnected;
import li.l1t.common.util.Closer;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides JDBC SQL support for plugins out of the box, using {@link SpigotSql}.
 * This also takes care of creating and destroying the managed SpigotSql.
 * <p>
 * <b>Note:</b> Read the {@link #getConnectable()} JavaDoc for information on how credentials are
 * managed.
 * </p>
 *
 * @author xxyy
 * @since practically forever
 */
public abstract class SqlXyPlugin extends AbstractXyPlugin implements SqlConnected {
    /**
     * Lists all currently registered {@link SqlXyPlugin}s.
     */
    public final static List<SqlXyPlugin> INSTANCES = new ArrayList<>();

    /**
     * SafeSql managing SQL for this {@link SqlXyPlugin}. Was historically public, but has been
     * replaced with {@link #getSql()} since XYC 3.1.0.
     */
    private SpigotSql ssql;

    private SaneSql saneSql;

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

    @Override
    public SaneSql sql() {
        return saneSql;
    }

    @SuppressWarnings("deprecation")
    protected final void loadSql() {
        SqlXyPlugin.INSTANCES.add(this);
        this.ssql = new SpigotSql(getConnectable(), this);
        this.saneSql = new SingleSql(getConnectable());
    }

    @SuppressWarnings("deprecation")
    protected final void unloadSql() {
        if (this.ssql != null) {
            this.ssql.preReload();
            this.ssql = null;
        }
        Closer.close(saneSql);
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
        getConfig().addDefault("sql.host", "jdbc:mysql://localhost:3306/");
        getConfig().addDefault("sql.database", "MissingCredentialsInConfigYml");
        getConfig().addDefault("sql.user", "PleaseProvideCredentials");
        getConfig().addDefault("sql.password", "Please, use a secure password.");
        return SqlConnectables.fromCredentials(
                getConfig().getString("sql.host"),
                getConfig().getString("sql.database"),
                getConfig().getString("sql.user"),
                getConfig().getString("sql.password")
        );
    }
}
