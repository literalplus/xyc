/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.chat;


import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * Provides some generic {@link UnaryOperator}s which operate on strings for use with {@link TextReplacementService}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>, Janmm14
 * @since 04/06/15
 */
public class TextOperators {
    static {
        GERMAN_DEFAULTS = ImmutableList.<UnaryOperator<String>>builder()
                .add(replaceAll("n[o0u\\(\\)]+b", "nette Person"))
                .add(replaceAll("s[p]+[a4]+[s]+t", "sehr nette Person"))
                .add(replaceAll("[h]+[u]+[r]*[e3]*[n]*[s]+[o0u\\(\\)]+[h]*[n]*", "Sohn einer lieben Person"))
                .add(replaceAll("[w]+[i1]+[chksx]+(?:[e3]+[r]+|[a4]+)", "belebte Person"))
                .add(replaceAll("[a4](rsch)?[l1][o0\\(\\)]+(ch)?|[a4]rsch", "elegante Person"))
                .add(replaceAll("fuck", "fluff"))
                .add(replaceAll("[?!]{3,}", "."))
                .build(); //These are some German swear words
    }

    /**
     * A list of default text operators which replace some German swear words with nicer expressions.
     */
    public static final List<UnaryOperator<String>> GERMAN_DEFAULTS;

    /**
     * Returns an operator that replaces all occurrences of a regular expression pattern in a string.
     *
     * @param pattern     the pattern to find
     * @param replacement the text to replace with
     * @return an unary operator performing outlined operation on a string
     */
    public static UnaryOperator<String> replaceAll(String pattern, String replacement) {
        Pattern pat = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        return inp -> pat.matcher(inp).replaceAll(replacement);
    }

    /**
     * Returns an operator that replaces a string with another one in its entirety if it contains a match of provided
     * regular expression pattern.
     *
     * @param pattern    the pattern to find
     * @param newMessage the text to replace with
     * @return an unary operator performing outlined operation on a string
     */
    public static UnaryOperator<String> replaceEntirely(String pattern, String newMessage) {
        Pattern pat = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        return inp -> pat.matcher(inp).find() ? newMessage : inp;
    }

    /**
     * Returns an operator that converts a string to lower case.
     *
     * @return an unary operator performing outlined operation on a string
     */
    public static UnaryOperator<String> toLowerCase() {
        return String::toLowerCase;
    }
}
