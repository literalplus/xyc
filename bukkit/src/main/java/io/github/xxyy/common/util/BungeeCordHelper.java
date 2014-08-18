/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without permission from the
 *  original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.xxyy.common.xyplugin.AbstractXyPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class that provides several static BungeeCord utilities.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class BungeeCordHelper {

    private BungeeCordHelper() {
    }

    /**
     * Sends a player to a specific BungeeCord proxied server. This will fail silently if BungeeCord is not found. This will register an outgoing plugin message channel "BungeeCord" for the first
     * XyPlugin found if that has not already been done.
     *
     * @param plr        The player to send
     * @param serverName Destination server
     */
    public static void sendTo(Player plr, String serverName) {
        BungeeCordHelper.tryRegOut();
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bs);
        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName);
        } catch (IOException imaginaryException) { /* this will never happen */ }
        plr.sendPluginMessage(AbstractXyPlugin.getInstances().get(0), "BungeeCord", bs.toByteArray());
    }

    /**
     * Sends a player to a specified BungeeCord server, after a specified amount of server ticks.
     *
     * @param plr        Player to target
     * @param serverName destination server
     * @param delay      Delay in server ticks.
     * @see BungeeCordHelper#sendTo(Player, String)
     * @see BukkitScheduler#runTaskLater(org.bukkit.plugin.Plugin, Runnable, long)
     */
    public static void sendToIn(final Player plr, final String serverName, long delay) {
        Bukkit.getScheduler().runTaskLater(AbstractXyPlugin.getInstances().get(0), () -> BungeeCordHelper.sendTo(plr, serverName), delay);
    }

    private static void tryRegOut() {
        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(AbstractXyPlugin.getInstances().get(0), "BungeeCord")) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(AbstractXyPlugin.getInstances().get(0), "BungeeCord");
        }
    }
}
