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
