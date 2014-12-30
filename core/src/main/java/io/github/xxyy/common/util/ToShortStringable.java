/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.util;

/**
 * Interface for objects that provide a method to
 * turn it into a shorter String for display.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public interface ToShortStringable { //TÖDO: Better name

    /**
     * @return A short String representation of this object.
     * @see Object#toString()
     */
    public String toShortString();
}
