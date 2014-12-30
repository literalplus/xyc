/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import io.github.xxyy.common.XycConstants;

import java.util.UUID;

/**
 * helps dealing with chat on BungeeCord servers.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 16.5.14
 */
public final class ChatHelper {
    private ChatHelper() {

    }

    /**
     * If sender is not a player, send them a message.
     *
     * @param sender CommandSender to analyse
     * @return {@code true} if sender is not a player.
     */
    public static boolean kickConsoleFromMethod(CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new ComponentBuilder("Du kannst diesen Befehl nur als Spieler verwenden!")
                    .color(ChatColor.RED).create());
            return true;
        }

        return false;
    }

    /**
     * Returns the UUID of the sender, or {@link io.github.xxyy.common.XycConstants#NIL_UUID} if the sender does not have an UUID.
     *
     * @param sender the sender to target
     * @return an UUID which can represent given sender
     */
    public static UUID getSenderId(CommandSender sender) {
        return (sender != null && sender instanceof ProxiedPlayer) ? ((ProxiedPlayer) sender).getUniqueId() : XycConstants.NIL_UUID;
    }
}
