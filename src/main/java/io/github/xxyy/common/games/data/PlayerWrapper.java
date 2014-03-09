package io.github.xxyy.common.games.data;

import io.github.xxyy.common.games.GameLib;
import io.github.xxyy.common.lib.com.mojang.api.profiles.HttpProfileRepository;
import io.github.xxyy.common.lib.com.mojang.api.profiles.Profile;
import io.github.xxyy.common.lib.com.mojang.api.profiles.ProfileCriteria;
import io.github.xxyy.common.sql.QueryResult;
import io.github.xxyy.common.sql.SafeSql;
import io.github.xxyy.common.util.CommandHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Wrapper for {@link Player} to be used by games to store data. The default implementation stores some common data in SQL table
 * {@value PlayerWrapper#FULL_XY_TABLE_NAME}.
 *
 * @author xxyy98
 */
public abstract class PlayerWrapper<T>//TODO implement Player?
{//TODO daily passes w/ extra method to call in onJoin @ lobby

    /**
     * Full table name used to store common data. Defaults to {@value GameLib#XY_DB_NAME}.game_users.
     */
    public static final String FULL_XY_TABLE_NAME = GameLib.XY_DB_NAME + ".game_users";
    public static final String FULL_CENTRAL_USER_TABLE_NAME = GameLib.CENTRAL_DB_NAME + ".user";
    public static final HttpProfileRepository HTTP_PROFILE_REPOSITORY = new HttpProfileRepository();
    protected int passesUsed = 0;
    protected String nick = null;
    protected GroupData group;
    protected int passesAmount = 0;
    protected boolean xyFetched = false;
    protected boolean impFetched = false;
    protected String plrName = null;
    protected boolean xyChanged = false;
    protected UUID uuid = null;
    private WeakReference<Player> weakPlr = null;
    protected boolean isConsole = false;
    protected final SafeSql ssql;

    /**
     * Gets a wrapper for a {@link CommandSender}. If it's a {@link ConsoleCommandSender}, internal things will happen. Use this method if you want to
     * check permissions for {@link CommandSender}s!
     * <p/>
     * <b>Implementations must implement a constructor with the exact same arguments!!</b>
     *
     * @param sender A {@link CommandSender}. (must be {@link Player} or {@link ConsoleCommandSender} or {@link BlockCommandSender})
     * @param ssql   SafeSql to use for storing the object.
     * @throws ClassCastException If <code>sender</code> is not any of the expected types.
     */
    protected PlayerWrapper(CommandSender sender, SafeSql ssql) {
        if (sender instanceof Player) {
            this.weakPlr = new WeakReference<>((Player) sender);
            this.plrName = sender.getName();
            this.uuid = ((Player) sender).getUniqueId();
        } else {
            if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
                this.plrName = sender.getName();
                this.isConsole = true;
            } else {
                throw new ClassCastException("That is neither a Player nor a Console nor a CommandBlock.");
            }
        }
        this.ssql = ssql;
    }

    /**
     * Wraps a {@link Player}.
     * <p/>
     * <b>Implementations must implement a constructor with the exact same arguments!!</b>
     *
     * @param plr  Player to wrap.
     * @param ssql SafeSql to use for storing the object.
     * @deprecated kinda useless - use {@link PlayerWrapper#PlayerWrapper(org.bukkit.command.CommandSender, io.github.xxyy.common.sql.SafeSql)}
     * instead.
     */
    @Deprecated
    protected PlayerWrapper(Player plr, SafeSql ssql) {
        this.weakPlr = new WeakReference<>(plr);
        this.plrName = plr.getName();
        this.uuid = plr.getUniqueId();
        this.ssql = ssql;
    }

    /**
     * Wraps a player by name. Use this if you don't have access to a Player instance; If the player is not online, {@link PlayerWrapper#plr()} will
     * return null.
     * <b>Notice:</b> If you have a CommandSender, use {@link PlayerWrapper#PlayerWrapper(CommandSender, SafeSql)} instead - that constructor also allows for
     * CONSOLE {@link PlayerWrapper#hasPermission(String)} checks.
     * <p/>
     * <b>Implementations must implement a constructor with the exact same arguments!!</b>
     *
     * @param plrName Player to wrap (by name)
     * @param ssql    SafeSql to use for storing the object.
     */
    protected PlayerWrapper(String plrName, SafeSql ssql) {
        this.plrName = plrName;
        this.ssql = ssql;
    }

    /**
     * Wraps a player by UUID. Use this if you don't have access to a Player instance; If the player is not online, {@link PlayerWrapper#plr()} will
     * return null.
     * <b>Notice:</b> If you have a CommandSender, use {@link PlayerWrapper#PlayerWrapper(CommandSender, SafeSql)} instead - that constructor also allows for
     * CONSOLE {@link PlayerWrapper#hasPermission(String)} checks.
     * <p/>
     * <b>Implementations must implement a constructor with the exact same arguments!!</b>
     *
     * Only use this if you don't have anything else, stuff will break very badly if there's no database entry for this UUID.
     *
     * @param uuid UUID of the player to wrap
     * @param ssql SafeSql to use for storing the object.
     */
    protected PlayerWrapper(UUID uuid, SafeSql ssql) {
        this.ssql = ssql;
        this.uuid = uuid;

        this.xyFetch();
    }

    /**
     * Adds a pass use safely, re-fetching passes and saving immediately afterwards.
     */
    public void addPassUse() {
        this.refetchPasses();//safety!
        this.passesUsed++;
        this.passesAmount--;
        if (this.passesAmount < 0) {
            this.passesAmount = 0;
        }
        this.xyChanged = true;
        this.xyFlush(); //safety!
    }

    /**
     * Revokes a pass use safely, re-fetching passes and saving immediately afterwards. This is the reverse operation of
     * {@link PlayerWrapper#addPassUse()}
     */
    public void revokePassUse() {
        this.refetchPasses();//safety!
        this.passesUsed++;
        this.passesAmount--;
        if (this.passesAmount < 0) {
            this.passesAmount = 0;
        }
        this.xyChanged = true;
        this.xyFlush(); //safety!
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
     * @return SafeSql used by this object to obtain and save data.
     */
    public SafeSql getSql() {
        return ssql;
    }

    /**
     * @return The chat color of this player, using the one of the "default" group if a nickname is enabled.
     */
    public String getChatColor() {
        if (this.getNick() != null) {
            return GroupData.getByName("default", ssql).getChatColor();
        }
        return this.getGroup().getChatColor();
    }

    /**
     * Parses a colorified name for the player represented by this wrapper. Primarily intended for chat, rankings and the TAB list.
     *
     * @param sixteenCharLimit Whether to limit the output to 16 characters (I did not make that limitation!)
     * @return The colorified display name of this player, using the color of the "default" group if a nickname is enabled.
     */
    public String getColorizedDisplayName(boolean sixteenCharLimit) {
        String nameColor = this.getGroup().getNameColor();
        if (this.getNick() != null) {
            nameColor = GroupData.getByName("default", ssql).getNameColor();
        }
        if (!sixteenCharLimit) {
            return nameColor.concat(this.getDisplayName());
        }
        return CommandHelper.sixteenCharColorize(
                this.getDisplayName(),
                nameColor);
    }

    /**
     * Returns the display name of the player represented by this wrapper. The name returned will *NOT* be colorified. This will return the name of
     * the player or his nickname, if he/she has chosen one.
     *
     * @return The display name for the wrapped player, either his/her real name or his/her nickname.
     */
    public String getDisplayName() {
        if (!this.xyFetched) {
            this.xyFetch();
        }
        if (this.nick != null) {
            return this.nick;
        }
        return this.plrName;
    }

    /**
     * Returns the group the wrapped player is in.
     *
     * @return {@link GroupData} the wrapped player is in.
     */
    public GroupData getGroup() {
        if (!this.xyFetched) {
            this.xyFetch();
        }
        return this.group;
    }

    /**
     * Returns the nickname the wrapped player has chosen or
     * <code>null</code> if he/she has not (yet) chosen one. If the player has chosen a nickname, please try to use it as much as possible.
     *
     * @return nickname the wrapped player has chosen or <code>null</code> if none.
     * @see PlayerWrapper#getDisplayName()
     * @see PlayerWrapper#getColorizedDisplayName(boolean)
     */
    public String getNick() {
        if (!this.xyFetched) {
            this.xyFetch();
        }
        return this.nick;
    }

    /**
     * Returns the amount of passes currently owned by the wrapped player. Passes are some kind of currency that is intended to be used up by games
     * for premium functionality.
     *
     * @return The amount of passes currently owned by the wrapped player.
     * @see PlayerWrapper#addPassUse()
     * @see PlayerWrapper#getPassesUsed()
     */
    public int getPassesAmount() {
        if (!this.xyFetched) {
            this.xyFetch();
        }
        return this.passesAmount;
    }

    /**
     * Returns how many passes the wrapped player has used up all-time. Passes are some kind of currency that is intended to be used up by games for
     * premium functionality.
     *
     * @return The all-time count of passes used by the wrapped player.
     */
    public int getPassesUsed() {
        if (!this.xyFetched) {
            this.xyFetch();
        }
        return this.passesUsed;
    }

    /**
     * Checks if this {@link PlayerWrapper} has a permission. If a {@link ConsoleCommandSender} or {@link BlockCommandSender} was wrapped using
     * {@link PlayerWrapper#PlayerWrapper(CommandSender, SafeSql)},
     * <code>true</code> will always be returned.
     * <p/>
     * Regular expressions can be used, for example:
     * <code>game.admin.*</code> matches
     * <code>game.admin</code> and, for example,
     * <code>game.admin.awe</code>
     *
     * @param regex The permission to check for, RegEx are allowed.
     * @return if this {@link PlayerWrapper} is a {@link ConsoleCommandSender} or {@link BlockCommandSender} OR, if it's not, if this
     * {@link PlayerWrapper}'s group has a permission <code>regex</code>.
     */
    public boolean hasPermission(String regex) {
        return this.isConsole || this.getGroup().hasPermission(regex);
    }

    /**
     * Returns true if the wrapped player is online. Internally, this checks if a {@link Player} reference is being maintained by this object and, in
     * this case, checks its {@link Player#isOnline()} method.
     *
     * @return whether the player is online.
     */
    public boolean isOnline() {
        Player plr = this.plr();
        return plr != null && plr.isOnline();
    }

    /**
     * @return The name of the wrapped player. Ignores nicknames.
     * @see Player#getName()
     */
    public String name() {
        return this.plrName;
    }

    /**
     * @return The player wrapped by this {@link PlayerWrapper} OR null id the player is offline.
     */
    public Player plr() {
        if (this.weakPlr == null) {
            Player plr = Bukkit.getPlayerExact(this.plrName);
            if (plr == null) {
                return null;//throw new PlayerOfflineException();
            }
            this.weakPlr = new WeakReference<>(plr);
            this.uuid = plr.getUniqueId();
            return plr;
        }
        Player plr = this.weakPlr.get();
        if (plr == null) {
            return null; //throw new PlayerOfflineException();
        }
        return plr;
    }

    /**
     * Returns the unique ID of the player wrapped by this object, as in {@link Player#getUniqueId()}. If it is not obtainable (i.e. the player is
     * offline),
     * <code>null</code> will be returned.
     *
     * @return The Mojang UUID of the wrapped player.
     * @see Player#getUniqueId()
     * @see PlayerWrapper#plr()
     */
    public UUID getUniqueId() {
        if (this.uuid == null) {
            Player plr = plr();

            if (plr == null) {
                this.xyFetch(); //Try and get UUID from SQL
            } else {
                this.uuid = plr.getUniqueId();
            }
        }

        return this.uuid;
    }

    /**
     * Re-fetches only the amount of passes owned and passes used by the wrapped player. This is for safety and accuracy purposes, and should be
     * invoked every once-in-a-while by games to prevent players from using their passes twice.
     */
    public void refetchPasses() {
        if (!this.xyFetched) {
            this.xyFetch();
            return;//no need to fetch again ;)
        }
        Validate.notNull(getUniqueId(), "Cannot refetch passes, no UUID!");

        try (QueryResult queryResult = ssql.executeQueryWithResult("SELECT passes_amount, passes_used FROM "
                + PlayerWrapper.FULL_CENTRAL_USER_TABLE_NAME + " WHERE uuid=?", getUniqueId().toString()).assertHasResultSet()) {
            if (!queryResult.rs().next()) {
                ssql.safelyExecuteUpdate("INSERT INTO " + PlayerWrapper.FULL_CENTRAL_USER_TABLE_NAME +
                        " SET username=?, uuid=?", this.plrName, getUniqueId().toString());
                return;
            }
            this.passesAmount = queryResult.rs().getInt("passes_amount");
            this.passesUsed = queryResult.rs().getInt("passes_used");
        } catch (SQLException e) {
            ssql.formatAndPrintException(e, "PlayerWrapper#refetchPasses()");
            GameLib.plugs.get(0).setError("Exception when refetching passes. This is an error.", "PlayerWrapper#refetchPasses()");
        }
    }

    /**
     * [0] is the column used to identify this player, either {@code username} or {@code uuid}.
     * [1] is the value used to identify this player.
     *
     * @return Parameters for a SQL WHERE clause.
     */
    protected String[] getWhereClauseParameters() {
        if (getUniqueId() == null) {
            return new String[]{" WHERE username=?", this.plrName};
        }

        return new String[]{" WHERE uuid=?", getUniqueId().toString()};
    }

    /**
     * Sets the group of the wrapped player. {@link GroupData} objects can be obtained using the static factory method
     * {@link GroupData#getByName(java.lang.String, SafeSql)}
     *
     * @param group New group for the wrapped player.
     */
    public void setGroup(GroupData group) {
        this.regXyChange();
        this.group = group;
    }

    /**
     * Sets the nickname of this player.
     *
     * @param nick New nickname for the wrapped player or <code>null</code> to disable.
     * @see PlayerWrapper#getNick()
     * @see PlayerWrapper#getDisplayName()
     */
    public void setNick(String nick) {
        this.regXyChange();
        this.nick = nick;
    }

    /**
     * Summons passes from the magical void. Use negative values to withdraw passes.
     *
     * @param amount Can also be negative!
     */
    public void summonPasses(int amount) {
        this.refetchPasses();//safety!
        this.passesAmount += amount;
        this.xyChanged = true;
        this.xyFlush();//safety!
    }

    /**
     * Method to be overwritten by implementations. Will be called in {@link PlayerWrapper#forceFullFetch()}. Shall (re-)fetch implementation data.
     *
     * @see PlayerWrapper#xyFetch()
     */
    protected abstract void impFetch();

    /**
     * Method to be overwritten by implementations. Will be called in {@link PlayerWrapper#forceFullFlush()}. Shall save implementation data stored in
     * this object to database.
     *
     * @see PlayerWrapper#xyFlush()
     */
    protected abstract void impFlush();

    private void regXyChange() {
        if (!this.xyFetched) {
            this.xyFetch();
        }
        this.xyChanged = true;
    }

    /**
     * Re-fetches all data stored by native XYC implementation, i.e. everything included in {@link PlayerWrapper}.
     */
    final void xyFetch() {
        this.xyFetched = true;

        if (!tryFetchByUUID() && !tryFetchByName() &&
                getUniqueId() != null && name() != null) {
            ssql.safelyExecuteUpdate("INSERT INTO " + PlayerWrapper.FULL_CENTRAL_USER_TABLE_NAME + " SET username=?, uuid=?", name(), getUniqueId());
        }

        if (getUniqueId() == null) {
            if (name() != null) {
                Profile[] matchedProfiles = HTTP_PROFILE_REPOSITORY.findProfilesByCriteria(new ProfileCriteria(name(), "minecraft"));

                if(matchedProfiles.length == 1){ //Which Profile should we choose if there's multiple? So shut up.
                    this.uuid = io.github.xxyy.common.lib.net.minecraft.server.UtilUUID.getFromString(matchedProfiles[0].getId());
                    ssql.safelyExecuteUpdate("INSERT INTO "+ PlayerWrapper.FULL_CENTRAL_USER_TABLE_NAME+" SET username=?, uuid=? " +
                            "ON DUPLICATE KEY UPDATE username=?, uuid=?", name(), this.uuid, name(), this.uuid);
                    return;
                }
            }

            throw new IllegalStateException("Could not fetch UUID! Can not continue for name=" + name());
        }
    }

    private boolean tryFetchByArgs(String column, String value) { //Returns true if it got the data
        try (QueryResult queryResult = ssql.executeQueryWithResult("SELECT username, passes_amount, passes_used, "
                + "nickname,groupname,uuid,username FROM " + PlayerWrapper.FULL_CENTRAL_USER_TABLE_NAME + " WHERE " + column + "=?", value)) {
            if (queryResult.rs().next()) {
                this.passesAmount = queryResult.rs().getInt("passes_amount");
                this.passesUsed = queryResult.rs().getInt("passes_used");
                this.group = GroupData.getByName(queryResult.rs().getString("groupname"), ssql);
                this.nick = queryResult.rs().getString("nickname");
                this.uuid = io.github.xxyy.common.lib.net.minecraft.server.UtilUUID.getFromString(queryResult.rs().getString("uuid"));
                this.plrName = queryResult.rs().getString("username");
                this.xyFetched = true;
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            ssql.formatAndPrintException(e, "PlayerWrapper#tryFetchByArgs");
            this.xyFetched = false;
            return false;
        }
    }

    private boolean tryFetchByUUID() { //Returns true if it got the data
        return getUniqueId() != null && tryFetchByArgs("uuid", getUniqueId().toString());

    }

    private boolean tryFetchByName() { //Returns true if it got the data
        return plrName != null && tryFetchByArgs("username", name());
    }

    /**
     * Writes all data stored in this object to database. It is advisable to this when the wrapped player leaves and also periodically, to be prepared
     * for server crashes.
     */
    final void xyFlush() {
        if (!this.xyChanged) {
            return;
        }

        ssql.safelyExecuteUpdate("UPDATE " + PlayerWrapper.FULL_XY_TABLE_NAME + " SET "
                + "passes_amount=" + this.passesAmount + ","
                + "passes_used=" + this.passesUsed + ","
                + "nickname=?, groupname=? "
                + "WHERE uuid=?",
                this.nick, this.group.getName(), getUniqueId()); //If we have fetched, there must be an UUID
    }

    /**
     * Returns the real user name for a provided String.
     *
     * @param name Name to seek.
     * @return Name of the player currently owning this nickname or <code>name</code> if <code>name</code> is a real name or <code>null</code> if
     * there is no such user.
     */
    public static String getAnyName(String name) {
        try (QueryResult queryResult = GameLib.getSql().executeQueryWithResult(
                "SELECT username FROM " + PlayerWrapper.FULL_CENTRAL_USER_TABLE_NAME + " WHERE username=? OR nickname=?", name, name).assertHasResultSet()) {
            if (queryResult.rs().next()) {
                return queryResult.rs().getString("username");
            }
        } catch (SQLException e) {
            GameLib.getSql().formatAndPrintException(e, "PlayerWrapper#getNameByNick");
        }
        return null;
    }

    /**
     * Gets the real name of an user by their nickname.
     *
     * @param nick Nickname to seek.
     * @return Name of the player currently owning this nickname or <code>null</code> if this nickname has not been registered.
     */
    public static String getNameByNick(String nick) {
        try (QueryResult queryResult = GameLib.getSql().executeQueryWithResult(
                "SELECT username FROM " + PlayerWrapper.FULL_CENTRAL_USER_TABLE_NAME + " WHERE nickname=?", nick).assertHasResultSet()) {
            if (queryResult.rs().next()) {
                return queryResult.rs().getString("username");
            }
        } catch (SQLException e) {
            GameLib.getSql().formatAndPrintException(e, "PlayerWrapper#getNameByNick");
        }
        return null;
    }

    /**
     * Initialises tables used by this class. This executes the following SQL:
     * <code>CREATE DATABASE IF NOT EXISTS</code> {@value GameLib#XY_DB_NAME}
     * <code>CREATE TABLE IF NOT EXISTS</code> {@value PlayerWrapper#FULL_XY_TABLE_NAME}
     * `username` VARCHAR(30) NOT NULL,
     * `passes_amount` INT UNSIGNED NOT NULL DEFAULT '0',
     * `passes_used` INT UNSIGNED NOT NULL DEFAULT '0',
     * `nickname` VARCHAR(30) DEFAULT NULL,
     * `groupname` VARCHAR(30) NOT NULL DEFAULT 'default',
     * PRIMARY KEY (`username`)
     * COLLATE='utf8_unicode_ci'
     * ENGINE=MyISAM;</code>
     *
     * @param ssql SafeSql to use to query the database.
     */
    public static void initTable(SafeSql ssql) {
        ssql.executeUpdate("CREATE DATABASE IF NOT EXISTS " + GameLib.XY_DB_NAME);
        ssql.executeUpdate("CREATE TABLE IF NOT EXISTS " + PlayerWrapper.FULL_XY_TABLE_NAME + " (\n"
                + "    `username` VARCHAR(30) NOT NULL,\n"
                + "    `passes_amount` INT UNSIGNED NOT NULL DEFAULT '0',\n"
                + "    `passes_used` INT UNSIGNED NOT NULL DEFAULT '0',\n"
                + "    `nickname` VARCHAR(30) DEFAULT NULL,\n"
                + "    `groupname` VARCHAR(30) NOT NULL DEFAULT 'default',\n"
                + "    PRIMARY KEY (`username`)\n"
                + ")\n"
                + "COLLATE='utf8_unicode_ci'\n"
                + "ENGINE=MyISAM;");
    }
}
