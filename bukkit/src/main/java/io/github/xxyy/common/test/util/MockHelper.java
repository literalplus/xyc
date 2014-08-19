/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.test.util;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

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
            server = Mockito.mock(Server.class);
        } else {
            Mockito.reset(server);
        }

        Mockito.when(server.getName()).thenReturn("Spagt");
        Mockito.when(server.getBukkitVersion()).thenReturn("fuk of bukite");
        Mockito.when(server.getVersion()).thenReturn("infinity");
        Mockito.when(server.getLogger()).thenReturn(Logger.getLogger(Server.class.getName()));

        CommandSender consoleSender = loggerSender(Mockito.mock(ConsoleCommandSender.class), Bukkit.getServer().getLogger());
        Mockito.when(server.getConsoleSender()).thenAnswer(invocation -> consoleSender);
        Mockito.when(server.getPlayer(Matchers.any(UUID.class))).then(id -> Arrays.asList(Bukkit.getServer().getOnlinePlayers()).stream()
                .filter(plr -> plr.getUniqueId().equals(id.getArguments()[0]))
                .findAny().orElse(null));

        if (Bukkit.getServer() == null) {
            Bukkit.setServer(server);
        }

        return server;
    }

    public static Player mockPlayer(final UUID uuid, final String name) {
        Player plr = Mockito.mock(Player.class);
        Mockito.when(plr.getUniqueId()).thenReturn(uuid);
        Mockito.when(plr.getName()).thenReturn(name);
        return plr;
    }

    public static CommandSender printlnSender(CommandSender sender) {
        if (!Mockito.mockingDetails(sender).isMock()) {
            sender = Mockito.spy(sender);
        }

        Mockito.doAnswer((invocation) -> {
            System.out.println(invocation.getArguments()[0]);
            return null;
        }).when(sender).sendMessage(Matchers.any(String.class));

        return sender;
    }

    public static CommandSender loggerSender(CommandSender sender, Logger logger) {
        if (!Mockito.mockingDetails(sender).isMock()) {
            sender = Mockito.spy(sender);
        }

        Mockito.doAnswer((invocation) -> {
            logger.info((String) invocation.getArguments()[0]);
            return null;
        }).when(sender).sendMessage(Matchers.any(String.class));

        return sender;
    }
}
