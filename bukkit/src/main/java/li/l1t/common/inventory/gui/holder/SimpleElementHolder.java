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
import li.l1t.common.inventory.gui.element.Placeholder;
import li.l1t.common.util.inventory.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Simple implementation of an element holder with no limits on rendering. Provides strong
 * access to internal workings to subclasses.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public class SimpleElementHolder implements ElementHolder {
    private static final ItemStackFactory PLACEHOLDER_FACTORY =
            new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8))
                    .displayName(" --- ");
    protected Placeholder placeholder;
    private MenuElement[] elements = new MenuElement[INVENTORY_SIZE];

    /**
     * Creates a new simple element holder.
     */
    public SimpleElementHolder() {
        this.placeholder = new Placeholder(getPlaceholderFactory().produce());
    }

    public boolean isOccupied(int slotId) {
        return getElementRaw(slotId) != null;
    }

    public void addElement(int slotId, MenuElement element) {
        checkSlotId(slotId);
        setElementRaw(slotId, element);
    }

    public MenuElement getElement(int slotId) {
        checkSlotId(slotId);
        return getElementRaw(slotId);
    }

    public MenuElement[] getElements() {
        return Arrays.copyOf(elements, elements.length);
    }

    public void clear() {
        elements = new MenuElement[INVENTORY_SIZE];
    }

    public void addPlaceholder(int slotId) {
        addElement(slotId, placeholder);
    }

    /**
     * Returns the item stack factory used to create the placeholder instance. Note that this is
     * only called once, from the constructor.
     *
     * @return the item stack factory used to create the placeholder
     */
    protected ItemStackFactory getPlaceholderFactory() {
        return PLACEHOLDER_FACTORY;
    }

    /**
     * Gets an element by given id. Works like {@link #getElement(int)}, but does not do bounds
     * checking.
     *
     * @param slotId the slot id to get the element for
     * @return the element, or null if that slot is unoccupied
     */
    protected MenuElement getElementRaw(int slotId) {
        return elements[slotId];
    }

    /**
     * Sets an element directly. Works like {@link #addElement(int, MenuElement)}, but without
     * bounds checking.
     *
     * @param slotId  the id of the slot
     * @param element the element, or null to remove any elements from that slot
     */
    protected void setElementRaw(int slotId, MenuElement element) {
        elements[slotId] = element;
    }

    /**
     * @return a direct reference to the elements of this menu
     */
    protected MenuElement[] getElementsRaw() {
        return elements;
    }

    /**
     * Checks if a slot id is in the valid range, that is, between zero (inclusive) and the
     * {@link #INVENTORY_SIZE inventory size}, exclusive.
     *
     * @param slotId the id to check
     * @throws IllegalArgumentException if the slot id is out of bounds
     */
    protected void checkSlotId(int slotId) {
        if (slotId >= INVENTORY_SIZE || slotId < 0) {
            throw new IndexOutOfBoundsException("invalid slot id: " + slotId);
        }
    }
}
