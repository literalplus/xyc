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
import li.l1t.common.sql.builder.ConcurrentSqlNumberHolder;
import li.l1t.common.sql.builder.QueryBuilder;
import li.l1t.common.sql.builder.SqlHolders;
import li.l1t.common.sql.builder.SqlIdentifierHolder;
import li.l1t.common.sql.builder.SqlUUIDHolder;
import li.l1t.common.sql.builder.SqlValueHolder;
import li.l1t.common.sql.builder.annotation.SqlValueCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.Metadatable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

import static li.l1t.common.sql.builder.annotation.SqlValueCache.Type.NUMBER_MODIFICATION;
import static li.l1t.common.sql.builder.annotation.SqlValueCache.Type.OBJECT_IDENTIFIER;
import static li.l1t.common.sql.builder.annotation.SqlValueCache.Type.OBJECT_UPDATE;
import static li.l1t.common.sql.builder.annotation.SqlValueCache.Type.UUID_IDENTIFIER;

/**
 * Base stuff of PlayerWrapper.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2.4.14
 * @deprecated Part of the deprecated PlayerWrapper API. See {@link PlayerWrapper} for details.
 */
@Deprecated
public abstract class PlayerWrapperBase implements SqlValueHolder.DataSource, Metadatable {
    //    public static final String FULL_XY_TABLE_NAME = GameLib.XY_DB_NAME + ".game_users";
    public static final String FULL_CENTRAL_USER_TABLE_NAME = GameLib.CENTRAL_DB_NAME + ".user";
    private static SqlHolders.CacheBuilder BASE_CACHE_BUILDER;
//    public static final HttpProfileRepository HTTP_PROFILE_REPOSITORY = new HttpProfileRepository();

    /**
     * SafeSql object used by this wrapper to communicate to a database.
     */
    protected final SafeSql sql;
    protected boolean xyFetched = false;
    protected boolean impFetched = false;
    protected boolean xyChanged = false;
    protected boolean isConsole = false;
    protected WeakReference<Player> weakPlr;

    protected GroupData group;

    @SqlValueCache(value = "username", type = OBJECT_IDENTIFIER)
    protected SqlIdentifierHolder<String> plrName;
    @SqlValueCache(value = "uuid", type = UUID_IDENTIFIER)
    protected SqlUUIDHolder uuid;

    @SqlValueCache(value = "passes_used", numberType = Integer.class, type = NUMBER_MODIFICATION)
    protected ConcurrentSqlNumberHolder<Integer> passesUsed;
    @SqlValueCache(value = "passes_amount", numberType = Integer.class, type = NUMBER_MODIFICATION)
    protected ConcurrentSqlNumberHolder<Integer> passesAmount;

    @SqlValueCache(value = "nickname", type = OBJECT_UPDATE)
    protected SqlValueHolder<String> nick;
    @SqlValueCache(value = "groupname", type = OBJECT_UPDATE)
    protected SqlValueHolder<String> groupName;

    @SqlValueCache(value = "kills", numberType = Integer.class, type = NUMBER_MODIFICATION)
    protected ConcurrentSqlNumberHolder<Integer> kills;
    @SqlValueCache(value = "deaths", numberType = Integer.class, type = NUMBER_MODIFICATION)
    protected ConcurrentSqlNumberHolder<Integer> deaths;

    protected Collection<SqlValueHolder<?>> valueHolders;
    protected QueryBuilder queryBuilder;

