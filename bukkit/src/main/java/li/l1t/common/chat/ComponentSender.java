/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


/**
 * Static utility class that allows to send {@link ComponentBuilder}s to Bukkit {@link CommandSender} instances.
 * This allows to use the builder but still not exclude non-Player senders from receiving formatted messages.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2015-09-10
 */
public class ComponentSender {
    private ComponentSender() {

    }

    /**
     * Sends the built components from the given builder to a receiver. Players will receiver fully formatted JSON chat
     * including events and all that fancy stuff, everything else will receive solely the legacy text representation.
     *
     * @param builder  the builder to use to create the parts to send
     * @param receiver the receiver of the parts
     * @return whether a message was sent
     */
    public static boolean sendTo(ComponentBuilder builder, CommandSender receiver) {
        return sendTo(builder.create(), receiver);
    }

    /**
     * Sends the given components to a receiver. Players will receiver fully formatted JSON chat including events and
     * all that fancy stuff, everything else will receive solely the legacy text representation.
     *
     * @param parts    the parts to send
     * @param receiver the receiver of the parts
     * @return whether a message was sent
     */
    public static boolean sendTo(BaseComponent[] parts, CommandSender receiver) {
        if (receiver instanceof Player){
            ((Player) receiver).spigot().sendMessage(parts);
        } else {
            receiver.sendMessage(TextComponent.toLegacyText(parts));
        }
        return true;
    }

    /**
     * Sends the given components to a receiver. Players will receiver fully formatted JSON chat including events and
     * all that fancy stuff, everything else will receive solely the legacy text representation. This method executes
     * the action in the main server thread and is intended for code in async threads.
     *
     * @param parts    the parts to send
     * @param receiver the receiver of the parts
     * @param plugin   the plugin to use for accessing the scheduler
     * @return whether a message was sent
     */
    public static boolean sendToSync(BaseComponent[] parts, CommandSender receiver, Plugin plugin) {
        plugin.getServer().getScheduler().runTask(plugin, () -> sendTo(parts, receiver));
        return true;
    }

    /**
     * Sends the given components to a receiver. Players will receiver fully formatted JSON chat including events and
     * all that fancy stuff, everything else will receive solely the legacy text representation. This method executes
     * the action in the main server thread and is intended for code in async threads.
     *
     * @param builder  the builder to use to create the parts to send
     * @param receiver the receiver of the parts
     * @param plugin   the plugin to use for accessing the scheduler
     * @return whether a message was sent
     */
    public static boolean sendToSync(ComponentBuilder builder, CommandSender receiver, Plugin plugin) {
        plugin.getServer().getScheduler().runTask(plugin, () -> sendTo(builder.create(), receiver));
        return true;
    }
}
