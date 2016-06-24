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
