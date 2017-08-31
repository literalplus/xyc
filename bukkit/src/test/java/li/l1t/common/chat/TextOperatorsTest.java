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
