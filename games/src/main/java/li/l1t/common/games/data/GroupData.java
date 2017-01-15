/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.common.games.data;

import li.l1t.common.games.GameLib;
import li.l1t.common.sql.QueryResult;
import li.l1t.common.sql.SafeSql;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Provides data of groups.
 *
 * @author xxyy98
 * @deprecated Part of the deprecated PlayerWrapper API. See {@link PlayerWrapper} for details.
 */
@Deprecated
public class GroupData {

    /**
     * Provides the full table name that is used for storing permissions. Constant, default value is {@value GameLib#XY_DB_NAME}.game_permissions.
     */
    public static final String FULL_XY_PERM_TABLE = GameLib.CENTRAL_DB_NAME + ".groups_permissions";
    /**
     * Provides the full table name that is used for storing group meta. Constant, default value is {@value GameLib#XY_DB_NAME}.game_permissions.
     */
    public static final String FULL_XY_META_TABLE = GameLib.CENTRAL_DB_NAME + ".groups";
    private static Map<String, GroupData> cache = new HashMap<>();
    private final SafeSql ssql;
    private String name;
    private List<String> rawPermissions = null;
    private Map<String, Boolean> matchedPermissions = new ConcurrentHashMap<>(16, 0.75F, 2);
    private String nameColor = "§9";
    private String chatColor = "§7";
    private boolean metaFetched = false;

    /**
     * Constructs a {@link GroupData} by its name.
     *
     * @param name Name of the group that this {@link GroupData} object represents.
     * @param ssql the SafeSql to get data from
     */
    protected GroupData(String name, SafeSql ssql) {
        this.name = name;
        this.ssql = ssql;
    }

    /**
     * Factory method that returns a {@link GroupData} object for this name.
     *
     * @param name Name of the group the object will represent.
     * @param ssql SafeSql to use to communicate with the database.
     * @return A (not necessarily new) {@link GroupData} object that represents the group of the name {@code name}.
     */
    public static GroupData getByName(@Nullable String name, @Nonnull SafeSql ssql) {
        Validate.notNull(ssql, "ssql");
        GroupData rtrn = GroupData.cache.computeIfAbsent(name, k -> new GroupData(name, ssql));
        return rtrn;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GroupData && this.name.equalsIgnoreCase(((GroupData) obj).getName());
    }

    /**
     * @return The chat color of the group represented by this {@link GroupData} object.
     */
    public String getChatColor() {
        if (!this.metaFetched) {
            this.fetchMeta();
        }
        return this.chatColor;
    }

    /**
     * @return The name of the group represented by this {@link GroupData} object.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The name color of the group represented by this {@link GroupData} object.
     */
    public String getNameColor() {
        if (!this.metaFetched) {
            this.fetchMeta();
        }
        return this.nameColor;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    /**
     * Checks if the group denoted by this {@link GroupData} object has a certain permission. RegEx allowed. negated ("explicit") nodes prefixed by
     * '-' will override any others, including ".*" ("super-node")
     *
     * @param regex Permission to check.
     * @return Whether this group has the given permission.
     */
    public synchronized boolean hasPermission(String regex) {
        if (this.rawPermissions == null) {
            this.fetchPermissions();
        }
        if (this.rawPermissions.isEmpty()) {
            return false;
        }
        if (this.matchedPermissions.containsKey(regex)) {
            return this.matchedPermissions.get(regex);
        }
        boolean foundImplicitMatch = false;
        for (String rawPerm : this.rawPermissions) {
            if (!foundImplicitMatch && Pattern.compile(rawPerm, Pattern.CASE_INSENSITIVE).matcher(regex).matches()) {
                foundImplicitMatch = true;
                continue; // for .*
            }
            if (Pattern.compile("-" + rawPerm, Pattern.CASE_INSENSITIVE).matcher(regex).matches()) {
                this.matchedPermissions.put(regex, false);
                return false;
            }
        }
        if (foundImplicitMatch) {
            this.matchedPermissions.put(regex, true);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "TDGroup: " + this.name;
    }

    @SuppressWarnings("SpellCheckingInspection")
    void fetchMeta() {
        try (QueryResult queryResult = ssql.executeQueryWithResult("SELECT namecol, chatcol FROM " +
                GroupData.FULL_XY_META_TABLE + " WHERE groupname=?", this.name).assertHasResultSet()) {
            if (!queryResult.rs().next()) {
                ssql.safelyExecuteUpdate("INSERT INTO " + GroupData.FULL_XY_META_TABLE + " SET groupname=?", this.name);
                this.metaFetched = true;
                return;
            }
            this.nameColor = ChatColor.translateAlternateColorCodes('&', queryResult.rs().getString("namecol"));
            this.chatColor = ChatColor.translateAlternateColorCodes('&', queryResult.rs().getString("chatcol"));
            this.metaFetched = true;
        } catch (SQLException e) {
            ssql.formatAndPrintException(e, "GroupData#fetchMeta");
        }
    }

    void fetchPermissions() {
        this.rawPermissions = Collections.synchronizedList(new ArrayList<>());

        try (QueryResult queryResult = ssql.executeQueryWithResult("SELECT permission FROM " +
                GroupData.FULL_XY_PERM_TABLE + " WHERE groupname=?", this.name).assertHasResultSet()) {
            while (queryResult.rs().next()) {
                this.rawPermissions.add(queryResult.rs().getString("permission"));
            }
        } catch (SQLException e) {
            ssql.formatAndPrintException(e, "GroupData#fetchPermissions");
            // np, all permission checks will return false
        }
    }
}
