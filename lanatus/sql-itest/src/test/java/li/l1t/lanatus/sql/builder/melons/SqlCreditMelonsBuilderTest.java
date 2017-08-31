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

package li.l1t.lanatus.sql.builder.melons;

import li.l1t.lanatus.api.builder.CreditMelonsBuilder;
import li.l1t.lanatus.api.exception.AccountConflictException;
import li.l1t.lanatus.api.purchase.Purchase;
import li.l1t.lanatus.sql.AbstractLanatusSqlTest;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests the integration of the sql credit melons builder with the database, verifying only using
 * the repositories' methods.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-21
 */
public class SqlCreditMelonsBuilderTest extends AbstractLanatusSqlTest {

    private static final String SOME_COMMENT = "muh comment";

    @Test
    public void testCredit__basic() {
        //given
        UUID playerId = UUID.randomUUID();
        CreditMelonsBuilder builder = client()
                .creditMelons(playerId)
                .withMelonsCount(500);
        //when
        builder.build();
        //then
        assertThat(remoteMelonsCount(playerId), is(500));
    }

    private int remoteMelonsCount(UUID playerId) {
        return findAccount(playerId).getMelonsCount();
    }

    @Test
    public void testCredit__existing() throws AccountConflictException {
        //given
        UUID playerId = givenAPlayerWithMelons(500);
        CreditMelonsBuilder builder = client()
                .creditMelons(playerId)
                .withMelonsCount(200);
        //when
        builder.build();
        //then
        assertThat(remoteMelonsCount(playerId), is(500 + 200));
    }

    @Test
    public void testCredit__comment() {
        //given
        UUID playerId = UUID.randomUUID();
        CreditMelonsBuilder builder = client()
                .creditMelons(playerId)
                .withMelonsCount(5)
                .withComment(SOME_COMMENT);
        //when
        builder.build();
        //then
        String comment = findSinglePurchaseByPlayer(playerId).getComment();
        assertThat(comment, is(SOME_COMMENT));
    }

    private Purchase findSinglePurchaseByPlayer(UUID playerId) {
        return client().purchases().findByPlayer(playerId).stream().findFirst().orElseThrow(AssertionError::new);
    }
}
