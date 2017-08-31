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

package li.l1t.common.games;

import li.l1t.common.games.data.PlayerWrapperBase;
import li.l1t.common.sql.SafeSql;
import li.l1t.common.xyplugin.SqlXyGamePlugin;
import org.bukkit.Bukkit;

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

    private static void initLib(SqlXyGamePlugin pl) {
        Bukkit.getLogger().log(Level.INFO, "\u00a78[Xyg] Loading xxyy98's game library...");

        ssql = pl.getSql();
        PlayerWrapperBase.initTable(pl.getSql());
        GameLib.isInit = true;
    }
}
