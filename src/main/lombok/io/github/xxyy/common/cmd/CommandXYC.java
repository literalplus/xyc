package io.github.xxyy.common.cmd;

import io.github.xxyy.common.XyHelper;
import io.github.xxyy.common.util.CommandHelper;
import io.github.xxyy.common.xyplugin.AbstractXyPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * A command that provides some admin utilities for XYC. (Functions include resetting the internal XYC locale files and listing all registered XyPlugins.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class CommandXYC extends XYCCommandExecutor {

    @Override
    public final boolean catchCommand(final CommandSender sender, final String senderName, final Command command, final String label,
                                      final String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /xyc [rstlng|plugins|xyc]");
        } else {
            switch (args[0].toLowerCase()) {
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
                sender.sendMessage("§eUsage: /xyc");
                break;
            }
        }
        return true;
    }
}
