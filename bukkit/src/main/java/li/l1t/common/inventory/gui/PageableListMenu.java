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

import java.util.Collection;
import java.util.List;

/**
 * An inventory menu that is able to hold more items than fit into the inventory at once by dividing
 * them into pages. <p>Implementations must state in their class JavaDoc whether they permit null
 * elements.</p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-18
 */
public interface PageableListMenu<V> extends InventoryMenu {
    /**
     * Selects a specific page number (starting at 1) for this menu. Note that this does not update
     * the player's view of the inventory. Note that, instead of just {@link #redraw() redrawing the
     * menu} for updating, it is recommended to {@link #open() reopen the inventory} so that the
     * title is also updated with the new page number.
     *
     * @param pageNum the number of the page to select
     * @throws IllegalArgumentException if pageNum &lt; 1
     * @throws IllegalArgumentException if pageNum &gt; {@link #getPageCount()}
     */
    void selectPageNum(int pageNum);

    /**
     * @return the greatest page number available in this menu
     */
    int getPageCount();

    /**
     * @return the number of the page currently selected in this menu
     */
    int getCurrentPageNum();

    /**
     * @return the immutable list of items currently registered with this menu
     */
    List<V> getItems();

    /**
     * @param newItems the collection of items to replace the existing list of items in this menu
     * @throws IllegalArgumentException if this menu does not permit null elements and newItems
     *                                  contains null elements
     */
    void setItems(Collection<V> newItems);

    /**
     * Removes all items from this menu.
     */
    void clearItems();

    /**
     * @param newItems the collection of items to add to this menu
     * @throws IllegalArgumentException if this menu does not permit null elements and newItems
     *                                  contains null elements
     */
    void addItems(Collection<V> newItems);
}
