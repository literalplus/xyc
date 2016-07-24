/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common;

import li.l1t.common.localisation.LangHelper;
import li.l1t.common.localisation.XycLocale;
import li.l1t.common.xyplugin.AbstractXyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

/**
 * Class that deals with initialisation of XYC and storing plugins.
 *
 * @author xxyy98
 */
public class XyHelper {
    private static final File cfgFile = new File("plugins/XYC", "xyc.cfg.yml");
    /**
     * Default language used by {@link LangHelper}.
     */
    public static String defaultLang = "de_AT";
    /**
     * XYC primary chat color. Whatever.
     */
    public static String priChatCol = "§7";
    /**
     * Chat color used for warnings.
     */
    public static String warChatCol = "§8";
    /**
     * Chat color used for code or commands.
     */
    public static String codeChatCol = "§3";
    private static YamlConfiguration xycConfiguration = YamlConfiguration.loadConfiguration(XyHelper.cfgFile);
    private static XycLocale locale;

    static {
        initialise();
    }

    /**
     * @return the XYC config as {@link YamlConfiguration}.
     */
    public static YamlConfiguration getCfg() {
        return XyHelper.xycConfiguration;
    }

    /**
     * @return the internal locale storage.
     */
    public static XycLocale getLocale() {
        return XyHelper.locale;
    }

    private static void initialise() {

        //config
        XyHelper.xycConfiguration.options().header("XYC....use valid YAML!");
        XyHelper.xycConfiguration.options().copyHeader(true);
        XyHelper.xycConfiguration.options().copyDefaults(true);
        XyHelper.xycConfiguration.addDefault("sql.user", "root");
        XyHelper.xycConfiguration.addDefault("sql.db", "minecraft");
        XyHelper.xycConfiguration.addDefault("sql.password", "");
        XyHelper.xycConfiguration.addDefault("sql.host", "jdbc:mysql://localhost:3306/");

        //locale
        XyHelper.locale = new XycLocale();
        XyHelper.priChatCol = XycLocale.getString("XYC-pricol", "CONSOLE");
        XyHelper.warChatCol = XycLocale.getString("XYC-warningcol", "CONSOLE");
        XyHelper.codeChatCol = XycLocale.getString("XYC-codecol", "CONSOLE");

//        XyHelper.isSql=ev.isSql();
        Bukkit.getConsoleSender().sendMessage("§8[XYC] Initialised XYC!");
    }

    /**
     * @return a list of plugins registered with XYC
     * @deprecated Use {@link AbstractXyPlugin#getInstances()}.
     */
    @Deprecated
    public static List<AbstractXyPlugin> getPlugins() {
        return AbstractXyPlugin.getInstances();
    }
}
