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

import com.google.common.base.Preconditions;
import li.l1t.common.inventory.gui.element.MenuElement;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * An inventory menu that behaves just like {@link SimpleInventoryMenu}, but also provides a
 * dedicated API for providing control buttons in the topmost row of the inventory. The top row is
 * filled with placeholders by default.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-15
 */
public abstract class TopRowMenu extends SimpleInventoryMenu {
    protected TopRowMenu(Plugin plugin, String inventoryTitle, Player player) {
        super(plugin, inventoryTitle, player);
        prefillTopRow();
    }

    public TopRowMenu(Plugin plugin, Player player) {
        super(plugin, player);
    }

    /**
     * Initialises the top row with buttons. By default, all slots of the top row are already filled
     * with placeholders before this method is called. This method should be called from the
     * implementation's constructor. It has been made abstract so that implementations do not forget
     * to initialise their top row.
     */
    protected abstract void initTopRow();

    private void prefillTopRow() {
        for (int i = 0; i < ROW_SIZE; i++) {
            addPlaceholder(i);
        }
    }

    /**
     * Adds an element to the top row.
     *
     * @param slotId  the index in the top row of the element
     * @param element the element
     * @throws IllegalArgumentException if the slotId is not in the top row
     */
    public void addToTopRow(int slotId, MenuElement element) {
        Preconditions.checkArgument(isTopBarSlotId(slotId),
                "slotId must be positive and less than 10, given: %s", slotId);
        setElementRaw(slotId, element);
    }

    /**
     * @param slotId the slot id to check
     * @return whether given slot id is located in the top bar
     */
    protected boolean isTopBarSlotId(int slotId) {
        return slotId >= 0 && slotId < 9;
    }
}
