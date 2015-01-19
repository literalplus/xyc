/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.test.util;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.UUID;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 18.8.14
 */
@SuppressWarnings("UnusedDeclaration")
public class MockHelper {
    private MockHelper() {

    }

    public static Server mockServer() {
        Server server = Bukkit.getServer();

        if (server == null) {
            server = mock(Server.class);
        } else {
            Mockito.reset(server);
        }

        Mockito.when(server.getName()).thenReturn("Spagt");
        Mockito.when(server.getBukkitVersion()).thenReturn("fuk of bukite");
        Mockito.when(server.getVersion()).thenReturn("infinity");
        Logger logger = Logger.getLogger(Server.class.getName());
        Mockito.when(server.getLogger()).thenReturn(logger);

        CommandSender consoleSender = loggerSender(mock(ConsoleCommandSender.class), logger);
        Mockito.when(server.getConsoleSender()).thenAnswer(invocation -> consoleSender);
        Mockito.when(server.getPlayer(Matchers.any(UUID.class)))
                .then(id -> Bukkit.getServer().getOnlinePlayers().stream()
                .filter(plr -> plr.getUniqueId().equals(id.getArguments()[0]))
                .findAny().orElse(null));

        if (Bukkit.getServer() == null) {
            Bukkit.setServer(server);
        }

        return server;
    }

    public static Player mockPlayer(final UUID uuid, final String name) {
        Player plr = mock(Player.class);
        Mockito.when(plr.getUniqueId()).thenReturn(uuid);
        Mockito.when(plr.getName()).thenReturn(name);
        return plr;
    }

    public static Plugin mockPlugin(Server server) {
        Plugin plugin = mock(Plugin.class);
        when(plugin.getServer()).thenReturn(server);
        when(plugin.getName()).thenReturn("SpagtPlugine");
        Logger logger = server.getLogger();
        when(plugin.getLogger()).thenReturn(logger);
        return plugin;
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