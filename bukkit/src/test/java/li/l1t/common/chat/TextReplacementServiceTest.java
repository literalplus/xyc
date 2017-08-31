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

import static org.hamcrest.CoreMatchers.is;

/**
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 10/06/15
 */
public class TextReplacementServiceTest {

    private final TextReplacementService service = new TextReplacementService();

    @Test
    public void testApply() throws Exception {
        Assert.assertThat(service.apply("But he's married to his job"), is("But he's married to his job"));

        service.getOperators().addAll(TextOperators.GERMAN_DEFAULTS);
        assertAllConvertedTo("nette Person", "noob", "NOOB", "N00B", "Nub", "nuuuuuuououuub", "n()()b", "NoOoOoOB");
        assertAllConvertedTo("sehr nette Person", "spast", "sp4st", "spassst");
        assertAllConvertedTo("belebte Person", "wixer", "wichser", "wickser");
        assertAllConvertedTo("elegante Person", "Arschloch", "Arsch", "aloch", "alo", "arschlo", "4l()ch");
        assertAllConvertedTo("fluff", "fuck");
        assertAllConvertedTo(".", "!!!!!!!!!!!!", "?!?!?!?!?", "??????");
    }

    private void assertAllConvertedTo(String expected, String... strings) {
        for (String string : strings) {
            Assert.assertThat(service.apply(string), is(expected));
        }
    }
}
