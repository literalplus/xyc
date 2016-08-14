/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.test.util;

import com.avaje.ebean.EbeanServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Mock implementation of a plugin, for testing.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-14
 */
public class MockPlugin implements Plugin {
    private final MockServer server;
    private final PluginDescriptionFile description;
    private boolean enabled = true;

    public MockPlugin(MockServer server, PluginDescriptionFile description) {
        this.server = server;
        this.description = description;
    }

    @Override
    public File getDataFolder() {
        return new File(".");
    }

    @Override
    public PluginDescriptionFile getDescription() {
        return description;
    }

    @Override
    public FileConfiguration getConfig() {
        throw new UnsupportedOperationException("MockPlugin#getConfig()");
    }

    @Override
    public InputStream getResource(String filename) {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }

    @Override
    public void saveConfig() {
        //not implemented
    }

    @Override
    public void saveDefaultConfig() {
        //not implemented
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        //not implemented
    }

    @Override
    public void reloadConfig() {
        //not implemented
    }

    @Override
    public PluginLoader getPluginLoader() {
        return server.getPluginLoader();
    }

    @Override
    public MockServer getServer() {
        return server;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void onDisable() {
        //no-op
    }

    @Override
    public void onLoad() {
        //no-op
    }

    @Override
    public void onEnable() {
        //no-op
    }

    @Override
    public boolean isNaggable() {
        return false;
    }

    @Override
    public void setNaggable(boolean canNag) {
        //leave me alone
    }

    @Override
    public EbeanServer getDatabase() {
        throw new UnsupportedOperationException("MockPlugin#getDatabase()");
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        throw new UnsupportedOperationException("MockPlugin#getDefaultWorldGenerator()");
    }

    @Override
    public Logger getLogger() {
        return server.getLogger();
    }

    @Override
    public String getName() {
        return getDescription().getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        throw new UnsupportedOperationException("MockPlugin#onCommand()");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
