/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.inventory.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

import io.github.xxyy.common.inventory.gui.element.MenuElement;
import io.github.xxyy.common.inventory.gui.holder.ElementHolder;
import io.github.xxyy.common.inventory.gui.util.InvMenuListener;

/**
 * Represents an inventory menu that renders a graphical user interface into a
 * Minecraft inventory.
 * <p><b>Note:</b> To make sure your menu actually gets notified about events, call
 * {@link InvMenuListener#register(InventoryMenu) Ghostbusters}.</p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public interface InventoryMenu extends InventoryHolder, ElementHolder {
    /**
     * @return the player associated with this inventory
     */
    Player getPlayer();

    /**
     * @return the plugin associated with this inventory
     */
    Plugin getPlugin();

    /**
     * Forces the menu to redraw itself and all {@link MenuElement#draw(ElementHolder)} elements}.
     */
    void redraw();

    /**
     * Opens this menu to the associated player, closing the inventory they had previously open,
     * if any.
     */
    void open();

    /**
     * Handles a click on this menu.
     *
     * @param evt the event causing the click
     * @return whether the event should be cancelled
     */
    boolean handleClick(InventoryClickEvent evt);

    /**
     * Handles a click on this menu on the {@link org.bukkit.event.EventPriority#MONITOR} priority.
     * Note that this only gets called if {@link #handleClick(InventoryClickEvent)} hasn't cancelled
     * the event. This is useful for when the action needs to actually be performed before it can
     * be handled.
     *
     * @param evt the event causing the click
     */
    void handleClickMonitor(InventoryClickEvent evt);

    /**
     * Handles this inventory being closed.
     *
     * @param evt the event causing the close
     */
    void handleClose(InventoryCloseEvent evt);

    /**
     * Figures out whether this menu permits hotbar swap for a given event. This is also called
     * for moving to the other inventory using shift.
     *
     * @param evt the event to process
     * @return whether hotbar swap is permitted for that event
     */
    boolean permitsHotbarSwap(InventoryClickEvent evt);

    /**
     * @return the title to be shown to a player viewing this inventory
     */
    String getInventoryTitle();
}
