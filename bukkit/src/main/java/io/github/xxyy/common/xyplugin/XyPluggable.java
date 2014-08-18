/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.xyplugin;

public interface XyPluggable {
    public static String pluginPrefix = "§7[§8XYC§7]";

    public void disable();

    public void enable();
}
