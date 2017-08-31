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

package li.l1t.common.test.util;

import li.l1t.common.test.util.mokkit.MockPlugin;
import li.l1t.common.test.util.mokkit.MockServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.UUID;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 18.8.14
 */
@SuppressWarnings("UnusedDeclaration")
public class MockHelper {
    private MockHelper() {

    }

    public static MockServer mockServer() {
        Server server = Bukkit.getServer();

        if (server == null) {
            server = new MockServer();
        } else if (server instanceof MockServer) {
            ((MockServer) server).reset();
        }

        if (Bukkit.getServer() == null) {
            Bukkit.setServer(server);
        }

        //noinspection ConstantConditions
        return (MockServer) server;
    }

    public static Player mockPlayer(final UUID uuid, final String name) {
        Player plr = mock(Player.class);
        Mockito.when(plr.getUniqueId()).thenReturn(uuid);
        Mockito.when(plr.getName()).thenReturn(name);
        return plr;
    }

    public static Plugin mockPlugin(MockServer server) {
        PluginDescriptionFile description = new PluginDescriptionFile(
                "MockPlugin", "4.2.0", "li.l1t.common.test.util.mokkit.MockPlugin"
        );
        return new MockPlugin(server, description);
    }

    public static CommandSender printlnSender(CommandSender sender) {
        if (!Mockito.mockingDetails(sender).isMock()) {
            sender = spy(sender);
        }

        Mockito.doAnswer((invocation) -> {
            System.out.println(invocation.getArguments()[0]);
            return null;
        }).when(sender).sendMessage(Matchers.any(String.class));

        return sender;
    }

    public static CommandSender loggerSender(CommandSender sender, Logger logger) {
        if (!Mockito.mockingDetails(sender).isMock()) {
            sender = spy(sender);
        }

        Mockito.doAnswer((invocation) -> {
            logger.info((String) invocation.getArguments()[0]);
            return null;
        }).when(sender).sendMessage(Matchers.any(String.class));

        return sender;
    }
}
