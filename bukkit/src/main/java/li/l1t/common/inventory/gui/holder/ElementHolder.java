/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.inventory.gui.holder;

import li.l1t.common.inventory.gui.element.MenuElement;
import li.l1t.common.inventory.gui.exception.IllegalPositionException;

/**
 * Holds an array of menu elements for an inventory. Implementations must not necessarily write
 * the elements into an inventory directly, but can also, for example, provide templates.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public interface ElementHolder {
    /**
     * The amount of slots in a row.
     */
    int ROW_SIZE = 9;
    /**
     * The amount of rows.
     */
    int ROW_COUNT = 6;
    /**
     * The total count of slots in the inventory.
     */
    int INVENTORY_SIZE = ROW_COUNT * ROW_SIZE;

    /**
     * Checks whether there is currently an item in a slot of the inventory.
     *
     * @param slotId the id of the slot to check
     * @return whether there is currently an item rendered in given slot
     * @throws IndexOutOfBoundsException if slotId > {@value #INVENTORY_SIZE}
     */
    boolean isOccupied(int slotId);

    /**
     * Renders a practically invisible placeholder item in a slot of the inventory.
     *
     * @param slotId the id of the slot to add the placeholder to
     * @throws IllegalPositionException  if drawing in given slot is prohibited
     * @throws IndexOutOfBoundsException if slotId > {@value #INVENTORY_SIZE}
     */
    void addPlaceholder(int slotId);

    /**
     * Renders a dynamic clickable element in the menu. If the element is null, remove any
     * previous elements in that slot.
     *
     * @param slotId  the id of the slot to render in
     * @param element the element to render, may be null to remove
     * @throws IllegalPositionException  if drawing in given slot is prohibited
     * @throws IndexOutOfBoundsException if slotId > {@value #INVENTORY_SIZE}
     */
    void addElement(int slotId, MenuElement element);

    /**
     * Gets the element currently in given slot, or null if the slot is currently unoccupied.
     *
     * @param slotId the id of the slot to get the element for
     * @return the element currently in the slot
     */
    MenuElement getElement(int slotId);

    /**
     * @return a copy of the current elements of this menu
     */
    MenuElement[] getElements();

    /**
     * Forces the menu to forget all contents and redraw itself to the default state.
     */
    void clear();
}
