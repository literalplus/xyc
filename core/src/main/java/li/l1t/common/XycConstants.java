/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common;

import li.l1t.common.util.UUIDHelper;
import li.l1t.common.version.PluginVersion;

import java.util.UUID;

/**
 * Class that provides some constants that are mostly outdated.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class XycConstants {
    public static final PluginVersion VERSION = PluginVersion.ofClass(XycConstants.class);

    /**
     * short name of XYC.
     */
    public static final String pluginID = "XYC";//not actually a plugin
    /**
     * An Unicode check mark, especially useful for To<a></a>Do-lists.
     */ //Prevent IDEA from seeing this as TO_DO comment
    public static final String CHECK_MARK = "✔";
    /**
     * An Unicode ballot X, especially useful for To<a></a>Do-lists.
     */
    public static final String BALLOT_X = "✘";
    /**
     * The nil UUID - a special UUID which can be used to represent special values, such as the Minecraft server console.
     * {@code 00000000-0000-0000-0000-000000000000}
     * @deprecated Use {@link UUIDHelper#NIL_UUID}
     */
    public static final UUID NIL_UUID = UUIDHelper.NIL_UUID;
    /**
     * XYC chat prefix.
     */
    public static String chatPrefix = "§7[§8XYC§7] ";
}
