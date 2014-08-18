/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without permission from the
 *  original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.util;

/**
 * Validation class inspired by {@link org.apache.commons.lang.Validate}. This is essentially here to prevent having to
 * shade commons-lang3 just for {@code Validate#validateState(...) }.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 14.8.14
 */
public final class XyValidate {
    private XyValidate() {

    }

    /**
     * Ensures that a given expression resolves to TRUE or throws an {@link IllegalStateException} otherwise.
     * Message will be parsed using {@link java.lang.String#format(String, Object...)}.
     *
     * @param expression the expression to validate
     * @param message    the thrown exception's message
     * @param arguments  the arguments for the message
     */
    public static void validateState(boolean expression, String message, Object... arguments) {
        if (!expression) {
            if (message == null) {
                throw new IllegalStateException("Expression resolved to false!");
            } else {
                throw new IllegalStateException(String.format(message, arguments));
            }
        }
    }
}
