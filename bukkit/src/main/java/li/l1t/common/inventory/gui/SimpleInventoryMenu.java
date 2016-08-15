/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.inventory.gui;

import com.google.common.base.Preconditions;
import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.holder.SimpleElementHolder;
import li.l1t.common.inventory.gui.util.InvMenuListener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * A simple implementation of an inventory menu that renders a graphical user interface into a
 * Minecraft inventory. This implementation does not permit hotbar swap. Click events are {@link
 * MenuElement#handleMenuClick(InventoryClickEvent, InventoryMenu)} forwarded} to elements, monitor
 * click and close are ignored. The inventory is created lazily to make sure the inventory title is
 * available.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public class SimpleInventoryMenu extends SimpleElementHolder implements InventoryMenu {
    private final Plugin plugin;
    private String inventoryTitle;
    private Inventory inventory;
    private Player player;

    /**
     * Creates a new abstract inventory menu.
     *
     * @param plugin         the plugin associated with this menu, may not be null
     * @param inventoryTitle the title of the inventory, may not be null
     * @param player         the player associated with this menu, may not be null
     */
    protected SimpleInventoryMenu(Plugin plugin, String inventoryTitle, Player player) {
        this(plugin, player);
        this.inventoryTitle = Preconditions.checkNotNull(inventoryTitle, "inventoryTitle");
    }

    /**
     * Creates a new abstract inventory menu without a title. When using this constructor, {@link
     * #getInventoryTitle()} must be overridden.
     *
     * @param plugin the plugin associated with this menu, may not be null
     * @param player the player associated with this menu, may not be null
     */
    protected SimpleInventoryMenu(Plugin plugin, Player player) {
        this.plugin = Preconditions.checkNotNull(plugin, "plugin");
        this.player = Preconditions.checkNotNull(player, "player");
        InvMenuListener.register(this);
    }

    @Override
    public void redraw() {
        Inventory inv = getInventory();
        MenuElement[] elementsRaw = getElementsRaw();
        for (int slotId = 0; slotId < INVENTORY_SIZE; slotId++) {
            MenuElement element = elementsRaw[slotId];
            if (element == null) {
                continue;
            }
            ItemStack stack = element.draw(this);
            if (stack == null) {
                stack = placeholder.createStack();
            }
            inv.setItem(slotId, stack);
        }
    }

    @Override
    public void open() {
        redraw();
        if (getPlayer().getOpenInventory() != null) {
            getPlayer().closeInventory();
        }

        getPlayer().openInventory(getInventory());
    }

    @Override
    public void handleClickMonitor(InventoryClickEvent evt) {
        //no-op
    }

    @Override
    @SuppressWarnings("unchecked") //Java needs self-types for generics
    public boolean handleClick(InventoryClickEvent evt) {
        int slotId = evt.getSlot();
        if (isOccupied(slotId)) {
            getElementRaw(slotId).handleMenuClick(evt, this);
        }
        return true;
    }

    @Override
    public void handleClose(InventoryCloseEvent evt) {
        //no-op
    }

    @Override
    public boolean permitsHotbarSwap(InventoryClickEvent evt) {
        return false;
    }

    @Override
    public Inventory getInventory() {
        //lazy init to circumvent title possibly being unknown at creation time if overridden
        if (inventory == null) {
            inventory = getPlugin().getServer()
                    .createInventory(this, INVENTORY_SIZE, getInventoryTitle());
        }
        return inventory;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getInventoryTitle() {
        if (inventoryTitle == null) {
            throw new AssertionError("If no inventory title is passed, " +
                    "SimpleInventoryMenu#getInventoryTitle() must be overridden! (" + getClass().getName() + ")");
        }
        return inventoryTitle;
    }
}
