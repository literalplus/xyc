/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
