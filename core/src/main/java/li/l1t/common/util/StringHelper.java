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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package li.l1t.common.util;

import com.google.common.collect.ImmutableMap;
import li.l1t.common.util.math.NumberHelper;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Class that provides static utility methods for dealing with Strings.
 *
 * @author <a href="http://xxyy.github.io/">xxyy98</a>
 */
public final class StringHelper {
    public static final String VALID_FORMATTING_CODES = "0123456789abcdefklmnor";
    private static final Map<Character, TimePeriod> CHARS_TO_TIME_PERIODS;

    static {
        ImmutableMap.Builder<Character, TimePeriod> mapBuilder = ImmutableMap.builder();
        for (TimePeriod period : TimePeriod.values()) {
            mapBuilder.put(period.getId(), period);
        }
        CHARS_TO_TIME_PERIODS = mapBuilder.build();
    }

    private StringHelper() {
    }

    /**
     * Concatenates an array of strings, using a space as separator.
     *
     * @param args            the array to concatenate
     * @param startIndex      first array index to process
     * @param translateColors whether to pass the output through {@link #translateAlternateColorCodes(java.lang.String)}
     * @return the concatenated string
     * @deprecated Boolean parameter decides between two implementations. Use {@link li.l1t.common.chat.Arguments#joinRange(String[],
     * int, int)} or {@link li.l1t.common.chat.Arguments#joinRangeColored(String[], int, int)}
     */
    @Deprecated
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
     * @deprecated Boolean parameter decides between two implementations. Use {@link li.l1t.common.chat.Arguments#joinRange(String[],
     * int, int)} or {@link li.l1t.common.chat.Arguments#joinRangeColored(String[], int, int)}
     */
    public static String varArgsString(String[] args, int startIndex, int end, boolean translateColors) {
        final StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i < (args.length - end); i++) {
            builder.append(((i == startIndex) ? "" : " ")).append(args[i]);
        }
        String text = builder.toString();
        if (translateColors) {
            text = translateAlternateColorCodes(text);
        }
        return text;
    }

    /**
     * Translates an alternate syntax for <a href="http://minecraftwiki.net/wiki/Formatting%20Codes">Minecraft's color
     * codes</a> using {@code &} instead of {@code §} to the {@code §} notation accepted by the Minecraft client. This
     * only translates actual formatting codes (specified by {@code validCodes}) and ignores other instances of {@code
     * &}.
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
            if (text.charAt(i) == '&' && validCodes.indexOf(Character.toLowerCase(text.charAt(i + 1))) != -1) {
                stringBuilder.append('§');
            } else {
                stringBuilder.append(text.charAt(i));
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Translates an alternate syntax for <a href="http://minecraftwiki.net/wiki/Formatting%20Codes">Minecraft's color
     * codes</a> using {@code &} instead of {@code §} to the {@code §} notation accepted by the Minecraft client. This
     * only translates actual formatting codes and ignores other instances of {@code &}.
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
        if (allowUpperCase && NumberHelper.RANDOM.nextBoolean()) {
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
     * Time units are defined by the identifiers of {@link StringHelper.TimePeriod}, for example
     * "2h42m3s" would represent 2 hours, 42 minutes and 3 seconds (9 723 000ms)
     *
     * @param input the input string to parse
     * @return the amount of milliseconds represented by the input
     * @throws java.lang.IllegalArgumentException If the input couldn't be parsed
     */
    public static long parseTimePeriod(@Nonnull String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Empty input!");
        }

        StringBuilder numberBuilder = new StringBuilder();
        long result = 0L;
        for (int i = 0; i < input.length(); i++) {
            char chr = input.charAt(i);
            TimePeriod period = CHARS_TO_TIME_PERIODS.get(chr);
            if (period != null) {
                if (numberBuilder.length() == 0) {
                    throw new IllegalArgumentException("Time period " + period.name() + " missing amount at index " + i);
                }
                result += TimeUnit.MILLISECONDS.convert(Long.parseLong(numberBuilder.toString()) * period.getMultiplier(), period.getUnit());
                numberBuilder = new StringBuilder();
            } else if (Character.isDigit(chr)) {
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
