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

package li.l1t.common.chat;


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
    /**
     * A list of default text operators which replace some German swear words with nicer expressions.
     */
    public static final List<UnaryOperator<String>> GERMAN_DEFAULTS;

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
