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

package li.l1t.common.inventory.gui.util;

import li.l1t.common.inventory.gui.InventoryMenu;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

/**
 * Global listener for all actions on inventory menus. Menus should call {@link
 * InvMenuListener#register(InventoryMenu)} to make sure the listener is registered and actually
 * forwards events to the menu.
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
            try {
                evt.setCancelled(((InventoryMenu) holder).handleClick(evt));
            } catch (Throwable t) { //e.g. ClassNotFoundError - want to be certain that players can't steal items out of readonly inventories
                Bukkit.getLogger().warning("Error handling inventory menu click:");
                t.printStackTrace();
                evt.setCancelled(true);
            }
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
