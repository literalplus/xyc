/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.util;

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
