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

package li.l1t.common.util;

import org.bukkit.ChatColor;

/**
 * This utility class provides some static utility methods for dealing with Minecraft chat. This previously contained
 * some more methods for filtering which have since been refactored and move to the
 * {@link li.l1t.common.chat} package.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>, Janmm14
 */
public class ChatHelper {
    /**
     * These characters will cause '&amp;' to become {@link ChatColor#COLOR_CHAR} if followed by one of them.
     *
     * @deprecated public static field with mutable data
     */
    @Deprecated
    public static String allowedChatColors = "012356789AaBbDdEeFfRr";

    private ChatHelper() {

    }

    /**
     * Replaces occurrences of '&amp;' followed by a hexadecimal character with the special formatting character
     * accepted by Minecraft. Note that this is referred to as "legacy" by a lot of developers though, so please
     * consider using one of the many JSON chat APIs if possible. This is using the <code>public static</code> field
     * {@link #allowedChatColors}, which is why it is deprecated.
     *
     * @param message Message to colorize
     * @return Colorized message
     * @deprecated use {@link #convertChatColors(String, String)}.
     */
    @Deprecated
    public static String convertDefaultChatColors(String message) {
        //noinspection deprecation
        return convertChatColors(message, allowedChatColors);
    }

    /**
     * Replaces occurrences of '&amp;' followed by a hexadecimal character with the special formatting character
     * accepted by Minecraft. Note that this is referred to as "legacy" by a lot of developers though, so please
     * consider using one of the many JSON chat APIs if possible.
     *
     * @param message the message to colorize
     * @return the colorized message
     */
    public static String convertStandardColors(String message) {
        return convertChatColors(message, "012356789AaBbCcDdEeFfKkRr");
    }

    /**
     * Replaces occurrences of '&amp;' followed by a hexadecimal character specified in the second argument with the
     * special formatting character accepted by Minecraft. Note that this is referred to as "legacy" by a lot of
     * developers though, so please consider using one of the many JSON chat APIs if possible. Any character in the
     * "allowed colors" string will be allowed as formatting code. Note that matching is case-sensitive.
     *
     * @param message       the message to colorize
     * @param allowedColors a string where each character specifies an allowed color
     * @return the colorized message
     */
    public static String convertChatColors(String message, String allowedColors) {
        char[] b = message.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && allowedColors.indexOf(b[i + 1]) > -1) {
                b[i] = ChatColor.COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }
}
