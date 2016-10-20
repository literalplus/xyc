/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.lanatus.sql.position;

import li.l1t.lanatus.api.account.MutableAccount;
import li.l1t.lanatus.api.builder.PurchaseBuilder;
import li.l1t.lanatus.api.exception.AccountConflictException;
import li.l1t.lanatus.api.position.Position;
import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.sql.AbstractLanatusSqlTest;
import li.l1t.lanatus.sql.SqlLanatusClient;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests the integration of the sql position repository with the database, verifying only using the
 * repository's own methods.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-21
 */
public class SqlPositionRepositoryTest extends AbstractLanatusSqlTest {
    private static final UUID PLAYER_ID = UUID.randomUUID();
    private static final UUID PERMANENT_PRODUCT_ID = UUID.randomUUID();
    private static final UUID TEMPORARY_PRODUCT_ID = UUID.randomUUID();
    private static UUID TEMPORARY_PURCHASE_ID;
    private static UUID PERMANENT_PURCHASE_ID;

    @BeforeClass
    public static void setupSampleData() throws AccountConflictException {
        SqlLanatusClient client = createClient();
        MutableAccount mutable = client.accounts().findMutable(PLAYER_ID);
        mutable.setMelonsCount(1337);
        client.accounts().save(mutable);
        Product permProduct = client.products().registration(PERMANENT_PRODUCT_ID).register();
        PERMANENT_PURCHASE_ID = purchase(client, permProduct);
        Product tempProduct = client.products()
                .registration(TEMPORARY_PRODUCT_ID)
                .withPermanent(false).register();
        TEMPORARY_PURCHASE_ID = purchase(client, tempProduct);
    }

    private static UUID purchase(SqlLanatusClient client, Product product) {
        PurchaseBuilder builder = client.startPurchase(PLAYER_ID).withProduct(product);
        builder.build();
        return builder.getPurchaseId();
    }

    @Test
    public void testFindByPurchase__permanent() {
        //given PERMANENT_PURCHASE_ID
        //when
        Optional<Position> optional = client().positions().findByPurchase(PERMANENT_PURCHASE_ID);
        //then
        assertTrue(optional.isPresent());
        Position position = optional.get();
        assertThat(position.getPlayerId(), is(PLAYER_ID));
        assertThat(position.getProduct().getUniqueId(), is(PERMANENT_PRODUCT_ID));
        assertThat(position.getPurchaseId(), is(PERMANENT_PURCHASE_ID));
    }

    @Test
    public void testFindByPurchase__nonPermanent() {
        //given TEMPORARY_PURCHASE_ID
        //when
        Optional<Position> optional = client().positions().findByPurchase(TEMPORARY_PURCHASE_ID);
        //then
        assertFalse(optional.isPresent());
    }

    @Test
    public void testFindAllByPlayer__basic() {
        //given PLAYER_ID, TEMPORARY_PRODUCT_ID, PERMANENT_PRODUCT_ID
        //when
        Collection<Position> results = client().positions().findAllByPlayer(PLAYER_ID);
        //then
        assertThat(results, hasItem(withProductId(is(PERMANENT_PRODUCT_ID))));
        assertThat(results, not(hasItem(withProductId(is(TEMPORARY_PRODUCT_ID)))));
    }

    private Matcher<Object> withProductId(Matcher<UUID> valueMatcher) {
        return hasProperty(
                "product", hasProperty("uniqueId", valueMatcher)
        );
    }

    @Test
    public void testPlayerHasProduct__permanent() {
        //given PLAYER_ID
        //when
        boolean hasProduct = client().positions().playerHasProduct(PLAYER_ID, PERMANENT_PRODUCT_ID);
        //then
        assertTrue(hasProduct);
    }

    @Test
    public void testPlayerHasProduct__temporary() {
        //given PLAYER_ID
        //when
        boolean hasProduct = client().positions().playerHasProduct(PLAYER_ID, TEMPORARY_PRODUCT_ID);
        //then
        assertFalse(hasProduct);
    }

    @Test
    public void testPlayerHasProduct__nonExisting() {
        //given PLAYER_ID
        //when
        boolean hasProduct = client().positions().playerHasProduct(PLAYER_ID, UUID.randomUUID());
        //then
        assertFalse(hasProduct);
    }
}
