/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.xxyy.common.tests.implementations;

import com.avaje.ebean.config.ServerConfig;
import io.github.xxyy.common.XycConstants;
import org.bukkit.*;
import org.bukkit.Warning.WarningState;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author xxyy98 (http://xxyy.github.io/)
 */
public class TestServer implements Server {

    private TestConsoleCommandSender consoleSender = new TestConsoleCommandSender(this);

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public String getVersion() {
        return XycConstants.versionString;
    }

    @Override
    public String getBukkitVersion() {
        return "timeless";
    }

    @Override
    public Player[] getOnlinePlayers() {
        return new Player[0];
    }

    @Override
    public int getMaxPlayers() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public int getPort() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public int getViewDistance() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public String getIp() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public String getServerName() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public String getServerId() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public String getWorldType() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean getGenerateStructures() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean getAllowEnd() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean getAllowNether() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean hasWhitelist() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void setWhitelist(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void reloadWhitelist() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public int broadcastMessage(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public String getUpdateFolder() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public File getUpdateFolderFile() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public long getConnectionThrottle() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Player getPlayer(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Player getPlayerExact(String string) {
        return null;
    }

    @Override
    public List<Player> matchPlayer(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public PluginManager getPluginManager() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public BukkitScheduler getScheduler() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public ServicesManager getServicesManager() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public List<World> getWorlds() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public World createWorld(WorldCreator wc) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean unloadWorld(String string, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean unloadWorld(World world, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public World getWorld(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public World getWorld(UUID uuid) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public MapView getMap(short s) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public MapView createMap(World world) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    @Override
    public PluginCommand getPluginCommand(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void savePlayers() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean dispatchCommand(CommandSender cs, String string) throws CommandException {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void configureDbConfig(ServerConfig sc) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public List<Recipe> getRecipesFor(ItemStack is) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Iterator<Recipe> recipeIterator() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void clearRecipes() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void resetRecipes() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Map<String, String[]> getCommandAliases() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public int getSpawnRadius() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void setSpawnRadius(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean getOnlineMode() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean getAllowFlight() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean isHardcore() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean useExactLoginLocation() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public int broadcast(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Set<String> getIPBans() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void banIP(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void unbanIP(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public BanList getBanList(BanList.Type type) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Set<OfflinePlayer> getOperators() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public GameMode getDefaultGameMode() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void setDefaultGameMode(GameMode gm) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return consoleSender;
    }

    @Override
    public File getWorldContainer() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Messenger getMessenger() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public HelpMap getHelpMap() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Inventory createInventory(InventoryHolder ih, InventoryType it) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Inventory createInventory(InventoryHolder ih, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Inventory createInventory(InventoryHolder ih, int i, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public int getMonsterSpawnLimit() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public int getAnimalSpawnLimit() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public int getAmbientSpawnLimit() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public boolean isPrimaryThread() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public String getMotd() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public String getShutdownMessage() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public WarningState getWarningState() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public ItemFactory getItemFactory() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public CachedServerIcon getServerIcon() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public CachedServerIcon loadServerIcon(BufferedImage bufferedImage) throws IllegalArgumentException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public void setIdleTimeout(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public int getIdleTimeout() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public UnsafeValues getUnsafe() {
        return null;
    }

    @Override
    public void sendPluginMessage(Plugin plugin, String string, byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        throw new UnsupportedOperationException("Not supported yet."); //TODO not yet implemented overriding method
    }
}
