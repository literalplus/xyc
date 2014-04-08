package io.github.xxyy.common.games.data;

import io.github.xxyy.common.games.GameLib;
import io.github.xxyy.common.sql.QueryResult;
import io.github.xxyy.common.sql.SafeSql;
import io.github.xxyy.common.sql.builder.QueryBuilder;
import io.github.xxyy.common.sql.builder.QuerySnapshot;
import io.github.xxyy.common.util.CommandHelper;
import lombok.NonNull;
import org.apache.commons.lang.Validate;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Wrapper for {@link Player} to be used by games to store data. The default implementation stores some common data in SQL table
 * {@value PlayerWrapper#FULL_XY_TABLE_NAME}.
 *
 * @author xxyy98
 */
public abstract class PlayerWrapper<T> extends PlayerWrapperBase//TODO implement Player? //TODO Why is there a T here?
{

    public static final UUID CONSOLE_UUID = UUID.fromString("084b992e-5705-411a-9be0-9e91413fb23a");
    private QueryBuilder passQueryBuilder;

    /**
     * Gets a wrapper for a {@link CommandSender}. If it's a {@link ConsoleCommandSender}, internal things will happen. Use this method if you want to
     * check permissions for {@link CommandSender}s!
     * <b>Implementations must implement a constructor with the exact same arguments!!</b>
     *
     * @param sender A {@link CommandSender}. (must be {@link Player} or {@link ConsoleCommandSender} or {@link BlockCommandSender})
     * @param ssql   SafeSql to use for storing the object.
     * @throws ClassCastException If {@code sender} is not any of the expected types.
     */
    protected PlayerWrapper(CommandSender sender, SafeSql ssql) {
        super(ssql);

        if (sender instanceof Player) {
            this.weakPlr = new WeakReference<>((Player) sender);
            this.plrName.updateValue(sender.getName());
            this.uuid.updateValue(((Player) sender).getUniqueId());
        } else {
            if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
                this.plrName.updateValue(sender.getName());
                this.uuid.updateValue(CONSOLE_UUID);
                this.isConsole = true;
            } else {
                throw new ClassCastException("That is neither a Player nor a Console nor a CommandBlock.");
            }
        }
    }

    /**
     * Wraps a player by name. Use this if you don't have access to a Player instance; If the player is not online, {@link PlayerWrapper#plr()} will
     * return null.
     * <b>Notice:</b> If you have a CommandSender, use {@link PlayerWrapper#PlayerWrapper(CommandSender, SafeSql)} instead - that constructor also allows for
     * CONSOLE {@link PlayerWrapper#hasPermission(String)} checks.
     *
     * <b>Implementations must implement a constructor wi"th the exact same arguments!!</b>
     *
     * @param plrName Player to wrap (by name)
     * @param ssql    SafeSql to use for storing the object.
     */
    protected PlayerWrapper(String plrName, SafeSql ssql) {
        super(ssql);
        this.plrName.updateValue(plrName);
    }

    /**
     * Wraps a player by UUID. Use this if you don't have access to a Player instance; If the player is not online, {@link PlayerWrapper#plr()} will
     * return null.
     * <b>Notice:</b> If you have a CommandSender, use {@link PlayerWrapper#PlayerWrapper(CommandSender, SafeSql)} instead - that constructor also allows for
     * CONSOLE {@link PlayerWrapper#hasPermission(String)} checks.
     *
     * <b>Implementations must implement a constructor with the exact same arguments!!</b>
     *
     * Only use this if you don't have anything else, stuff will break very badly if there's no database entry for this UUID.
     *
     * @param uuid UUID of the player to wrap
     * @param plrName Name of the player to wrap (May be null if not available)
     * @param ssql SafeSql to use for storing the object.
     */
    protected PlayerWrapper(@NonNull UUID uuid, @Nullable String plrName, @NonNull SafeSql ssql) {
        this(plrName, ssql);
        this.uuid.updateValue(uuid);

        this.xyFetch();
    }

    /**
     * Adds a pass use safely, re-fetching passes and saving immediately afterwards.
     *
     * @return Whether the pass could be used.
     * @deprecated Magic value. Use {@link #modifyPassesAmount(int)}
     */
    @Deprecated
    public boolean addPassUse() {
        return modifyPassesAmount(-1);
    }

    /**
     * Modifies the amount of passes the wrapped player has.
     * Also modifies the 'used passes' stat by the negated modifier (i.e -2 for modifier=2).
     *
     * @param modifier Value to add to the amount of passes possessed by the wrapped player.
     * @return Whether the operation succeeded.
     */
    public boolean modifyPassesAmount(int modifier) {
//        this.refetchPasses();//safety! // No longer needed since only a modifier is being written

        if (modifier < 0 && ((this.passesAmount.getValue() + modifier) < 0)) {
            return false; //pls stahp haxe
        }

        this.passesUsed.modify(-modifier);
        this.passesAmount.modify(modifier);

        this.xyChanged = true;
        this.xyFlush(); //safety!

        return true;
    }

    /**
     * Revokes a pass use safely, re-fetching passes and saving immediately afterwards. This is the reverse operation of
     * {@link PlayerWrapper#addPassUse()}.
     *
     * @deprecated Magic value. Use {@link #modifyPassesAmount(int)}
     */
    @Deprecated
    public boolean revokePassUse() {
        return modifyPassesAmount(+1);
    }

    /**
     * Returns the amount of passes currently owned by the wrapped player. Passes are some kind of currency that is intended to be used up by games
     * for premium functionality.
     *
     * @return The amount of passes currently owned by the wrapped player.
     * @see PlayerWrapper#addPassUse()
     * @see PlayerWrapper#getPassesUsed()
     */
    public long getPassesAmount() {
        return this.passesAmount.getValue();
    }

    /**
     * Returns how many passes the wrapped player has used up all-time. Passes are some kind of currency that is intended to be used up by games for
     * premium functionality.
     *
     * @return The all-time count of passes used by the wrapped player.
     */
    public long getPassesUsed() {
        return this.passesUsed.getValue();
    }

    /**
     * Summons passes from the magical void. Use negative values to withdraw passes.
     *
     * @param amount Can also be negative!
     */
    public void summonPasses(int amount) {
//        this.refetchPasses();//safety! // No longer needed since only a modifier is being written
        this.passesAmount.modify(amount);
        this.xyFlush();//safety!
    }

    /**
     * Re-fetches only the amount of passes owned and passes used by the wrapped player. This is for safety and accuracy purposes, and should be
     * invoked every once-in-a-while by games to prevent players from using their passes twice.
     */
    public void refetchPasses() {
        Validate.notNull(getUniqueId(), "Cannot re-fetch passes, no UUID!");

        if (this.passQueryBuilder == null) {
            this.passQueryBuilder = new QueryBuilder(FULL_CENTRAL_USER_TABLE_NAME)
                    .addPart((QuerySnapshot) this.passesAmount)
                    .addPart((QuerySnapshot) this.passesUsed)
                    .addUniqueIdentifier(this.uuid);
        }

        try (QueryResult queryResult = this.passQueryBuilder.executeSelect(getSql(), false).vouchForResultSet()) {
            if (queryResult.rs().next()) {
                this.passesAmount.updateValue(queryResult.rs().getInt(this.passesAmount.getColumnName()));
                this.passesUsed.updateValue(queryResult.rs().getInt(this.passesUsed.getColumnName()));
            } else {
                this.passesAmount.updateValue(0);
                this.passesUsed.updateValue(0);
                Logger.getLogger(getClass().getName()).warning("Player " + name() + " had no table entry for passes!!");
                this.passQueryBuilder.addUniqueIdentifier(this.plrName) //Add name for insertion
                        .executeUpdate(getSql()); //Other fields have default values
                this.passQueryBuilder.clearUniqueIdentifiers() //Clear identifiers to remove the name
                        .addUniqueIdentifier(this.uuid); //Re-add UUID
            }
        } catch (SQLException e) {
            sql.formatAndPrintException(e, "PlayerWrapper#refetchPasses()");
        }
    }

    /**
     * @return The chat color of this player, using the one of the "default" group if a nickname is enabled.
     */
    public String getChatColor() {
        if (this.getNick() != null) { //default chat for nicknames - kanney feature(TM)
            return GroupData.getByName("default", sql).getChatColor();
        }
        return this.getGroup().getChatColor();
    }

    /**
     * Parses a colorized name for the player represented by this wrapper. Primarily intended for chat, rankings and the TAB list.
     *
     * @param sixteenCharLimit Whether to limit the output to 16 characters (I did not make that limitation!)
     * @return The colorized display name of this player, using the color of the "default" group if a nickname is enabled.
     */
    public String getColorizedDisplayName(boolean sixteenCharLimit) {
        String nameColor = this.getGroup().getNameColor();

        if (this.getNick() != null) { //default chat for nicknames - kanney feature(TM)
            nameColor = GroupData.getByName("default", sql).getNameColor();
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
        if (this.getNick() != null) {
            return this.getNick();
        }

        return this.name();
    }

    /**
     * Returns the nickname the wrapped player has chosen or
     * {@code null} if he/she has not (yet) chosen one. If the player has chosen a nickname, please try to use it as much as possible.
     *
     * @return nickname the wrapped player has chosen or {@code null} if none.
     * @see PlayerWrapper#getDisplayName()
     * @see PlayerWrapper#getColorizedDisplayName(boolean)
     */
    public String getNick() {
        return this.nick.getValue();
    }

    /**
     * Sets the group of the wrapped player. {@link GroupData} objects can be obtained using the static factory method
     * {@link GroupData#getByName(java.lang.String, SafeSql)}
     *
     * @param group New group for the wrapped player.
     */
    public void setGroup(GroupData group) {
        this.group = group;
        this.groupName.setValue(group.getName());
    }

    /**
     * Sets the nickname of this player.
     *
     * @param nick New nickname for the wrapped player or {@code null} to disable.
     * @see PlayerWrapper#getNick()
     * @see PlayerWrapper#getDisplayName()
     */
    public void setNick(String nick) {
        this.nick.setValue(nick);
    }

////////////////////////// STATIC UTILITY METHODS //////////////////////////////////////////////////////////////////////

    /**
     * Returns the real user name for a provided String.
     *
     * @param name Name to seek.
     * @return Name of the player currently owning this nickname or {@code name} if {@code name} is a real name or {@code null} if
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
     * @return Name of the player currently owning this nickname or {@code null} if this nickname has not been registered.
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

}
