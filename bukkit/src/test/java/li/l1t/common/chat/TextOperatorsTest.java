/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.chat;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.UnaryOperator;

import static org.hamcrest.CoreMatchers.is;

/**
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 10/06/15
 */
public class TextOperatorsTest {
    @Test
    public void testReplaceAll() throws Exception {
        UnaryOperator<String> replacer = TextOperators.replaceAll("Business C[at]{2}", "MEOW");
        Assert.assertThat(replacer.apply("Business Cat flies around the world, he's a first class guy (Business Cat)"),
                is("MEOW flies around the world, he's a first class guy (MEOW)"));
    }

    @Test
    public void testReplaceEntirely() throws Exception {
        UnaryOperator<String> replacer = TextOperators.replaceEntirely("la+g", "This server has some awesome purrformance!");
        Assert.assertThat(replacer.apply("You're also his secretary"), is("You're also his secretary"));
        Assert.assertThat(replacer.apply("You've got a wife and laaaag"), is("This server has some awesome purrformance!"));
    }

    //GERMAN_DEFAULTS are checked in TextReplacementServiceTest
}
