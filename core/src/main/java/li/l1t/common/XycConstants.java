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
