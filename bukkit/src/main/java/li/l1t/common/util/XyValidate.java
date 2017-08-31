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
