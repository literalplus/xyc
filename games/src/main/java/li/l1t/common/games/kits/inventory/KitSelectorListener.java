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

package li.l1t.common.games.kits.inventory;

import li.l1t.common.games.kits.Kit;
import li.l1t.common.games.kits.objective.ParentObjectiveResolver;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

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
