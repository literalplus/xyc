/*
 * Copyright (c) 2013-2016.
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt
 * or alternatively obtained by sending an email to xxyy98+mtclicense@gmail.com.
 */

package li.l1t.common.inventory.gui.element.button;

import li.l1t.common.inventory.gui.ChildMenu;
import li.l1t.common.inventory.gui.element.CheckedMenuElement;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import li.l1t.common.util.inventory.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A button forwarding players back to a previous menu on click.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-04-17
 */
public class BackToParentButton extends CheckedMenuElement<ElementHolder, ChildMenu> {
    public static final BackToParentButton INSTANCE = new BackToParentButton();

    private BackToParentButton() {
        super(ElementHolder.class, ChildMenu.class);
    }

    @Override
    protected ItemStack checkedDraw(ElementHolder holder) {
        return new ItemStackFactory(Material.WOOD_DOOR)
                .displayName("§b<< Zurück")
                .produce();
    }

    @Override
    protected void checkedHandleMenuClick(InventoryClickEvent evt, ChildMenu menu) {
        menu.getParent().open();
    }
}
