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
