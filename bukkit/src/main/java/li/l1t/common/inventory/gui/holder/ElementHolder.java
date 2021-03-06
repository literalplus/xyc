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

package li.l1t.common.inventory.gui.holder;

import li.l1t.common.inventory.SlotPosition;
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
     * @throws IndexOutOfBoundsException if slotId &gt; {@value #INVENTORY_SIZE}
     */
    boolean isOccupied(int slotId);

    /**
     * Checks whether there is currently an item in a slot of the inventory.
     *
     * @param slot the position of the slot to check
     * @return whether there is currently an item rendered in given slot
     * @throws IndexOutOfBoundsException if slotId &gt; {@value #INVENTORY_SIZE}
     */
    boolean isOccupied(SlotPosition slot);


    /**
     * Renders a practically invisible placeholder item in a slot of the inventory.
     *
     * @param slotId the id of the slot to add the placeholder to
     * @throws IllegalPositionException  if drawing in given slot is prohibited
     * @throws IndexOutOfBoundsException if slotId &gt; {@value #INVENTORY_SIZE}
     */
    void addPlaceholder(int slotId);

    /**
     * Renders a practically invisible placeholder item in a slot of the inventory.
     *
     * @param slot the position of the slot to add the placeholder to
     * @throws IllegalPositionException  if drawing in given slot is prohibited
     * @throws IndexOutOfBoundsException if slotId &gt; {@value #INVENTORY_SIZE}
     */
    void addPlaceholder(SlotPosition slot);


    /**
     * Renders a dynamic clickable element in the menu. If the element is null, remove any
     * previous elements in that slot.
     *
     * @param slotId  the id of the slot to render in
     * @param element the element to render, may be null to remove
     * @throws IllegalPositionException  if drawing in given slot is prohibited
     * @throws IndexOutOfBoundsException if slotId &gt; {@value #INVENTORY_SIZE}
     */
    void addElement(int slotId, MenuElement element);

    /**
     * Renders a dynamic clickable element in the menu. If the element is null, remove any
     * previous elements in that slot.
     *
     * @param slot    the slot of the slot to render in
     * @param element the element to render, may be null to remove
     * @throws IllegalPositionException  if drawing in given slot is prohibited
     * @throws IndexOutOfBoundsException if slotId &gt; {@value #INVENTORY_SIZE}
     */
    void addElement(SlotPosition slot, MenuElement element);


    /**
     * Gets the element currently in given slot, or null if the slot is currently unoccupied.
     *
     * @param slotId the id of the slot to get the element for
     * @return the element currently in the slot
     */
    MenuElement getElement(int slotId);

    /**
     * Gets the element currently in given slot, or null if the slot is currently unoccupied.
     *
     * @param slot the position of the slot to get the element for
     * @return the element currently in the slot
     */
    MenuElement getElement(SlotPosition slot);


    /**
     * @return a copy of the current elements of this menu
     */
    MenuElement[] getElements();

    /**
     * Forces the menu to forget all contents and redraw itself to the default state.
     */
    void clear();
}
