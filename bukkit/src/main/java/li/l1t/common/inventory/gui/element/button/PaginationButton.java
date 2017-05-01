/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.inventory.gui.element.button;

import li.l1t.common.inventory.gui.PageableListMenu;
import li.l1t.common.inventory.gui.element.CheckedMenuElement;
import li.l1t.common.inventory.gui.holder.ElementHolder;
import li.l1t.common.util.inventory.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * An element that acts as a pagination button.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-18
 */
public class PaginationButton extends CheckedMenuElement<ElementHolder, PageableListMenu> {
    private final PaginationAction action;
    private final ItemStack itemStack;

    public PaginationButton(PaginationAction action, String displayName) {
        super(ElementHolder.class, PageableListMenu.class);
        this.action = action;
        this.itemStack = createDisplayStackFor(action, displayName);
    }

    private ItemStack createDisplayStackFor(PaginationAction action, String displayName) {
        return new ItemStackFactory(action.getDisplayMaterial())
                .displayName(displayName)
                .produce();
    }

    @Override
    protected ItemStack checkedDraw(ElementHolder holder) {
        return itemStack;
    }

    @Override
    protected void checkedHandleMenuClick(InventoryClickEvent evt, PageableListMenu menu) {
        menu.selectPageNum(action.getTargetPageNum(menu));
    }

    public enum PaginationAction {
        FIRST(Material.POWERED_MINECART) {
            @Override
            int getTargetPageNum(PageableListMenu<?> menu) {
                return 1;
            }
        },
        PREVIOUS(Material.MINECART) {
            @Override
            int getTargetPageNum(PageableListMenu<?> menu) {
                if (menu.getCurrentPageNum() == 1) {
                    return menu.getPageCount();
                } else {
                    return menu.getCurrentPageNum() - 1;
                }
            }
        },
        NEXT(Material.MINECART) {
            @Override
            int getTargetPageNum(PageableListMenu<?> menu) {
                if (menu.getCurrentPageNum() == menu.getPageCount()) {
                    return 1;
                } else {
                    return menu.getCurrentPageNum() + 1;
                }
            }
        },
        LAST(Material.POWERED_MINECART) {
            @Override
            int getTargetPageNum(PageableListMenu<?> menu) {
                return menu.getPageCount();
            }
        };

        private final Material displayMaterial;

        PaginationAction(Material displayMaterial) {
            this.displayMaterial = displayMaterial;
        }

        abstract int getTargetPageNum(PageableListMenu<?> menu);

        Material getDisplayMaterial() {
            return displayMaterial;
        }
    }
}
