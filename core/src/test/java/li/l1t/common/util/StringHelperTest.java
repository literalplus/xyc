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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StringHelperTest {
    @Test
    public void testParseTimePeriod() {
        assertThat(StringHelper.parseTimePeriod("2h42m3s"), is(9_723_000L)); //JavaDoc example
        assertThat(StringHelper.parseTimePeriod("897s"), is(897_000L)); //Only one unit, more than 60s
        assertThat(StringHelper.parseTimePeriod("5y"), is(157_680_000_000L));
        assertThat(StringHelper.parseTimePeriod("5y3M15d18h34m897s"), is(166_819_737_000L)); //Everything
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTimePeriodException1() {
        StringHelper.parseTimePeriod("2y42m3t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTimePeriodException2() {
        StringHelper.parseTimePeriod("y42m3s");
    }
}
