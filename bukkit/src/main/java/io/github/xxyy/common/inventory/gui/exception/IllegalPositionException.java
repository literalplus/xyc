/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.inventory.gui.exception;

/**
 * Thrown if a render is attempted in an
 * {@link io.github.xxyy.common.inventory.gui.InventoryMenu} at a position that custom rendering
 * is not allowed at. Such restrictions must be documented in the respective menu's class JavaDoc.
 * Note that this exception never has a cause.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-24
 */
public class IllegalPositionException extends RuntimeException {
    public IllegalPositionException(String message) {
        super(message);
    }
}
