/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.xxyy.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;

/**
 * Class that provides static utility methods for dealing with Strings.
 *
 * @author <a href="http://xxyy.github.io/">xxyy98</a>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringHelper {

    /**
     * Method that turns an array of Strings into a single String, delimited by spaces.
     *
     *
     * @param args            source String[]
     * @param startIndex      At what position in the array is the first String to be processed located (starts with 0)
     * @param translateColors Whether to use {@link ChatColor#translateAlternateColorCodes(char, java.lang.String)} on the output.
     *
     * @return the source array in a single String, delimited by spaces.
     */
    public static String varArgsString(final String[] args, final int startIndex, final boolean translateColors) {
        final StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i < args.length; i++) {
            builder.append(((i == startIndex) ? "" : " ")).append(args[i]);
        }
        String text = builder.toString();
        if (translateColors) {
            text = ChatColor.translateAlternateColorCodes('&', text);
        }
        return text;
    }
}
