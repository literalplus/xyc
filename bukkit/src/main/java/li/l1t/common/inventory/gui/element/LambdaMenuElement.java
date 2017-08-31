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
