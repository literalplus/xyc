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
import org.bukkit.inventory.ItemStack;

/**
 * Represents a placeholder for use in inventory menus. Placeholders do not react to clicks and
 * always draw copies of the same item stack, which is supposed to be mostly invisible to the
 * player. A good material for that is {@link org.bukkit.Material#STAINED_GLASS_PANE} with damage
 * value 8.
 *
 * @param <M> the kind of menu this element can be used in
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public class Placeholder<M extends InventoryMenu> extends NoopMenuItem<M> {
    private final ItemStack template;

    /**
     * Creates a new placeholder with a template stack.
     * @param template the template stack to use as icon
     */
    public Placeholder(ItemStack template) {
        this.template = template.clone();
    }

    @Override
    public ItemStack draw(ElementHolder menu) {
        return createStack();
    }

    /**
     * @return a new copy of the placeholder stack
     */
    public ItemStack createStack() {
        return template.clone();
    }
}
