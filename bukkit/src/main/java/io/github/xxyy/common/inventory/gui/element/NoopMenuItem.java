/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.inventory.gui.element;

import org.bukkit.event.inventory.InventoryClickEvent;

import io.github.xxyy.common.inventory.gui.InventoryMenu;

/**
 * Abstract base class for menu items. Offers a no-op click handler.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public abstract class NoopMenuItem implements MenuElement {
    @Override
    public void handleMenuClick(InventoryClickEvent evt, InventoryMenu menu) {
        //no-op
    }
}
