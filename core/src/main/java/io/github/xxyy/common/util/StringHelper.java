/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.xxyy.common.util;

import io.github.xxyy.common.util.math.NumberHelper;

/**
 * Class that provides static utility methods for dealing with Strings.
 *
 * @author <a href="http://xxyy.github.io/">xxyy98</a>
 */
public final class StringHelper {
    public static final String VALID_FORMATTING_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";

    private StringHelper() {
    }

    /**
     * Method that turns an array of Strings into a single String, delimited by spaces.
     *
     * @param args            source String[]
     * @param startIndex      At what position in the array is the first String to be processed located (starts with 0)
     * @param translateColors Whether to use {@link #translateAlternateColorCodes(java.lang.String)} on the output.
     * @return the source array in a single String, delimited by spaces.
     */
    public static String varArgsString(final String[] args, final int startIndex, final boolean translateColors) {
        final StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i < args.length; i++) {
            builder.append(((i == startIndex) ? "" : " ")).append(args[i]);
        }
        String text = builder.toString();
        if (translateColors) {
            text = translateAlternateColorCodes(text);
        }
        return text;
    }

    /**
     * Translates an alternate syntax for <a href="http://minecraftwiki.net/wiki/Formatting%20Codes">Minecraft's color codes</a>
     * using {@code &} instead of {@code §} to to {@code §} notation accepted by the Minecraft client. This only translates
     * actual formatting codes and ignores other instances of {@code &}.
     *
     * @param text the text to translate
     * @return the input, with formatting codes as accepted by the Minecraft client
     */
    public static String translateAlternateColorCodes(String text) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&' && VALID_FORMATTING_CODES.indexOf(text.charAt(i + 1)) != -1) {
                stringBuilder.append('§');
            } else {
                stringBuilder.append(text.charAt(i));
            }
        }

        return stringBuilder.toString();
    }

    /**
     * @return A random String, created with 130 bytes. Considered to be "securely" random.
     */
    public static String randomString() {
        return NumberHelper.randomInteger().toString(32);
    }
}
