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

package li.l1t.common.inventory.gui;

import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import li.l1t.common.inventory.gui.util.InvMenuListener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

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
