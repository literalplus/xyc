/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package li.l1t.common.util;

import li.l1t.common.xyplugin.AbstractXyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

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
