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

import li.l1t.common.util.PredicateHelper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * An extension of {@link PagingListMenu} that behaves just like its superclass but also offers
 * methods to sort and filter the items in the menu. The superclass only knows about those items
 * that match the filter.
 *
 * <p> Note that filters and sorts get lost when a new item is added to the menu, meaning that
 * adding an item puts the menu in an inconsistent state. In such a state, not all items are
 * displayed, but not all items match the filter, and some items are sorted, but not all.</p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-18
 */
public abstract class SortFilterListMenu<V> extends PagingListMenu<V> {
    private List<V> allItems = new ArrayList<>();
    private Predicate<? super V> filter = anything -> true;
    private Comparator<? super V> comparator = null;

    public SortFilterListMenu(Plugin plugin, Player player) {
        super(plugin, player);
    }

    /**
     * Sets this menu's comparator and sorts the items according to it.
     *
     * @param comparator the comparator to sort with
     */
    public void setComparator(Comparator<? super V> comparator) {
        this.comparator = comparator;
        filterAndSortItems();
    }

    /**
     * Sets the filter of this menu and applies it. Items not matching the filter are still kept,
     * but not displayed.
     *
     * @param filter sets the filter that all items must match
     */
    public void setFilter(Predicate<? super V> filter) {
        this.filter = filter;
        filterAndSortItems();
    }

    private void filterAndSortItems() {
        List<V> newItems = copyAllItems();
        newItems.removeIf(PredicateHelper.not(filter));
        if (comparator != null) {
            newItems.sort(comparator);
        }
        super.setItems(newItems);
    }

    private ArrayList<V> copyAllItems() {
        return new ArrayList<>(allItems);
    }

    /**
     * Removes the current filter, making all items eligible to be displayed again.
     */
    public void removeFilter() {
        filter = anything -> true;
    }

    /**
     * @return a list of all items, including those currently hidden by a filter
     */
    public List<V> getAllItems() {
        return Collections.unmodifiableList(allItems);
    }

    @Override
    public void clearItems() {
        super.clearItems();
        allItems.clear();
    }

    @Override
    public void setItems(Collection<V> newItems) {
        clearItems();
        addItems(newItems);
    }

    @Override
    public void addItems(Collection<V> newItems) {
        this.allItems.addAll(newItems);
        filterAndSortItems();
    }
}
