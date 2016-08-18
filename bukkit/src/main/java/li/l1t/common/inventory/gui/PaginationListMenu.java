/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
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
    }

    @Override
    protected void initTopRow() {
        addToTopRow(0, new PaginationButton(PaginationButton.PaginationAction.FIRST, "§a<< Zur ersten Seite"));
        addToTopRow(1, new PaginationButton(PaginationButton.PaginationAction.PREVIOUS, "§a< Zur vorherigen Seite"));
        addToTopRow(7, new PaginationButton(PaginationButton.PaginationAction.NEXT, "§aZur nächsten Seite >"));
        addToTopRow(8, new PaginationButton(PaginationButton.PaginationAction.LAST, "§aZur letzten Seite >>"));
    }
}
