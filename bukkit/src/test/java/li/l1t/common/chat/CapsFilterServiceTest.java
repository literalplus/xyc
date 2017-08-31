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

import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 10/06/15
 */
public class CapsFilterServiceTest {
    private final CapsFilterService capsFilterService = new CapsFilterService();

    @Test
    public void testCheck() throws Exception { //default caps factor is 0.5F
        assertCaps("BUSINESS CAT JUST CALLED TO MAKE SURE THAT WE'RE SINGING FROM THE SAME HYMN SHEET", true);
        assertCaps("That we're optimizing, synergizing our portfolio", false);
        assertCaps("DIVERSIFYING our assets", true); //exactly half of that string is in upper case
        assertCaps("BY", false); //messages shorter than 5 chars should be ignored
        assertCaps("INVESTING in other kind of business PETS", false);
    }

    @Test
    public void testSetCapsFactor() throws Exception {
        capsFilterService.setCapsFactor(0.0F);
        assertCaps("And he wants us to know, before he's gotta go", true);
        capsFilterService.setCapsFactor(1.0F);
        assertCaps("TO THE CONFERENCE BEING HELD IN TOKYO", false);
    }

    private void assertCaps(String message, boolean caps) {
        Assert.assertThat(capsFilterService.check(message),
                describedAs("Capitalisation detection result %1 for %0",
                        is(caps),
                        message, caps));
    }
}
