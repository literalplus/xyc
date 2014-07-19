/*
 * This file is part of XYC.
 * It may only be used in my projects
 * and only be distributed with my explicit permission.
 */
package io.github.xxyy.common.util;

import com.google.common.base.Preconditions;
import io.github.xxyy.common.XycConstants;
import io.github.xxyy.common.localisation.XycLocale;
import io.github.xxyy.common.util.inventory.InventoryHelper;
import io.github.xxyy.common.util.math.NumberHelper;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that provides several utilities for commands and some unrelated methods.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class CommandHelper {
    /**
     * Alternative to {@link Bukkit#broadcast(String, String)} that works with PermissionsEx.
     *
     * @param msg        Message to send
     * @param permission Players with that permission will receive {@code msg}
     * @see Bukkit#broadcast(String, String)
     */
    public static void broadcast(String msg, String permission) {
        for (Player plr : Bukkit.getOnlinePlayers()) {
            if (plr.hasPermission(permission)) {
                plr.sendMessage(msg);
            }
        }
    }

    /**
     * public static boolean checkActionPermAndMsg(CommandSender sender, String permission, String action) Checks for a permission and gives an
     * appropriate message. (For actions,not commands)
     *
     * @param sender     CommandSender to check for permission and to send message to
     * @param permission Permission to require
     * @param action     Short description of the action that needs permission, shown to {@code sender} if permission check returns false.
     * @return Whether {@code sender} had the permission required.
     */
    public static boolean checkActionPermAndMsg(CommandSender sender, String permission, String action) {
        if (sender.hasPermission(permission)) {
            return true;
        }
        sender.sendMessage(String.format(XycLocale.getString("XYC-nopermsact-1", sender.getName()), action));
        sender.sendMessage(String.format(XycLocale.getString("XYC-noperms-2", sender.getName()), permission));
        return false;
    }

    /**
     * Checks for a permission and gives an appropriate message if it is assigned to {@code sender}.
     *
     * @param sender     CommandSender to check for permission and to send message to
     * @param permission Permission to require
     * @param label      Label of the command that needs permission, shown to {@code sender} if permission check returns false.
     * @return If sender has permission.
     */
    public static boolean checkPermAndMsg(CommandSender sender, String permission, String label) {
        if (sender.hasPermission(permission)) {
            return true;
        }
        sender.sendMessage(String.format(XycLocale.getString("XYC-noperms-1", sender.getName()), label));
        sender.sendMessage(String.format(XycLocale.getString("XYC-noperms-2", sender.getName()), permission));
        return false;
    }

    /**
     * Clears the Inventory of a provided Player.
     *
     * @param plr Target Player
     * @deprecated Use {@link io.github.xxyy.common.util.inventory.InventoryHelper#clearInventory(org.bukkit.entity.Player)}
     */
    @Deprecated
    public static void clearInv(Player plr) {
        InventoryHelper.clearInventory(plr);
    }

    /**
     * Clears a list of inventories.
     *
     * @param plrs Target Players
     * @see CommandHelper#clearInv(org.bukkit.entity.Player)
     * @deprecated Use {@link io.github.xxyy.common.util.inventory.InventoryHelper#clearInventory(org.bukkit.entity.Player)}.
     */
    @Deprecated
    public static void clearInvList(final List<Player> plrs) {
        InventoryHelper.clearInventories(plrs);
    }

    /**
     * Safely gets the size of a collection, avoiding {@link NullPointerException}s.
     *
     * @param collection Collection to count, May be {@code null}
     * @return Amount of items in {@code collection}, or -1 if {@code collection} is {@code null}.
     */
    public static int safeSize(Collection<?> collection) {
        if (collection == null) {
            return -1;
        }

        return collection.size();
    }

    /**
     * Comma separates a Collection's contents' String representations. This is the same as calling {@link CommandHelper#CSCollection(java.lang.Iterable, java.lang.String)} with "{empty}" as default.
     *
     * @param input An Iterable to separate
     * @return Element1, Element2, Element3 OR "{empty}"
     * @see CommandHelper#CSCollection(java.lang.Iterable, java.lang.String)
     */
    public static String CSCollection(Iterable<?> input) {
        return CSCollection(input, "{empty}");
    }

    /**
     * Comma separates a Collection's children's String representations.
     *
     * @param input      An Iterable to separate
     * @param defaultVal value to be returned if {@code col} is empty.
     * @return Element1, Element2, Element3 OR {@code defaultVal}
     * @see CommandHelper#CSCollection(java.lang.Iterable)
     */
    public static String CSCollection(Iterable<?> input, String defaultVal) {
        if (input == null) {
            return "~~~null~~~";
        }

        Iterator<?> i = input.iterator();
        if (!i.hasNext()) {
            return defaultVal;
        }
        final StringBuilder rtrnBuilder = new StringBuilder(String.valueOf(i.next()));
        while (i.hasNext()) {
            rtrnBuilder.append(", ")
                    .append(i.next());
        }
        return rtrnBuilder.toString();
    }

    /**
     * Comma separates a Collection's children's ShortString representations.
     *
     * @param input An Iterable to separate
     * @return Element1, Element2, Element3 OR {@code defaultVal}
     * @see CommandHelper#CSCollection(java.lang.Iterable, java.lang.String)
     * @see CommandHelper#CSCollection(java.lang.Iterable)
     */
    public static String CSCollectionShort(final Iterable<? extends ToShortStringable> input) {
        Iterator<? extends ToShortStringable> i = input.iterator();
        if (!i.hasNext()) {
            return "{empty}";
        }
        StringBuilder rtrnBuilder = new StringBuilder(" ► ").append(i.next().toShortString());
        while (i.hasNext()) {
            rtrnBuilder.append("\n ► ")
                    .append(i.next().toShortString());
        }
        return rtrnBuilder.append("\n").toString();
    }

    /**
     * Formats {@code seconds} for a human-readable output in german. If {@code seconds &gt;= 60}, the output will be formatted like this:
     * <i>x Minuten und y Sekunden</i>
     * <b>Notice:</b> Currently, there is no support for hours.
     * <p>
     * <b>Examples:</b>
     * 1 -&gt; <i>1 Sekunde</i>
     * 46 -&gt; <i>46 Sekunden</i>
     * 60 -&gt; <i>1 Minute</i>
     * 61 -&gt; <i>1 Minute und 1 Sekunde</i>
     * 90 -&gt; <i>1 Minute und 30 Sekunden</i>
     * 120 -&gt; <i>2 Minuten</i>
     * 125 -&gt; <i>2 Minuten und 5 Sekunden</i>
     *
     * @param seconds Time to be formatted, in seconds.
     * @return a human-readable time string <b>in German</b>.
     * @deprecated Implemented for a specific language, with no possibility to change locale. Deprecated without replacement.
     */
    @SuppressWarnings("SpellCheckingInspection")
    @Deprecated
    public static String formatSeconds(int seconds) {
        if (seconds < 60) {
            return seconds + " Sekunde" + ((seconds == 1) ? "" : "n");
        }
        short minutes = (short) (seconds / 60);
        seconds -= (60 * minutes);
        return minutes + " Minute" + ((minutes == 1) ? "" : "n") + ((seconds == 0) ? "" : " und " + seconds + " Sekunde" + ((seconds == 1) ? "" : "n"));
    }

    /**
     * Returns a string representing the size of each element in {@code values} in ASCII art. {@code values} may only contain up to 16
     * values (Hexadecimal)
     *
     * @param maxLength how long the string shall be.
     * @param values    values.
     * @param max       The highest value in {@code values}.
     * @return 1111112222233333
     * @deprecated Implementation not clean, not generic enough
     */
    @Deprecated
    public static String getBarOfValues(int maxLength, List<Integer> values, int max) {
        Preconditions.checkArgument(values.size() <= 16, "Values may not contain more than 16 items!");

        maxLength -= 2; //why?
        int i = 0;
        final StringBuilder rtrnBuilder = new StringBuilder();
        for (int val : values) {
            float factor = (((float) val) / ((float) max));
            short drawCount = (short) (maxLength * factor);
            rtrnBuilder.append("\u00a7")
                    .append(Integer.toHexString(i))
                    .append(StringUtils.rightPad("", drawCount, (char) ('0' + i)));// there should never be more than 16 full items.
            i++;
        }
        return rtrnBuilder.toString();
    }

    /**
     * Returns the names of all online Players as a List. Actually kinda useful because it returns the NAMES and not the corresponding objects.
     *
     * @return A List of all online Players' names.
     * @see Bukkit#getOnlinePlayers()
     */
    public static List<String> getOnlinePlayerNames() {
        final List<String> rtrn = new ArrayList<>();
        for (final Player plr : Bukkit.getOnlinePlayers()) {
            rtrn.add(plr.getName());
        }
        return rtrn;
    }

    /**
     * Returns an ASCII art bar indicating completion state of a task.
     * <br>
     * <b>Example:</b><br>
     * ████▒▒▒ for (6,1,2)
     *
     * @param maxLength How many characters the bar will take up.
     * @param value     the current progress
     * @param max       the goal (100%)
     * @return A nice ASCII art bar using ASCII blocks.
     */
    public static String getProgressBar(int maxLength, int value, int max) {
        Preconditions.checkArgument(value <= max, "The current progress may not be greather than the goal.");
        double factor = (((double) value) / ((double) max));
        maxLength -= 5;// "[]xx%".length
        byte linesToDraw = (byte) (maxLength * factor);
        return StringUtils.rightPad(StringUtils.rightPad("[", linesToDraw, '\u2588'), maxLength, '\u2592') + "]"
                + StringUtils.leftPad(((byte) (factor * 100)) + "%", 3, '0');
    }

    /**
     * Checks if a Player's Inventory is currently empty (i.e. all slots, including armor, are either null or Material.AIR)
     *
     * @param plr Target Player
     * @return Whether the Player's Inventory is currently empty.
     * @deprecated Use {@link io.github.xxyy.common.util.inventory.InventoryHelper#isInventoryEmpty(org.bukkit.entity.Player)}.
     */
    @Deprecated
    public static boolean isInventoryEmpty(Player plr) {
        return InventoryHelper.isInventoryEmpty(plr);
    }

    /**
     * Determines if a number {@code toCheck} is between or equal to one the boundaries specified. There is no special order of the boundaries
     * required, they can even be equal.
     *
     * @param toCheck   Target integer
     * @param boundary1 One of the boundaries
     * @param boundary2 One of the boundaries
     * @return {@code true}, If {@code toCheck} is between boundary1 and boundary2.
     * @deprecated Use {@link io.github.xxyy.common.util.math.NumberHelper#isNumberBetween(int, int, int)}.
     */
    @Deprecated
    public static boolean isNumberBetween(int toCheck, int boundary1, int boundary2) {
        return NumberHelper.isNumberBetween(toCheck, boundary1, boundary2);
    }

    /**
     * Returns true if sender is not a player (i.e. the CONSOLE) and displays a message.
     *
     * @param sender Who to check
     * @param label  Command name
     * @return true, if sender is CONSOLE
     */
    public static boolean kickConsoleFromMethod(CommandSender sender, String label) {
        if (sender instanceof Player) {
            return false;
        }
        sender.sendMessage(String.format(XycLocale.getString("XYC-kickconsolemsg", "CONSOLE"), XycConstants.chatPrefix, label));
        return true;
    }

    /**
     * Returns a List with only {@code t} in it.
     *
     * @deprecated Use {@link com.google.common.collect.Lists#newArrayList(java.lang.Object...)}
     */
    @Deprecated
    public static <T> List<T> list(T t) {
        List<T> lst = new ArrayList<>();
        lst.add(t);
        return lst;
    }

    /**
     * Sends a message to sender without those ugly spaces on the beginning of each line.
     *
     * @param msg    Message to be sent, preferably multi-line (use /n)
     * @param sender Receiver of the message
     * @return always {@code true} for use with commands.
     */
    public static boolean msg(String msg, CommandSender sender) {
        for (String str2 : msg.split("\n")) {
            sender.sendMessage(str2);
        }
        return true;
    }

    /**
     * Prints a message to {@link System#out} and the provided logger {@code lgr} if it is not {@code null}.
     *
     * @param message Message to print
     * @param lgr     The logger to print it to, can be {@code null}.
     * @param lvl     {@link Level} to use
     */
    public static void printAndOrLog(String message, Logger lgr, Level lvl) {
        System.out.println(message);
        if (lgr != null) {
            lgr.log(lvl, message);
        }
    }

    /**
     * Sends notification of an important action to all Ops and CONSOLE. (/reload-styled) example: §7§m[Notch: §aInvented Minecraft.§7]
     *
     * @param sender  The player who performed the action
     * @param message Short description of the action
     */
    public static void sendImportantActionMessage(CommandSender sender, String message) {
        CommandHelper.sendMessageToOpsAndConsole("\u00a77\u00a7o[" + sender.getName() + ": \u00a7a\u00a7o" + message + "\u00a77\u00a7o]");
    }

    /**
     * Sends a message to all online Ops and CONSOLE.
     *
     * @param message The message to be sent
     */
    public static void sendMessageToOpsAndConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
        if (Bukkit.getOnlinePlayers().length == 0) {
            return;
        }
        for (Player plr : Bukkit.getOnlinePlayers()) {
            if (plr.hasPermission("xyc.adminmsg") || plr.isOp()) {
                plr.sendMessage(message);
            }
        }
    }

    /**
     * returns a set with just {@code t} in it.
     *
     * @param t Element to put and type argument
     * @return Set of type T with {@code t} in it.
     * @deprecated Use {@link com.google.common.collect.Sets#newHashSet(java.lang.Object...)} instead
     */
    @Deprecated
    public static <T> Set<T> set(T t) {
        Set<T> set = new HashSet<>();
        set.add(t);
        return set;
    }

    /**
     * Returns a String with maximal length of 16 characters. If {@code colorString} does not fit into {@code input} without exceeding the
     * limit, it will be returned uncolorized.
     * <b>Notice:</b> colorString is put first; Example: ("xxyy98","§3§l") =&gt; "§3§lxxyy98"
     *
     * @param input       String to colorize
     * @param colorString Color to use
     * @return A String with a maximal length of 16 characters. Even if {@code input} is longer than that.
     */
    public static String sixteenCharColorize(final String input, final String colorString) {
        if (input == null) {
            return "null";
        }
        if (colorString == null) {
            return CommandHelper.sixteenCharLimit(input);
        }
        if ((input.length() + colorString.length()) > 16) {
            return CommandHelper.sixteenCharLimit(input);
        }
        return colorString.concat(input);
    }

    /**
     * Returns {@code input}. If it is longer than 16 characters, returns a shortened version (cuts the end off)
     *
     * @param input String to limit
     * @return {@code input}, trimmed to 16 chars in necessary (trimming tail)
     */
    public static String sixteenCharLimit(final String input) {
        if (input == null) {
            return null;
        }
        if (input.length() > 16) {
            return input.substring(0, 16);
        }
        return input;
    }

    /**
     * Parses a tabComplete return so that all elements not starting with the last element of {@code args} are removed. if {@code rtrn} is
     * {@code null} or empty, {@code null} is returned. If {@code args.length == 0}, {@code rtrn} is returned. if the last arg is
     * empty, * {@code rtrn} is returned.
     *
     * @param args Arguments already entered by the user.
     * @param rtrn A list of Strings with tabComplete suggestions.
     * @return {@code rtrn}, with all elements not starting with the last element of {@code args} removed.
     */
    public static List<String> tabCompleteArgs(final String[] args, final List<String> rtrn) {
        if (rtrn == null || rtrn.isEmpty()) {
            return null;
        }
        if (args.length == 0) {
            return rtrn;
        }
        String lastArg = args[args.length - 1];
        if (lastArg.isEmpty()) {
            return rtrn;
        }
        Iterator<String> it = rtrn.iterator();
        while (it.hasNext()) {
            if (!it.next().startsWith(lastArg)) {
                it.remove();
            }
        }
        return rtrn;
    }

    /**
     * @param input Iterable to use.
     * @return A list of the String representations of all objects in a Collection.
     */
    public static List<String> toStringAll(Iterable<?> input) {
        List<String> lst = new ArrayList<>();
        for (Object obj : input) {
            lst.add(obj.toString());
        }
        return lst;
    }

    /**
     * An Array-based version of {@link CommandHelper#toStringAll(java.lang.Iterable)}
     */
    public static List<String> toStringAll(final Object[] arr) {
        return toStringAll(Arrays.asList(arr));
    }

    /**
     * Write t.toString() to Console and pass it as return.
     *
     * @param <T>   Type of the input Object.
     * @param input Object whose String representation is to be printed.
     * @return {@code t}, completely untouched.
     */
    public static <T> T writeAndPass(T input) {
        System.out.println("output: " + input.toString());
        return input;
    }
}
