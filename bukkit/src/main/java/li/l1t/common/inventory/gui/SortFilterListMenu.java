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
        Collections.sort(newItems, comparator);
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
        super.setItems(newItems);
        filterAndSortItems();
    }

    @Override
    public void addItems(Collection<V> newItems) {
        super.addItems(newItems);
        filterAndSortItems();
    }
}
