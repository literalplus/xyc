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
