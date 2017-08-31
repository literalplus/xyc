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
