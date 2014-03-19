package io.github.xxyy.common.cmd;

import io.github.xxyy.common.XycConstants;
import io.github.xxyy.common.util.CommandHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * A wrapper for {@link CommandExecutor} that provides several tools.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class XYCCommandExecutor implements CommandExecutor {

    /**
     * Called to catch commands. Some things have already been done!
     *
     * @see CommandExecutor#onCommand(CommandSender, Command, String, String[])
     * @param senderName Pre-fetched to save dat line of code :)
     *
     * @return Success
     */
    public abstract boolean catchCommand(CommandSender sender, String senderName, Command cmd, String label, String[] args);

    /**
     * Please DO NOT OVERRIDE. That's not good practice. Oh, you can't!
     */
    @Override
    public final boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("xyc")) {
            CommandHelper.msg("§9▀▄▒▄▀ ▒█░░▒█ ▒█▀▀█ §eXyCommon Libary.\n"
                    + "§9░▒█░░ ▒█▄▄▄█ ▒█░░░ §e" + XycConstants.versionString + "\n"
                    + "§9▄▀▒▀▄ ░░▒█░░ ▒█▄▄█ §ehttp://xxyy.github.io/\n"
                    + "§9### §eXyCommon...xxyy98 (Philipp Nowak) §9###", sender);
        }
        if (!this.preCatch(sender, sender.getName(), cmd, label, args)) {
            return true;
        }
        return this.catchCommand(sender, sender.getName(), cmd, label, args);
    }

    /**
     * This allows for implementations to specify something to be executed before {@link XYCCommandExecutor#catchCommand(CommandSender, String, Command, String, String[])}. This is especially if you
     * want to undo AFK states, display mails and so on. (Also to specify your very own credit message)
     *
     * @return Whether execution should continue
     */
    public boolean preCatch(CommandSender sender, String senderName, Command cmd, String label, String[] args) {
        return true;
    }
}
