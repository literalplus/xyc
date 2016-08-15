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
    /**
     * Creates a new abstract inventory menu.
     *
     * @param plugin         the plugin associated with this menu, may not be null
     * @param inventoryTitle the title of the inventory, may not be null
     * @param player         the player associated with this menu, may not be null
     */
    protected TopRowMenu(Plugin plugin, String inventoryTitle, Player player) {
        super(plugin, inventoryTitle, player);
        prefillTopRow();
        initTopRow();
    }

    /**
     * Initialises the top row with buttons. By default, all slots of the top row are already filled
     * with placeholders before this method is called. This method is called once from the
     * constructor.
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
        Preconditions.checkArgument(slotId >= 0 && slotId < 9,
                "slotId must be positive and less than 10, given: %s", slotId);
        setElementRaw(slotId, element);
    }
}
