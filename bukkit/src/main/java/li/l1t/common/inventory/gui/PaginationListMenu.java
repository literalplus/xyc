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

package li.l1t.common.inventory.gui;

import li.l1t.common.inventory.gui.element.button.PaginationButton;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Abstract base classes for inventory menus with pagination buttons for changing pages.
 *
 * <p>Displayed buttons are first page and previous page as well as next page and last page. These
 * are placed in the leftmost and rightmost, respectively, two slots of the top bar.</p>
 *
 * <p>Otherwise, behaves just like {@link SortFilterListMenu}.</p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-18
 */
public abstract class PaginationListMenu<V> extends SortFilterListMenu<V> {
    public PaginationListMenu(Plugin plugin, Player player) {
        super(plugin, player);
        initTopRow();
    }

    @Override
    protected void initTopRow() {
        addToTopRow(0, new PaginationButton(PaginationButton.PaginationAction.FIRST, "§a<< Zur ersten Seite"));
        addToTopRow(1, new PaginationButton(PaginationButton.PaginationAction.PREVIOUS, "§a< Zur vorherigen Seite"));
        addToTopRow(7, new PaginationButton(PaginationButton.PaginationAction.NEXT, "§aZur nächsten Seite >"));
        addToTopRow(8, new PaginationButton(PaginationButton.PaginationAction.LAST, "§aZur letzten Seite >>"));
    }
}
