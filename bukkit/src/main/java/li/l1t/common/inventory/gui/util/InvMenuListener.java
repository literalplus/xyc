/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.inventory.gui.util;

import li.l1t.common.inventory.gui.InventoryMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

/**
 * Global listener for all actions on inventory menus. Menus should call
 * {@link InvMenuListener#register(InventoryMenu)} to make sure the listener is registered and
 * actually forwards events to the menu.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public class InvMenuListener implements Listener {
    private static boolean registered;

    private InvMenuListener() {

    }

    /**
     * Makes sure there is a listener in place to notify given menu about events.
     *
     * @param menu the menu requesting the action
     */
    public static void register(InventoryMenu menu) {
        if (!registered) {
            Plugin plugin = menu.getPlugin();
            plugin.getServer().getPluginManager().registerEvents(new InvMenuListener(), plugin);
            registered = true;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent evt) {
        if (evt.getClickedInventory() == null) {
            return;
        }

        InventoryHolder holder = evt.getClickedInventory().getHolder();
        if (holder instanceof InventoryMenu) {
            evt.setCancelled(((InventoryMenu) holder).handleClick(evt));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClickMonitor(InventoryClickEvent evt) {
        if (evt.getClickedInventory() == null) {
            return;
        }

        InventoryHolder holder = evt.getClickedInventory().getHolder();
        if (holder instanceof InventoryMenu) {
            ((InventoryMenu) holder).handleClickMonitor(evt);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClose(InventoryCloseEvent evt) {
        if (evt.getInventory() == null) {
            return;
        }

        InventoryHolder holder = evt.getInventory().getHolder();
        if (holder instanceof InventoryMenu) {
            ((InventoryMenu) holder).handleClose(evt);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryHotbarSwap(InventoryClickEvent evt) {
        if (evt.getClickedInventory() == null) {
            return;
        }

        InventoryHolder holder = evt.getView().getTopInventory().getHolder();
        if (holder instanceof InventoryMenu) {
            switch (evt.getAction()) {
                case HOTBAR_MOVE_AND_READD:
                case HOTBAR_SWAP:
                case MOVE_TO_OTHER_INVENTORY:
                    evt.setCancelled(!((InventoryMenu) holder).permitsHotbarSwap(evt));
            }
        }
    }
}
