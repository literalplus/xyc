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

import li.l1t.common.inventory.SlotPosition;
import li.l1t.common.inventory.gui.element.MenuElement;

import java.util.BitSet;

/**
 * An element holder that works as a template for creating inventory menus.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public class TemplateElementHolder extends SimpleElementHolder {
    private final BitSet placeholderSlots = new BitSet(INVENTORY_SIZE);

    /**
     * Copies all elements from this template into a holder, setting elements directly instead of
     * through the API, bypassing unnecessary bound-checks. If the handler has special handling when
     * an element gets added, this might break that handling.
     *
     * @param holder the holder to copy into
     * @param <T>    the type of the holder
     * @return {@code holder}, for easy chaining
     * @see #apply(ElementHolder) The API-only counterpart of this method
     */
    public <T extends SimpleElementHolder> T applyRaw(T holder) {
        MenuElement[] elements = getElements();
        for (int slotId = 0; slotId < elements.length; slotId++) {
            if (placeholderSlots.get(slotId)) {
                holder.addPlaceholder(slotId);
            } else {
                MenuElement element = elements[slotId];
                holder.setElementRaw(slotId, element);
            }
        }
        return holder;
    }

    /**
     * Copies all elements from this template into a holder, using the public API.
     *
     * @param holder the holder to copy into
     * @param <T>    the type of the holder
     * @return {@code holder}, for easy chaining
     * @see #applyRaw(SimpleElementHolder) The raw counterpart of this method
     */
    public <T extends ElementHolder> T apply(T holder) {
        MenuElement[] elements = getElements();
        for (int slotId = 0; slotId < elements.length; slotId++) {
            if (placeholderSlots.get(slotId)) {
                holder.addPlaceholder(slotId);
            } else {
                MenuElement element = elements[slotId];
                holder.addElement(slotId, element);
            }
        }
        return holder;
    }

    /**
     * Copies some elements from this template into a holder, using the public API. Does not override
     * elements that are already defined in the holder, and does not apply elements if they are
     * null in this template.
     *
     * @param holder the holder to copy into
     * @param <T>    the type of the holder
     * @return {@code holder}, for easy chaining
     */
    public <T extends ElementHolder> T applySoft(T holder) {
        MenuElement[] elements = getElements();
        for (int slotId = 0; slotId < elements.length; slotId++) {
            if (!holder.isOccupied(slotId)) {
                if (placeholderSlots.get(slotId)) {
                    holder.addPlaceholder(slotId);
                } else {
                    MenuElement element = elements[slotId];
                    if(element != null) {
                        holder.addElement(slotId, element);
                    }
                }
            }
        }
        return holder;
    }

    @Override
    public void addPlaceholder(int slotId) {
        placeholderSlots.set(slotId);
    }

    @Override
    public boolean isOccupied(int slotId) {
        return super.isOccupied(slotId) || placeholderSlots.get(slotId);
    }

    /**
     * Checks whether this template has a placeholder at given slot id.
     *
     * @param slotId the slot id to check
     * @return whether there is a placeholder in given slot
     */
    public boolean hasPlaceholderAt(int slotId) {
        return placeholderSlots.get(slotId);
    }

    /**
     * Checks whether this template has a placeholder in given slot.
     *
     * @param slot the slot to check
     * @return whether there is a placeholder in given slot
     */
    public boolean hasPlaceholderAt(SlotPosition slot) {
        return placeholderSlots.get(slot.toSlotId());
    }

}
