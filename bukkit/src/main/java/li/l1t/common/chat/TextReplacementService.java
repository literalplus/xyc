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

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Provides a service to apply pre-defined modifications to text matching specific predicates. This is mainly intended
 * to be used to replace swear words with nicer versions, but can also be used for Propaganda purposes and for fun.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 03/06/15
 */
public class TextReplacementService {
    private List<UnaryOperator<String>> operators;

    /**
     * Constructs a new replacement service without any operators, i.e. leaving messages as-is.
     */
    public TextReplacementService() {
        this(new ArrayList<>());
    }

    /**
     * Constructs a new replacement service with pre-filled operator list.
     * @param operators the operators to start with
     */
    public TextReplacementService(List<UnaryOperator<String>> operators) {
        this.operators = operators;
    }

    /**
     * @return a mutable list of the operators currently used by this service
     */
    public List<UnaryOperator<String>> getOperators() {
        return operators;
    }

    /**
     * Applies all operators managed by this service to a message.
     * @param message the message to process
     * @return the filtered version of the input
     */
    public String apply(String message) {
        for(UnaryOperator<String> operator : operators) {
            message = operator.apply(message);
        }

        return message;
    }
}
