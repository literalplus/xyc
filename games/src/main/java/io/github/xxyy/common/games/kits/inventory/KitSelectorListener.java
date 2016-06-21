/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.games.kits.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

import io.github.xxyy.common.games.kits.Kit;
import io.github.xxyy.common.games.kits.objective.ParentObjectiveResolver;

/**
 * Listens to events related to kit selectors.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-21
 */
class KitSelectorListener implements Listener {
    private static boolean registered = false;

    private KitSelectorListener() {

    }

    static void registerIfNecessary(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new KitSelectorListener(), plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof KitSelector)) {
            return;
        }
        event.setCancelled(true);
        KitSelector selector = (KitSelector) holder;
        Player player = selector.getPlayer();
        int slotId = event.getRawSlot();
        ParentObjectiveResolver resolver = selector.getManager().getObjectiveResolver();

        if (slotId >= 0) {
            Kit kit = selector.getKitAt(slotId);
            boolean cancelled = !resolver.isCompleted(kit.getObjective(), player);
            OptionClickEvent clickEvent = new OptionClickEvent(
                    (Player) event.getWhoClicked(), selector, kit
            );
            selector.getClickHandler().accept(clickEvent);

            if (clickEvent.willClose()) {
                selector.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(
                        selector.getPlugin(), player::closeInventory, 1
                );
            }
            if (!clickEvent.isCancelled()) {
                selector.getManager().apply(kit, player);
            }
        }
    }
}
