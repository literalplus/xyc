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
