/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.games.kits.inventory;

import li.l1t.common.games.kits.Kit;
import li.l1t.common.games.kits.KitManager;
import li.l1t.common.games.kits.objective.ParentObjectiveResolver;
import li.l1t.common.util.inventory.InventoryHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

/**
 * A class that provides an inventory used to select kits.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class KitSelector implements Listener, InventoryHolder {
    private final Plugin plugin;
    private final KitManager manager;
    private final Player player;
    private final Kit[] options;
    private final Consumer<OptionClickEvent> clickHandler;
    private final Inventory inventory;

    protected KitSelector(Plugin plugin, KitManager manager, Player player,
                          Consumer<OptionClickEvent> clickHandler, String name) {
        this.plugin = plugin;
        this.manager = manager;
        this.player = player;
        this.clickHandler = clickHandler;
        int size = InventoryHelper.validateInventorySize(manager.getKits().size());
        this.inventory = Bukkit.createInventory(this, size, name);
        this.options = this.manager.getKits().toArray(new Kit[size]);
        populateInventory();
    }

    /**
     * Opens a new kit selector for a player.
     *
     * @param plr the player to open the selector for
     */
    public static KitSelector open(Plugin plugin, KitManager manager, Player plr, String name,
                                   Consumer<OptionClickEvent> clickHandler) {
        KitSelector selector = new KitSelector(plugin, manager, plr, clickHandler, name);
        KitSelectorListener.registerIfNecessary(plugin);
        plr.openInventory(selector.getInventory());
        return selector;
    }

    private void populateInventory() {
        ParentObjectiveResolver resolver = manager.getObjectiveResolver();

        for (int i = 0; i < options.length; i++) {
            Kit kit = options[i];
            if (kit != null && kit.getIconStack() != null) {
                if (kit.hasObjective() && !resolver.isCompleted(kit.getObjective(), player)) {
                    inventory.setItem(i, resolver.getUnavailIcon(kit.getObjective()));
                } else {
                    inventory.setItem(i, kit.getIconStack());
                }
            }
        }
    }

    Kit getKitAt(int slotId) {
        return options[slotId];
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Player getPlayer() {
        return player;
    }

    public KitManager getManager() {
        return manager;
    }

    public Kit[] getOptions() {
        return options;
    }

    public Consumer<OptionClickEvent> getClickHandler() {
        return clickHandler;
    }
}
