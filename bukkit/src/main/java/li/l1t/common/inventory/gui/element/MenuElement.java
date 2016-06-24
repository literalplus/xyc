/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.inventory.gui.element;

import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a clickable element in an inventory menu, shown to the user as an item stack.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-04-17
 */
public interface MenuElement {
    /**
     * @param menu the menu requesting to display this element
     * @return the item stack representing this element, or null if this element is hidden
     */
    ItemStack draw(ElementHolder menu);

    /**
     * Handles a click on this element.
     *
     * @param evt  the Bukkit event that caused the click
     * @param menu the menu associated with the click
     */
    void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu);
}
