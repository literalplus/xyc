/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
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
