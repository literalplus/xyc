/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.games.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.Metadatable;

import io.github.xxyy.common.games.GameLib;
import io.github.xxyy.common.sql.QueryResult;
import io.github.xxyy.common.sql.SafeSql;
import io.github.xxyy.common.sql.builder.ConcurrentSqlNumberHolder;
import io.github.xxyy.common.sql.builder.QueryBuilder;
import io.github.xxyy.common.sql.builder.SqlHolders;
import io.github.xxyy.common.sql.builder.SqlIdentifierHolder;
import io.github.xxyy.common.sql.builder.SqlUUIDHolder;
import io.github.xxyy.common.sql.builder.SqlValueHolder;
import io.github.xxyy.common.sql.builder.annotation.SqlValueCache;
import io.github.xxyy.lib.intellij_annotations.NotNull;
import io.github.xxyy.lib.intellij_annotations.Nullable;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static io.github.xxyy.common.sql.builder.annotation.SqlValueCache.Type.NUMBER_MODIFICATION;
import static io.github.xxyy.common.sql.builder.annotation.SqlValueCache.Type.OBJECT_IDENTIFIER;
import static io.github.xxyy.common.sql.builder.annotation.SqlValueCache.Type.OBJECT_UPDATE;
import static io.github.xxyy.common.sql.builder.annotation.SqlValueCache.Type.UUID_IDENTIFIER;

