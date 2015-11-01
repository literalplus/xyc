/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.xxyy.common.util;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import io.github.xxyy.common.util.math.NumberHelper;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Class that provides static utility methods for dealing with Strings.
 *
 * @author <a href="http://xxyy.github.io/">xxyy98</a>
 */
public final class StringHelper {
    static {
        ImmutableMap.Builder<Character, TimePeriod> mapBuilder = ImmutableMap.builder();
        for (TimePeriod period : TimePeriod.values()) {
            mapBuilder.put(period.getId(), period);
        }
        CHARS_TO_TIME_PERIODS = mapBuilder.build();
    }

    public static final String VALID_FORMATTING_CODES = "0123456789abcdefklmnor";
    private static final Map<Character, TimePeriod> CHARS_TO_TIME_PERIODS;

    private StringHelper() {
    }

    /**
     * Concatenates an array of strings, using a space as separator.
     *
     * @param args            the array to concatenate
     * @param startIndex      first array index to process
     * @param translateColors whether to pass the output through {@link #translateAlternateColorCodes(java.lang.String)}
     * @return the concatenated string
     */
    public static String varArgsString(final String[] args, final int startIndex, final boolean translateColors) {
        return varArgsString(args, startIndex, 0, translateColors);
    }

    /**
     * Concatenates an array of strings, using a space as separator.
     *
     * @param args            the array to concatenate
     * @param startIndex      first array index to process
     * @param end             how many items to ignore at the end; 0 means to process all items
     * @param translateColors whether to pass the output through {@link #translateAlternateColorCodes(java.lang.String)}
     * @return the concatenated string
     */
    public static String varArgsString(String[] args, int startIndex, int end, boolean translateColors) {
        final StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i < (args.length - end); i++) {
            builder.append(((i == startIndex) ? "" : " ")).append(args[i]);
        }
        String text = builder.toString();
        if (translateColors){
            text = translateAlternateColorCodes(text);
        }
        return text;
    }

    /**
     * Translates an alternate syntax for <a href="http://minecraftwiki.net/wiki/Formatting%20Codes">Minecraft's color codes</a>
     * using {@code &} instead of {@code §} to the {@code §} notation accepted by the Minecraft client. This only translates
     * actual formatting codes (specified by {@code validCodes}) and ignores other instances of {@code &}.
     *
     * @param text       the text to translate
     * @param validCodes a String containing each valid color code in lower case
     * @return the input, with formatting codes as accepted by the Minecraft client
     * @see #translateAlternateColorCodes(String)
     */
    public static String translateAlternateColorCodes(String text, String validCodes) {
        validCodes = validCodes.toLowerCase();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&' && validCodes.indexOf(Character.toLowerCase(text.charAt(i + 1))) != -1){
                stringBuilder.append('§');
            } else {
                stringBuilder.append(text.charAt(i));
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Translates an alternate syntax for <a href="http://minecraftwiki.net/wiki/Formatting%20Codes">Minecraft's color codes</a>
     * using {@code &} instead of {@code §} to the {@code §} notation accepted by the Minecraft client. This only translates
     * actual formatting codes and ignores other instances of {@code &}.
     *
     * @param text the text to translate
     * @return the input, with formatting codes as accepted by the Minecraft client
     * @see #translateAlternateColorCodes(String, String)
     */
    public static String translateAlternateColorCodes(String text) {
        return translateAlternateColorCodes(text, VALID_FORMATTING_CODES);
    }

    /**
     * Generates a semi-random alphanumeric character.
     *
     * @param allowUpperCase whether to allow upper-case characters as result
     * @return a semi-random alphanumeric character
     * @see #alphanumericString(int, boolean)
     */
    public static char alphanumericChar(boolean allowUpperCase) {
        char result = Character.forDigit(NumberHelper.RANDOM.nextInt(36), 36); //36....[0-9a-f]
        if (allowUpperCase && NumberHelper.RANDOM.nextBoolean()){
            return Character.toUpperCase(result);
        } else {
            return result;
        }
    }

    /**
     * Generates a semi-random string consisting of alphanumeric characters.
     *
     * @param length         length of the string
     * @param allowUpperCase whether to allow upper-case characters in the result
     * @return a semi-random alphanumeric string with specified length
     * @see #alphanumericChar(boolean)
     */
    public static String alphanumericString(int length, boolean allowUpperCase) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphanumericChar(allowUpperCase));
        }
        return sb.toString();
    }

    /**
     * Creates a random string using a "secure" random number generator. No guarantees
     * are made about the length of the returned string.
     *
     * @return A random String, created with 130 bytes. Considered to be "securely" random.
     */
    public static String randomString() {
        return NumberHelper.randomInteger().toString(36); //36....[0-9a-f]
    }

    /**
     * Converts a string representing a time period (without spaces) to a long representing milliseconds.
     * Time units are defined by the identifiers of {@link io.github.xxyy.common.util.StringHelper.TimePeriod}, for example
     * "2h42m3s" would represent 2 hours, 42 minutes and 3 seconds (9 723 000ms)
     *
     * @param input the input string to parse
     * @return the amount of milliseconds represented by the input
     * @throws java.lang.IllegalArgumentException If the input couldn't be parsed
     */
    public static long parseTimePeriod(@NotNull String input) {
        if (input.isEmpty()){
            throw new IllegalArgumentException("Empty input!");
        }

        StringBuilder numberBuilder = new StringBuilder();
        long result = 0L;
        for (int i = 0; i < input.length(); i++) {
            char chr = input.charAt(i);
            TimePeriod period = CHARS_TO_TIME_PERIODS.get(chr);
            if (period != null){
                if (numberBuilder.length() == 0){
                    throw new IllegalArgumentException("Time period " + period.name() + " missing amount at index " + i);
                }
                result += TimeUnit.MILLISECONDS.convert(Long.parseLong(numberBuilder.toString()) * period.getMultiplier(), period.getUnit());
                numberBuilder = new StringBuilder();
            } else if (Character.isDigit(chr)){
                numberBuilder.append(chr);
            } else {
                throw new IllegalArgumentException("Unexpected symbol '" + chr + "' at index " + i);
            }
        }

        return result;
    }

    public enum TimePeriod {
        /**
         * A second - 's'
         */
        SECONDS('s', TimeUnit.SECONDS),
        /**
         * A minute - 'm'
         */
        MINUTES('m', TimeUnit.MINUTES),
        /**
         * An hour - 'h'
         */
        HOURS('h', TimeUnit.HOURS),
        /**
         * A day - 'd'
         */
        DAYS('d', TimeUnit.DAYS),
        /**
         * A month - 'M' (30 days)
         */
        MONTHS('M', 30, TimeUnit.DAYS),
        /**
         * A year - 'y' (365 days)
         */
        YEARS('y', 365, TimeUnit.DAYS);

        private final char id;
        private final TimeUnit unit;
        private long multiplier;

        TimePeriod(char id, TimeUnit unit) {
            this(id, 1, unit);
        }

        TimePeriod(char id, long multiplier, TimeUnit unit) {
            this.id = id;
            this.unit = unit;
            this.multiplier = multiplier;
        }

        public TimeUnit getUnit() {
            return unit;
        }

        public char getId() {
            return id;
        }

        public long getMultiplier() {
            return multiplier;
        }
    }
}
