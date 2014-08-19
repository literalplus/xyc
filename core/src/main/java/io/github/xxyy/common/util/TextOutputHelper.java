/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides static utility methods to help with outputting text
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 18.8.14
 */
public final class TextOutputHelper {
    private TextOutputHelper() {

    }

    /**
     * Prints a message to {@link System#out} and the provided logger {@code lgr} if it is not {@code null}.
     *
     * @param message Message to print
     * @param lgr     The logger to print it to, can be {@code null}.
     * @param lvl     {@link java.util.logging.Level} to use
     */
    public static void printAndOrLog(String message, Logger lgr, Level lvl) {
        System.out.println(message);
        if (lgr != null) {
            lgr.log(lvl, message);
        }
    }
}
