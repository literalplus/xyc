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

import java.util.function.BiConsumer;

/**
 * A menu element that calls a consumer when it is clicked and always shows a copy of a constant
 * item stack.
 *
 * @param <M> the kind of menu this element can be used in
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public class LambdaMenuElement<M extends InventoryMenu> extends CheckedMenuElement<ElementHolder, M> {
    private final BiConsumer<InventoryClickEvent, M> consumer;
    private final ItemStack template;

    /**
     * Creates a new element with a template stack and a consumer.
     *
     * @param menuType the type of menu the consumer accepts
     * @param consumer the bi-consumer that consumes all click events
     * @param template the template stack to use as icon
     */
    public LambdaMenuElement(Class<? extends M> menuType, BiConsumer<InventoryClickEvent, M> consumer, ItemStack template) {
        super(ElementHolder.class, menuType);
        this.template = template;
        this.consumer = consumer;
    }

    @Override
    public void checkedHandleMenuClick(InventoryClickEvent evt, M menu) {
        consumer.accept(evt, menu);
    }

    @Override
    public ItemStack checkedDraw(ElementHolder menu) {
        return createStack();
    }

    private ItemStack createStack() {
        return template.clone();
    }
}
