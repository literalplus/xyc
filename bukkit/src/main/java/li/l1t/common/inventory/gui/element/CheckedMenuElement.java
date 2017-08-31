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

import com.google.common.base.Preconditions;
import li.l1t.common.inventory.gui.InventoryMenu;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Abstract base class for menu elements that need specific menu or holder types. Calls to the menu
 * element methods are forwarded to {@code #checkedXXX()} methods with appropriate generic types. If
 * the parameters are not subclasses of the required types, exceptions will be thrown.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-16
 */
public abstract class CheckedMenuElement<H extends ElementHolder, M extends InventoryMenu> implements MenuElement {
    private final Class<? extends H> holderType;
    private final Class<? extends M> menuType;

    public CheckedMenuElement(Class<? extends H> holderType, Class<? extends M> menuType) {
        this.holderType = holderType;
        this.menuType = menuType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final ItemStack draw(ElementHolder holder) {
        verifyAssignableFrom(holder, holderType);
        return checkedDraw((H) holder);
    }

    /**
     * Like {@link #draw(ElementHolder)}, but with verified parameter type.
     *
     * @param holder the holder to draw into
     * @return the drawn stack
     */
    protected abstract ItemStack checkedDraw(H holder);

    private void verifyAssignableFrom(ElementHolder menu, Class<?> expected) {
        Preconditions.checkArgument(expected.isAssignableFrom(menu.getClass()),
                "menu needs to extend %s (is %s)", expected, menu.getClass()
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu) {
        verifyAssignableFrom(menu, menuType);
        checkedHandleMenuClick(evt, (M) menu);
    }

    /**
     * Like {@link #handleMenuClick(InventoryClickEvent, InventoryMenu)}, but with verified
     * parameter type.
     *
     * @param evt  the Bukkit event that caused the click
     * @param menu the menu the click was registered in
     */
    protected abstract void checkedHandleMenuClick(InventoryClickEvent evt, M menu);

    public Class<? extends ElementHolder> getHolderType() {
        return holderType;
    }

    public Class<? extends InventoryMenu> getMenuType() {
        return menuType;
    }
}
