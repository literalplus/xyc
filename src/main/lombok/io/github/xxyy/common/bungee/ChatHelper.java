package io.github.xxyy.common.bungee;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * helps dealing with chat on BungeeCord servers.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 16.5.14
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChatHelper {

    /**
     * If sender is not a player, send them a message.
     * @param sender CommandSender to analyse
     * @return {@code true} if sender is not a player.
     */
    public static boolean kickConsoleFromMethod(CommandSender sender) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new ComponentBuilder("Du kannst diesen Befehl nur als Spieler verwenden!")
                    .color(ChatColor.RED).create());
            return true;
        }

        return false;
    }
}
