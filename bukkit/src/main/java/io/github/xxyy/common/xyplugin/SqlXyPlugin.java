/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.xyplugin;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.xxyy.common.sql.SafeSql;
import io.github.xxyy.common.sql.SpigotSql;
import io.github.xxyy.common.sql.SqlConnectable;
import io.github.xxyy.common.util.CommandHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides JDBC SQL support for plugins out of the box, using {@link io.github.xxyy.common.sql.SpigotSql}.
 * This also takes care of creating and destroying the managed SpigotSql.
 * <p>
 * <b>Note:</b> You need to override {@link #getConnectable()}, it is currently left concrete for compatibility reasons.
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
     * SafeSql managing SQL for this {@link SqlXyPlugin}.
     *
     * @deprecated Was not intended to be exposed. Please use {@link SqlXyPlugin#getSql()}. Will be removed or marked protected in future updates.
     */
    @Deprecated
    public SpigotSql ssql;

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
    public SafeSql getSql() {
        return this.ssql;
    }

    @SuppressWarnings("deprecation")
    protected final void loadSql() {
        SqlXyPlugin.INSTANCES.add(this);
        SqlConnectable connectable = getConnectable();
        try {
            this.ssql = new SpigotSql(connectable == null ? this : connectable, this);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            getLogger().severe("***************************************************");
            getLogger().severe("As of the SqlXyPlugin class JavaDoc, you need to");
            getLogger().severe("implement #getConnectable(). Sorry for the style.");
            getLogger().severe("***************************************************");
        }

        if (connectable == null) {
            getLogger().severe("***************************************************");
            getLogger().severe("The plugin " + getName() + " by " + CommandHelper.CSCollection(getDescription().getAuthors()));
            getLogger().severe("is using a severely deprecated API in XYC!");
            getLogger().severe("Please bug the author to switch to the new #getConnectable() API");
            getLogger().severe("AS SOON AS POSSIBLE. Thanks for your support.");
            getLogger().severe("***************************************************");
        }
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
     * <b>This method is temporarily concrete and will be made abstract very soon(tm). It is advised to implement this
     * method as soon as possible. For more details, see {@link #getSqlPwd()}.</b>
     *
     * @return SQL credentials which can be used to connect to the main database of this plugin
     */
    protected SqlConnectable getConnectable() {
        return null; //TODO: remove deprecated methods and make this method abstract. Also remember to remove the notices from JavaDoc.
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Was not intended to be exposed, kept for compatibility reasons. Will be removed very soon(tm).
     */
    @Override
    @Deprecated
    public String getSqlDb() {
        throw new UnsupportedOperationException("Public SQL credentials are unsupported as of XYC 3.1.0!");
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Was not intended to be exposed, kept for compatibility reasons. Will be removed very soon(tm).
     */
    @Override
    @Deprecated
    public String getSqlHost() {
        throw new UnsupportedOperationException("Public SQL credentials are unsupported as of XYC 3.1.0!");
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Was not intended to be exposed, kept for compatibility reasons. Will be removed very soon(tm).
     */
    @Override
    @Deprecated
    public String getSqlPwd() {
        throw new UnsupportedOperationException("Public SQL credentials are unsupported as of XYC 3.1.0!");
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Was not intended to be exposed, kept for compatibility reasons. Will be removed very soon(tm).
     */
    @Override
    @Deprecated
    public String getSqlUser() {
        throw new UnsupportedOperationException("Public SQL credentials are unsupported as of XYC 3.1.0!");
    }
}
