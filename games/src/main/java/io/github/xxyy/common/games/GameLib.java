/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without permission from the
 *  original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.games;

import org.bukkit.Bukkit;

import io.github.xxyy.common.games.data.PlayerWrapperBase;
import io.github.xxyy.common.sql.SafeSql;
import io.github.xxyy.common.xyplugin.SqlXyGamePlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


/**
 * Main class of XYGameLib.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class GameLib {
    /**
     * List of registered plugins.
     */
    public static final List<SqlXyGamePlugin> plugs = new ArrayList<>();
    /**
     * Name of the database used by the library. Constant with value "games".
     */
    public static final String XY_DB_NAME = "games";
    /**
     * Common database used by all of MinoTopia to save overall stats.
     */
    public static final String CENTRAL_DB_NAME = "mt_main";
    private static boolean isInit = false;
    private static SafeSql ssql;

    /**
     * Registers a plugin and initialises the library if that has not already been done.
     *
     * @param pl {@link SqlXyGamePlugin} to register.
     */
    public static void registerPlugin(SqlXyGamePlugin pl) {
        GameLib.plugs.add(pl);
        if (!GameLib.isInit) {
            GameLib.initLib(pl);
        }
    }

    /**
     * Returns a {@link SafeSql} capable of executing queries on the {@value GameLib#XY_DB_NAME} database.
     *
     * @return A SafeSql with access to {@value GameLib#XY_DB_NAME}.
     * @throws IllegalStateException If GameLIb has not yet been initialised.
     */
    public static SafeSql getSql() {
        if (ssql == null) {
            throw new IllegalStateException("GameLib has not yet been initialised!");
        }
        return ssql;
    }

    /**
     * @param pl It's just needed.
     */
    private static void initLib(SqlXyGamePlugin pl) {
        Bukkit.getLogger().log(Level.INFO, "\u00a78[Xyg] Loading xxyy98's game library...");

        ssql = pl.getSql();
        PlayerWrapperBase.initTable(pl.getSql());

        Bukkit.getLogger().log(Level.INFO, "\u00a78[Xyg] Loaded xxyy98's game library!");
        GameLib.isInit = true;
    }
}
