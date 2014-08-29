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

    public static final String VALID_FORMATTING_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    private static final Map<Character, TimePeriod> CHARS_TO_TIME_PERIODS;

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
