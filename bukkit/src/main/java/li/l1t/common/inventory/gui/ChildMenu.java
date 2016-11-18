/*
 * Copyright (c) 2013-2016.
 * This work is protected by international copyright laws and licensed
 * under the license terms which can be found at src/main/resources/LICENSE.txt
 * or alternatively obtained by sending an email to xxyy98+mtclicense@gmail.com.
 */

package li.l1t.common.inventory.gui;

import li.l1t.common.inventory.gui.InventoryMenu;

/**
 * Represents a menu that was opened by another menu.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-18-11
 */
public interface ChildMenu extends InventoryMenu {
    /**
     * @return the parent menu of this menu
     */
    InventoryMenu getParent();
}