    public PlayerWrapperBase(SafeSql ssql) {
        try {
            this.valueHolders = getCacheBuilder().build(this, this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new AssertionError("Could not access some field of PlayerWrapperBase! This is probably a compile-time failure...Cannot recover.", e);
        }

        this.queryBuilder = new QueryBuilder(FULL_CENTRAL_USER_TABLE_NAME).addAll(valueHolders, true);

        this.sql = ssql;
    }

    //lazy init of CacheBuilder
    protected static SqlHolders.CacheBuilder getCacheBuilder() {
        if (BASE_CACHE_BUILDER == null) {
            BASE_CACHE_BUILDER = SqlHolders.processClassStructure(PlayerWrapperBase.class);
        }

        return BASE_CACHE_BUILDER;
    }

    /**
     * Initialises tables used by this class.
     *
     * @param ssql SafeSql to use to query the database.
     */
    public static void initTable(SafeSql ssql) {
        ssql.executeUpdate("CREATE DATABASE IF NOT EXISTS " + GameLib.XY_DB_NAME);
        ssql.executeUpdate("CREATE TABLE IF NOT EXISTS " + PlayerWrapper.FULL_CENTRAL_USER_TABLE_NAME + " (\n" +
                "\t`uuid` CHAR(36) NOT NULL COMMENT 'Mojang has their UUID at 32 chars plain, 36 chars with dashes.' COLLATE 'utf8_swedish_ci',\n" +
                "\t`username` VARCHAR(16) NOT NULL COMMENT 'The name of the user at the last time he was here' COLLATE 'utf8_swedish_ci',\n" +
                "\t`nickname` VARCHAR(20) NULL DEFAULT NULL COLLATE 'utf8_swedish_ci',\n" +
                "\t`groupname` VARCHAR(20) NULL DEFAULT NULL COLLATE 'utf8_swedish_ci',\n" +
                "\t`reg_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "\t`passes_amount` INT(11) UNSIGNED NOT NULL DEFAULT '0',\n" +
                "\t`passes_used` INT(11) UNSIGNED NOT NULL DEFAULT '0',\n" +
                "\t`kills` INT(11) UNSIGNED NOT NULL DEFAULT '0',\n" +
                "\t`deaths` INT(11) UNSIGNED NOT NULL DEFAULT '0',\n" +
                "\tPRIMARY KEY (`uuid`),\n" +
                "\tUNIQUE INDEX `nickname` (`nickname`),\n" +
                "\tINDEX `username` (`username`)\n" +
                ")\n" +
                "COLLATE='utf8_swedish_ci'\n" +
                "ENGINE=MyISAM;\n");
    }

    /**
     * Forces a full (re-)fetch of all data. This is equivalent to calling {@link PlayerWrapper#impFetch()} and {@link PlayerWrapper#xyFetch()}.
     */
    public void forceFullFetch() {
        this.xyFetch();
        this.impFetch();
    }

    /**
     * Writes all data currently stored in this object to database. This is equivalent to calling {@link PlayerWrapper#impFlush()} and
     * {@link PlayerWrapper#xyFlush()}.
     */
    public void forceFullFlush() {
        this.xyFlush();
        this.impFlush();
    }

    /**
     * Returns the group the wrapped player is in.
     *
     * @return {@link GroupData} the wrapped player is in.
     */
    public GroupData getGroup() {
        if (!this.xyFetched) {
            this.forceFullFetch();
        }

        return this.group;
    }

    /**
     * Checks if this {@link PlayerWrapper} has a permission. If a {@link org.bukkit.command.ConsoleCommandSender} or {@link org.bukkit.command.BlockCommandSender} was wrapped using
     * {@link PlayerWrapper#PlayerWrapper(org.bukkit.command.CommandSender, SafeSql)},
     * {@code true} will always be returned.
     * Regular expressions can be used, for example:
     * {@code game.admin.*} matches
     * {@code game.admin} and, for example,
     * {@code game.admin.awe}
     *
     * @param regex The permission to check for, RegEx are allowed.
     * @return if this {@link PlayerWrapper} is a {@link org.bukkit.command.ConsoleCommandSender} or {@link org.bukkit.command.BlockCommandSender} OR, if it's not, if this
     * {@link PlayerWrapper}'s group has a permission {@code regex}.
     */
    public boolean hasPermission(String regex) {
        return this.isConsole || this.getGroup().hasPermission(regex);
    }

    /**
     * Returns true if the wrapped player is online. Internally, this checks if a {@link org.bukkit.entity.Player} reference is being maintained by this object and, in
     * this case, checks its {@link org.bukkit.entity.Player#isOnline()} method.
     *
     * @return whether the player is online.
     */
    public boolean isOnline() {
        Player plr = this.plr();
        return plr != null && plr.isOnline();
    }

    /**
     * @return The name of the wrapped player. Ignores nicknames.
     * @see org.bukkit.entity.Player#getName()
     */
    public String name() {
        return this.plrName.getValue();
    }

    /**
     * @return The player wrapped by this {@link PlayerWrapper} OR null id the player is offline.
     */
    @Nullable
    public Player plr() {
        if (this.weakPlr == null) {
            return tryGetPlayer();
        }

        Player plr = this.weakPlr.get();
        if (plr == null) {
            return tryGetPlayer(); //throw new PlayerOfflineException();
        }
        return plr;
    }

    private Player tryGetPlayer() {
        Player plr = Bukkit.getPlayer(getUniqueId());

        if (plr == null) {
            return null;//throw new PlayerOfflineException();
        }

        this.weakPlr = new WeakReference<>(plr);

        return plr;
    }

    /**
     * Returns the unique ID of the player wrapped by this object, as in {@link org.bukkit.entity.Player#getUniqueId()}. If it is not obtainable (i.e. the player is
     * offline),
     * {@code null} will be returned.
     *
     * @return The Mojang UUID of the wrapped player.
     * @see org.bukkit.entity.Player#getUniqueId()
     * @see PlayerWrapper#plr()
     */
    @Nonnull
    public UUID getUniqueId() {
        return this.uuid.getValue();
    }

    /**
     * Method to be overwritten by implementations. Will be called in {@link PlayerWrapper#forceFullFetch()}. Shall (re-)fetch implementation data.
     *
     * @see PlayerWrapper#xyFetch()
     */
    protected void impFetch() {

    }

    /**
     * Method to be overwritten by implementations. Will be called in {@link PlayerWrapper#forceFullFlush()}. Shall save implementation data stored in
     * this object to database.
     *
     * @see PlayerWrapper#xyFlush()
     */
    protected void impFlush() {

    }

    /**
     * Re-fetches all data stored by native XYC implementation, i.e. everything included in {@link PlayerWrapper}.
     * It is recommended to call this async.
     */
    final void xyFetch() {
        this.xyFetched = true;

        if (!tryFetchByUUID() && !tryFetchByName() &&
                getUniqueId() != null && name() != null) {
            sql.safelyExecuteUpdate("INSERT INTO " + PlayerWrapperBase.FULL_CENTRAL_USER_TABLE_NAME + " SET username=?, uuid=?", name(), getUniqueId().toString());
            tryFetchByUUID();
        }

        if (getUniqueId() == null) { //This should actually never happen, except for really offline players...not even for them lol TO/DO: um
            throw new AssertionError("Could not find UUID! This is very bad..." +
                    "I will not attempt to fetch it for you because the name I got is not unique. (Thnx, Mojang, EvilSeph)");
        }

        this.group = GroupData.getByName(this.groupName.getValue(), getSql());
    }

    private boolean tryFetchByIdentifier(SqlIdentifierHolder<?> identifier) { //Returns true if it got the data
        try (QueryResult queryResult = this.queryBuilder.addUniqueIdentifier(identifier)
                .executeSelect(getSql(), false).vouchForResultSet()) {
            if (queryResult.rs().next()) {
                SqlHolders.updateFromResultSet(this.valueHolders, queryResult.rs());
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            getSql().formatAndPrintException(e, "PlayerWrapperBase#tryFetchByIdentifier");
            this.xyFetched = false;
            return false;
        } finally {
            this.queryBuilder.clearUniqueIdentifiers(); //Make sure we don't have any leftovers
        }
    }

    private boolean tryFetchByUUID() { //Returns true if it got the data
        return this.uuid.isFetched() && getUniqueId() != null && tryFetchByIdentifier(this.uuid);
    }

    private boolean tryFetchByName() { //Returns true if it got the data
        return plrName.getValue() != null && tryFetchByIdentifier(this.plrName);
    }

    /**
     * Writes all data stored in this object to database. It is advisable to this when the wrapped player leaves and also periodically, to be prepared
     * for server crashes.
     */
    final void xyFlush() {
        if (!this.xyChanged) {
            return;
        }

        try {
            this.queryBuilder.addUniqueIdentifier(this.uuid)
                    .executeTrueUpdate(getSql());

            this.xyChanged = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean select(@Nonnull SqlValueHolder<?> holder) {
        forceFullFetch();
        return true;
    }

    @Override
    public void registerChange(@Nonnull SqlValueHolder<?> holder) {
        this.xyChanged = true; //TO/DO only update actually changed values! :)
    }

    /**
     * Checks whether this object has a valid UUID stored, either by it being passed
     * to a constructor or by it being fetched from a database.
     * If this returns {@code false}, {@link #getUniqueId()} will try to fetch the UUID from database, but can
     * return {@code null} if an error occurs.
     *
     * @return whether this object has an UUID stored.
     */
    public boolean hasUniqueId() {
        return this.uuid.isFetched();
    }

    @Override
    public String toString() {
        return getClass().getName() + "{name=" + this.name() + ", uuid=" + (hasUniqueId() ? getUniqueId() : "unknown") + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerWrapperBase)) return false;

        PlayerWrapperBase that = (PlayerWrapperBase) o;

        return sql.equals(that.sql) && uuid.equals(that.uuid);

    }

    @Override
    public int hashCode() {
        int result = sql.hashCode();
        result = 31 * result + uuid.hashCode();
        return result;
    }

    protected <V> V lockedRead(SqlValueHolder<V> holder) {
        return holder.getValue();
    }

    protected <N extends Number> void lockedModify(ConcurrentSqlNumberHolder<N> holder, N modifier) {
        holder.modify(modifier);
    }

    public SafeSql getSql() {
        return this.sql;
    }
}
