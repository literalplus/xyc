/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.github.xxyy.common.XyHelper;
import io.github.xxyy.common.XycConstants;
import io.github.xxyy.common.util.CommandHelper;
import io.github.xxyy.common.xyplugin.AbstractXyPlugin;

/**
 * A wrapper for {@link CommandExecutor} that provides several tools.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class XYCCommandExecutor implements CommandExecutor {

    /**
     * Called to catch commands. Some things have already been done!
     *
     * @param senderName Pre-fetched to save dat line of code :)
     * @return Success
     * @see CommandExecutor#onCommand(CommandSender, Command, String, String[])
     */
    public abstract boolean catchCommand(CommandSender sender, String senderName, Command cmd, String label, String[] args);

    /**
     * Please DO NOT OVERRIDE. That's not good practice. Oh, you can't!
     */
    @Override
    public final boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return this.handleXycSubcommand(args, sender, label) || //Handle XYC tools
                !this.preCatch(sender, sender.getName(), cmd, label, args) //Handle pre-catch
                || this.catchCommand(sender, sender.getName(), cmd, label, args); //Catch

    }

    /**
     * This allows for implementations to specify something to be executed before {@link XYCCommandExecutor#catchCommand(CommandSender, String, Command, String, String[])}. This is especially if you
     * want to undo AFK states, display mails and so on. (Also to specify your very own credit message)
     *
     * @return Whether execution should continue
     */
    @SuppressWarnings("UnusedParameters")
    public boolean preCatch(CommandSender sender, String senderName, Command cmd, String label, String[] args) {
        return true;
    }

    private boolean handleXycSubcommand(String[] args, CommandSender sender, String label) {
        if (args.length == 0 || !args[0].equalsIgnoreCase("xyc")) {
            return false;
        }

        if (args.length == 1 || args[1].equalsIgnoreCase("version")) {
            CommandHelper.msg(
                    "§9▀▄▒▄▀ ▒█░░▒█ ▒█▀▀█ §eXyCommon Library.\n"
                            + "§9░▒█░░ ▒█▄▄▄█ ▒█░░░ §eby xxyy (Philipp Nowak)\n"
                            + "§9▄▀▒▀▄ ░░▒█░░ ▒█▄▄█ §ehttp://xxyy.github.io/\n"
                            + "§9### §e" + XycConstants.VERSION.toString() + " §9###", sender);
        } else {
            if (CommandHelper.checkPermAndMsg(sender, "xyc.command", label + "xyc")) {
                switch (args[1].toLowerCase()) {
                    case "rstlng":
                        XyHelper.getLocale().resetLang();
                        //noinspection SpellCheckingInspection
                        sender.sendMessage("§aI haz reset da langz!");
                        break;
                    case "plugins":
                        int i = 0;
                        for (AbstractXyPlugin pl : AbstractXyPlugin.getInstances()) {
                            sender.sendMessage(pl.getDescription().getName() + " @ " + pl.getDescription().getVersion()
                                    + " by " + CommandHelper.CSCollection(pl.getDescription().getAuthors()));
                            i++;
                        }
                        sender.sendMessage("§e" + i + " XyPlugins loaded.");
                        break;
                    default:
                        sender.sendMessage("§eUsage: /" + label + "xyc [rstlng|plugins|version]");
                        break;
                }
            }
        }

        return true;
    }
}
