/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.common.test.util.mokkit;

import com.avaje.ebean.config.ServerConfig;
import li.l1t.common.test.util.MockHelper;
import org.bukkit.BanList;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.UnsafeValues;
import org.bukkit.Warning;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.SimpleServicesManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;

/**
 * Mock impl of a server which returns useful dataa for some methods, but null-ish stuff for most.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-14
 */
public class MockServer implements Server {
    private final Logger logger = Logger.getLogger(MockServer.class.getName());
    private final Collection<Player> onlinePlayers = new CopyOnWriteArrayList<>();
    private List<Recipe> recipes = new ArrayList<>();
    private int idleTimeout;
    private Spigot spigot = new Spigot();
    private SimpleCommandMap commandMap = new SimpleCommandMap(this);
    private SimplePluginManager pluginManager = new SimplePluginManager(this, commandMap);
    @SuppressWarnings("deprecation")
    private JavaPluginLoader pluginLoader = new JavaPluginLoader(this);
    private SimpleServicesManager servicesManager = new SimpleServicesManager();

    @Override
    public String getName() {
        return "Spagt";
    }

    @Override
    public String getVersion() {
        return "infinity";
    }

    @Override
    public String getBukkitVersion() {
        return ">implying bukkit";
    }

    @Override
    public Player[] _INVALID_getOnlinePlayers() {
        return onlinePlayers.toArray(new Player[onlinePlayers.size()]);
    }

    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        return onlinePlayers;
    }

    /**
     * Sets the online players, removing any existing ones.
     *
     * @param players the players
     * @return this server
     */
    public MockServer setOnlinePlayers(Player... players) {
        return setOnlinePlayers(Arrays.asList(players));
    }

    /**
     * Sets the online players, removing any existing ones.
     *
     * @param players the players
     * @return this server
     */
    public MockServer setOnlinePlayers(Collection<Player> players) {
        onlinePlayers.clear();
        onlinePlayers.addAll(players);
        return this;
    }

    /**
     * Adds some online players.
     *
     * @param players the players
     * @return this server
     */
    public MockServer addOnlinePlayers(Player... players) {
        onlinePlayers.addAll(Arrays.asList(players));
        return this;
    }

    @Override
    public int getMaxPlayers() {
        return 420;
    }

    @Override
    public int getPort() {
        return 1337;
    }

    @Override
    public int getViewDistance() {
        return 4;
    }

    @Override
    public String getIp() {
        return "127.0.0.1";
    }

    @Override
    public String getServerName() {
        return "xyc-craft";
    }

    @Override
    public String getServerId() {
        return "xyc-craft";
    }

    @Override
    public String getWorldType() {
        return "DEFAULT";
    }

    @Override
    public boolean getGenerateStructures() {
        return true;
    }

    @Override
    public boolean getAllowEnd() {
        return true;
    }

    @Override
    public boolean getAllowNether() {
        return true;
    }

    @Override
    public boolean hasWhitelist() {
        return false;
    }

    @Override
    public void setWhitelist(boolean value) {
        //not implemented
    }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        return Collections.emptySet();
    }

    @Override
    public void reloadWhitelist() {
        //not implemented
    }

    @Override
    public int broadcastMessage(String message) {
        logger.info("broadcastMessage(" + message + ")");
        return getOnlinePlayers().size();
    }

    @Override
    public String getUpdateFolder() {
        return "updates";
    }

    @Override
    public File getUpdateFolderFile() {
        return new File(getUpdateFolder());
    }

    @Override
    public long getConnectionThrottle() {
        return -1;
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        return 20;
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        return 20;
    }

    @Override
    public Player getPlayer(String name) {
        String lowerName = name.toLowerCase();
        Player playerExact = getPlayerExact(lowerName);
        if (playerExact != null) {
            return playerExact;
        }
        return onlinePlayers.stream()
                .filter(plr -> plr.getName().toLowerCase().startsWith(lowerName))
                .findFirst().orElse(null);
    }

    @Override
    public Player getPlayerExact(String name) {
        String lowerName = name.toLowerCase();
        return onlinePlayers.stream()
                .filter(plr -> plr.getName().equals(lowerName))
                .findFirst().orElse(null);
    }

    @Override
    public List<Player> matchPlayer(String name) {
        String lowerName = name.toLowerCase();
        return onlinePlayers.stream()
                .filter(plr -> plr.getName().toLowerCase().startsWith(lowerName))
                .collect(Collectors.toList());
    }

    @Override
    public Player getPlayer(UUID id) {
        return onlinePlayers.stream()
                .filter(plr -> plr.getUniqueId().equals(id))
                .findFirst().orElse(null);
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public BukkitScheduler getScheduler() {
        throw new UnsupportedOperationException("MockServer#getScheduler()");
    }

    @Override
    public ServicesManager getServicesManager() {
        return servicesManager;
    }

    @Override
    public List<World> getWorlds() {
        throw new UnsupportedOperationException("MockServer#getWorlds()");
    }

    @Override
    public World createWorld(WorldCreator creator) {
        throw new UnsupportedOperationException("MockServer#createWorld()");
    }

    @Override
    public boolean unloadWorld(String name, boolean save) {
        return true;
    }

    @Override
    public boolean unloadWorld(World world, boolean save) {
        return true;
    }

    @Override
    public World getWorld(String name) {
        throw new UnsupportedOperationException("MockServer#getWorld()");
    }

    @Override
    public World getWorld(UUID uid) {
        throw new UnsupportedOperationException("MockServer#getWorld()");
    }

    @Override
    public MapView getMap(short id) {
        throw new UnsupportedOperationException("MockServer#getMap()");
    }

    @Override
    public MapView createMap(World world) {
        throw new UnsupportedOperationException("MockServer#createMap()");
    }

    @Override
    public void reload() {
        //lol
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public PluginCommand getPluginCommand(String name) {
        return (PluginCommand) commandMap.getCommand(name);
    }

    @Override
    public void savePlayers() {
        //not implemented
    }

    @Override
    public boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException {
        return commandMap.dispatch(sender, commandLine);
    }

    @Override
    public void configureDbConfig(ServerConfig config) {
        //not implemented
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        return recipes.add(recipe);
    }

    @Override
    public List<Recipe> getRecipesFor(ItemStack result) {
        return recipes.stream()
                .filter(recipe -> recipe.getResult().equals(result))
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<Recipe> recipeIterator() {
        return recipes.iterator();
    }

    @Override
    public void clearRecipes() {
        recipes.clear();
    }

    @Override
    public void resetRecipes() {
        clearRecipes();
    }

    @Override
    public Map<String, String[]> getCommandAliases() {
        throw new UnsupportedOperationException("MockServer#getCommandAliases()");
    }

    @Override
    public int getSpawnRadius() {
        return 1;
    }

    @Override
    public void setSpawnRadius(int value) {
        //not implemented
    }

    @Override
    public boolean getOnlineMode() {
        return false;
    }

    @Override
    public boolean getAllowFlight() {
        return false;
    }

    @Override
    public boolean isHardcore() {
        return false;
    }

    @Override
    public boolean useExactLoginLocation() {
        return false;
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("MockServer#shutdown()");
    }

    @Override
    public int broadcast(String message, String permission) {
        return broadcastMessage(message); //not exactly what we're supposed to do, but good enough
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String name) {
        throw new UnsupportedOperationException("MockServer#getOfflinePlayer()");
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID id) {
        throw new UnsupportedOperationException("MockServer#getOfflinePlayer()");
    }

    @Override
    public Set<String> getIPBans() {
        return Collections.emptySet();
    }

    @Override
    public void banIP(String address) {
        //not implemented
    }

    @Override
    public void unbanIP(String address) {
        //not implemented
    }

    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        return Collections.emptySet();
    }

    @Override
    public BanList getBanList(BanList.Type type) {
        throw new UnsupportedOperationException("MockServer#getBanList()");
    }

    @Override
    public Set<OfflinePlayer> getOperators() {
        return Collections.emptySet();
    }

    @Override
    public GameMode getDefaultGameMode() {
        return GameMode.SURVIVAL;
    }

    @Override
    public void setDefaultGameMode(GameMode mode) {
        //not implemented
    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return (ConsoleCommandSender) MockHelper.loggerSender(mock(ConsoleCommandSender.class), logger);
    }

    @Override
    public File getWorldContainer() {
        return new File("worlds");
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        throw new UnsupportedOperationException("MockServer#getOfflinePlayers()");
    }

    @Override
    public Messenger getMessenger() {
        throw new UnsupportedOperationException("MockServer#getMessenger()");
    }

    @Override
    public HelpMap getHelpMap() {
        throw new UnsupportedOperationException("MockServer#getHelpMap()");
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return createInventory(owner, type, type.getDefaultTitle());
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        int size = type == InventoryType.CHEST ? 27 : type.getDefaultSize();
        return createInventory(owner, size, title);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        return createInventory(owner, size, InventoryType.CHEST.getDefaultTitle());
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
        return new MockInventory(size, title, owner);
    }

    @Override
    public int getMonsterSpawnLimit() {
        return 50;
    }

    @Override
    public int getAnimalSpawnLimit() {
        return 40;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return 5;
    }

    @Override
    public int getAmbientSpawnLimit() {
        return 5;
    }

    @Override
    public boolean isPrimaryThread() {
        return true; //yolo?
    }

    @Override
    public String getMotd() {
        return "Spagt test server";
    }

    @Override
    public String getShutdownMessage() {
        return "This shouldn't have happened.";
    }

    @Override
    public Warning.WarningState getWarningState() {
        return Warning.WarningState.DEFAULT;
    }

    @Override
    public ItemFactory getItemFactory() {
        return new MockItemFactory();
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        throw new UnsupportedOperationException("MockServer#getScoreboardManager()");
    }

    @Override
    public CachedServerIcon getServerIcon() {
        throw new UnsupportedOperationException("MockServer#getServerIcon()");
    }

    @Override
    public CachedServerIcon loadServerIcon(File file) throws Exception {
        throw new UnsupportedOperationException("MockServer#loadServerIcon()");
    }

    @Override
    public CachedServerIcon loadServerIcon(BufferedImage image) throws Exception {
        throw new UnsupportedOperationException("MockServer#loadServerIcon()");
    }

    @Override
    public int getIdleTimeout() {
        return idleTimeout;
    }

    @Override
    public ChunkGenerator.ChunkData createChunkData(World world) {
        throw new UnsupportedOperationException("MockServer#createChunkData(" + world + ")");
    }

    @Override
    public void setIdleTimeout(int threshold) {
        this.idleTimeout = threshold;
    }

    @Override
    @SuppressWarnings("deprecation")
    public UnsafeValues getUnsafe() {
        throw new UnsupportedOperationException("MockServer#getUnsafe()");
    }

    @Override
    public Spigot spigot() {
        return spigot;
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        throw new UnsupportedOperationException("MockServer#sendPluginMessage()");
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return Collections.emptySet();
    }

    public void reset() {
        this.onlinePlayers.clear();
        this.clearRecipes();
    }

    public JavaPluginLoader getPluginLoader() {
        return pluginLoader;
    }
}