/**
 * Base stuff of PlayerWrapper.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2.4.14
 */
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

    /**
     * Lock used to lock database operations on this wrapper.
     * The default implementations of {@link #xyFetch()} and {@link #xyFlush()} use this.
     */
    protected final ReentrantReadWriteLock databaseLock = new ReentrantReadWriteLock(false);

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

    /**
     * Forces a full (re-)fetch of all data. This is equivalent to calling {@link io.github.xxyy.common.games.data.PlayerWrapper#impFetch()} and {@link io.github.xxyy.common.games.data.PlayerWrapper#xyFetch()}.
     */
    public void forceFullFetch() {
//        boolean relock = false;
//        if (this.databaseLock.getReadHoldCount() > 0) { //UNLOCK READ LOCKS - AVOID DEADLOCKS
//            try {
//                this.databaseLock.readLock().unlock();
//                relock = true;
//            } catch (IllegalMonitorStateException e) {
//                SafeSql.getLogger().warning("Could not unlock a ReadLock for " + this + "! - This thread does probably not hold that lock. Enjoy the following thread dump:");
//                ThreadHelper.printThreadDump(SafeSql.getLogger()); //FIX ME debug info
//                SafeSql.getLogger().warning("If the server stops responding now, you know what caused it at least :) [A deadlock because of some read lock not conforming to a policy]");
//            }
//        } //This code is probably a very wrong approach at locking, so let's just omit it until it causes issues


        this.databaseLock.writeLock().lock(); //And that, kids, is why you don't do anything with PlayerWrapper on the main thread, if avoidable.

        try {
            this.xyFetch();
            this.impFetch();
        } finally {
            this.databaseLock.writeLock().unlock();
        }


//        if (relock) { //RE-LOCK READ LOCKS
//            this.databaseLock.readLock().lock();
//        }
    }

    /**
     * Writes all data currently stored in this object to database. This is equivalent to calling {@link io.github.xxyy.common.games.data.PlayerWrapper#impFlush()} and
     * {@link io.github.xxyy.common.games.data.PlayerWrapper#xyFlush()}.
     */
    public void forceFullFlush() {
        this.databaseLock.writeLock().lock();

        try {
            this.xyFlush();
            this.impFlush();
        } finally {
            this.databaseLock.writeLock().unlock();
        }
    }

    /**
     * Returns the group the wrapped player is in.
     *
     * @return {@link io.github.xxyy.common.games.data.GroupData} the wrapped player is in.
     */
    public GroupData getGroup() {
        if (!this.xyFetched) {
            this.forceFullFetch();
        }

        this.databaseLock.readLock().lock();
        try {
            return this.group;
        } finally {
            this.databaseLock.readLock().unlock();
        }
    }

    /**
     * Checks if this {@link io.github.xxyy.common.games.data.PlayerWrapper} has a permission. If a {@link org.bukkit.command.ConsoleCommandSender} or {@link org.bukkit.command.BlockCommandSender} was wrapped using
     * {@link io.github.xxyy.common.games.data.PlayerWrapper#PlayerWrapper(org.bukkit.command.CommandSender, io.github.xxyy.common.sql.SafeSql)},
     * {@code true} will always be returned.
     * Regular expressions can be used, for example:
     * {@code game.admin.*} matches
     * {@code game.admin} and, for example,
     * {@code game.admin.awe}
     *
     * @param regex The permission to check for, RegEx are allowed.
     * @return if this {@link io.github.xxyy.common.games.data.PlayerWrapper} is a {@link org.bukkit.command.ConsoleCommandSender} or {@link org.bukkit.command.BlockCommandSender} OR, if it's not, if this
     * {@link io.github.xxyy.common.games.data.PlayerWrapper}'s group has a permission {@code regex}.
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
     * @return The player wrapped by this {@link io.github.xxyy.common.games.data.PlayerWrapper} OR null id the player is offline.
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
        this.uuid.fetchIfNecessary(); //Avoid deadlocks

        this.databaseLock.readLock().lock();

        try {
            Player plr = Bukkit.getPlayer(getUniqueId());

            if (plr == null) {
                return null;//throw new PlayerOfflineException();
            }

            this.weakPlr = new WeakReference<>(plr);

            return plr;
        } finally {
            this.databaseLock.readLock().unlock();
        }
    }

    /**
     * Returns the unique ID of the player wrapped by this object, as in {@link org.bukkit.entity.Player#getUniqueId()}. If it is not obtainable (i.e. the player is
     * offline),
     * {@code null} will be returned.
     *
     * @return The Mojang UUID of the wrapped player.
     * @see org.bukkit.entity.Player#getUniqueId()
     * @see io.github.xxyy.common.games.data.PlayerWrapper#plr()
     */
    @NotNull
    public UUID getUniqueId() {
        return this.uuid.getValue();
    }

    /**
     * Method to be overwritten by implementations. Will be called in {@link io.github.xxyy.common.games.data.PlayerWrapper#forceFullFetch()}. Shall (re-)fetch implementation data.
     * Implementations are expected to lock their database I/O with {@link #databaseLock}.
     *
     * @see io.github.xxyy.common.games.data.PlayerWrapper#xyFetch()
     */
    protected void impFetch() {

    }

    /**
     * Method to be overwritten by implementations. Will be called in {@link io.github.xxyy.common.games.data.PlayerWrapper#forceFullFlush()}. Shall save implementation data stored in
     * this object to database.
     * Implementations are expected to lock their database I/O with {@link #databaseLock}.
     *
     * @see io.github.xxyy.common.games.data.PlayerWrapper#xyFlush()
     */
    protected void impFlush() {

    }

    /**
     * Re-fetches all data stored by native XYC implementation, i.e. everything included in {@link io.github.xxyy.common.games.data.PlayerWrapper}.
     * It is recommended to call this async.
     */
    final void xyFetch() {
        this.databaseLock.writeLock().lock(); //Locks twice for fullFetch - Should not hurt though.

        try {
            this.xyFetched = true;

            if (!tryFetchByUUID() && !tryFetchByName() &&
                    getUniqueId() != null && name() != null) {
                sql.safelyExecuteUpdate("INSERT INTO " + PlayerWrapperBase.FULL_CENTRAL_USER_TABLE_NAME + " SET username=?, uuid=?", name(), getUniqueId().toString());
                tryFetchByUUID();
            }

            if (getUniqueId() == null) { //This should actually never happen, except for really offline players...not even for them lol TODO: um
                throw new AssertionError("Could not find UUID! This is very bad..." +
                        "I will not attempt to fetch it for you because the name I got is not unique. (Thnx, Mojang, EvilSeph)");
            }

            this.group = GroupData.getByName(this.groupName.getValue(), getSql());
        } finally {
            this.databaseLock.writeLock().unlock();
        }
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

        this.databaseLock.writeLock().lock(); //Locks twice for fullFlush - Should not hurt though.

        try {
            this.queryBuilder.addUniqueIdentifier(this.uuid)
                    .executeTrueUpdate(getSql());

            this.xyChanged = false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.databaseLock.writeLock().unlock();
        }
    }

    @Override
    public boolean select(@NotNull SqlValueHolder<?> holder) {
        forceFullFetch();
        return true;
    }

    @Override
    public void registerChange(@NotNull SqlValueHolder<?> holder) {
        this.xyChanged = true; //TODO only update actually changed values! :)
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

    protected <V> V lockedRead(SqlValueHolder<V> holder) {
        holder.fetchIfNecessary(); //Need to do this outside the locking part in order to avoid a deadlock

        this.databaseLock.readLock().lock();

        try {
            return holder.getValue();
        } finally {
            this.databaseLock.readLock().unlock();
        }
    }

    protected <N extends Number> void lockedModify(ConcurrentSqlNumberHolder<N> holder, N modifier) {
        holder.fetchIfNecessary();

        this.databaseLock.readLock().lock();

        try {
            holder.modify(modifier);
        } finally {
            this.databaseLock.readLock().unlock();
        }
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

    public SafeSql getSql() {
        return this.sql;
    }
}
