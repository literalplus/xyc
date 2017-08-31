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
        menu.open();
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
