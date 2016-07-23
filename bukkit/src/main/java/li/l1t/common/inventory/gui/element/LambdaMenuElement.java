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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A menu element that calls a consumer when it is clicked and always shows a copy of a constant
 * item stack.
 *
 * @param <M> the kind of menu this element can be used in
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public abstract class LambdaMenuElement<M extends InventoryMenu> extends Placeholder<M> {
    private final BiConsumer<InventoryClickEvent, M> consumer;

    /**
     * Creates a new element with a template stack and a consumer.
     *
     * @param consumer the bi-consumer that consumes all click events
     * @param template the template stack to use as icon
     */
    public LambdaMenuElement(BiConsumer<InventoryClickEvent, M> consumer, ItemStack template) {
        super(template);
        this.consumer = consumer;
    }

    /**
     * Creates a new element with a template stack and a consumer.
     *
     * @param consumer the click event consumer that consumes all click events
     * @param template the template stack to use as icon
     */
    public LambdaMenuElement(Consumer<InventoryClickEvent> consumer, ItemStack template) {
        super(template);
        this.consumer = (evt, menu) -> consumer.accept(evt);
    }

    /**
     * Creates a new element with a template stack and a consumer.
     *
     * @param template the template stack to use as icon
     * @param consumer the menu consumer that consumes all click events
     */
    public LambdaMenuElement(ItemStack template, Consumer<M> consumer) { //yes this is a hack
        super(template);
        this.consumer = (evt, menu) -> consumer.accept(menu);
    }

    @Override
    public void handleMenuClick(InventoryClickEvent evt, M menu) {
        consumer.accept(evt, menu);
    }
}
