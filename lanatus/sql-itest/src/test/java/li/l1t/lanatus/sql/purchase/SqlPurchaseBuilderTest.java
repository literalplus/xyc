/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.lanatus.sql.purchase;

import li.l1t.lanatus.api.account.MutableAccount;
import li.l1t.lanatus.api.builder.PurchaseBuilder;
import li.l1t.lanatus.api.exception.AccountConflictException;
import li.l1t.lanatus.api.exception.NotEnoughMelonsException;
import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.api.purchase.Purchase;
import li.l1t.lanatus.sql.AbstractLanatusSqlTest;
import li.l1t.lanatus.sql.SqlLanatusClient;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-20
 */
public class SqlPurchaseBuilderTest extends AbstractLanatusSqlTest {
    private static final UUID PLAYER_WITH_MELONS_ID = UUID.randomUUID();
    private static final UUID PRODUCT_ID = UUID.randomUUID();
    private static final String SOME_COMMENT = "/* no comment */";
    private static final String SOME_DATA = "biiiiig data";
    private static final int SOME_MELONS_COST = 15;

    @BeforeClass
    public static void setupSampleData() throws AccountConflictException {
        SqlLanatusClient client = createClient();
        MutableAccount mutable = client.accounts().findMutable(PLAYER_WITH_MELONS_ID);
        mutable.setMelonsCount(1337);
        client.accounts().save(mutable);
        client.products().registration(PRODUCT_ID).register();
    }

    @Test
    public void testBuild__basic() {
        //given
        Product product = givenAProduct();
        PurchaseBuilder builder = client().startPurchase(PLAYER_WITH_MELONS_ID)
                .withProduct(product);
        //when
        builder.build();
        //then
        Purchase purchase = builder.getPurchase();
        thenTheResultingPurchaseIsValid(builder, purchase);
        thenThePlayerId(is(PLAYER_WITH_MELONS_ID), purchase);
        thenTheOtherValuesAreTheDefaults(product, purchase);
        thenTheResultingPurchaseExistsInTheDatabase(builder);
    }

    private void thenTheResultingPurchaseExistsInTheDatabase(PurchaseBuilder builder) {
        Purchase remotePurchase = client().purchases().findById(builder.getPurchaseId());
        assertNotNull(remotePurchase);
    }

    private void thenTheOtherValuesAreTheDefaults(Product product, Purchase purchase) {
        thenTheMelonsCostIsTheSameAsTheProductCost(product, purchase);
        thenTheData(isEmptyString(), purchase);
        thenTheComment(isEmptyString(), purchase);
        thenThePurchaseWasMadeInThePast(purchase);
    }

    private Product givenAProduct() {
        return client().products().findById(PRODUCT_ID);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuild__noProduct() {
        //given nothing
        //when
        client().startPurchase(UUID.randomUUID()).build();
        //then an exception is thrown
    }

    @Test
    public void testBuild__allParameters() {
        //given
        PurchaseBuilder builder = client().startPurchase(PLAYER_WITH_MELONS_ID)
                .withComment(SOME_COMMENT)
                .withData(SOME_DATA)
                .withMelonsCost(SOME_MELONS_COST)
                .withProductId(PRODUCT_ID);
        //when
        builder.build();
        //then
        Purchase purchase = builder.getPurchase();
        thenTheResultingPurchaseIsValid(builder, purchase);
        thenThePlayerId(is(PLAYER_WITH_MELONS_ID), purchase);
        thenTheComment(is(SOME_COMMENT), purchase);
        thenTheData(is(SOME_DATA), purchase);
        thenTheMelonsCost(is(SOME_MELONS_COST), purchase);
    }

    @Test(expected = NotEnoughMelonsException.class)
    public void testBuild__notEnoughMelons() {
        //given
        PurchaseBuilder builder = client().startPurchase(UUID.randomUUID())
                .withProductId(PRODUCT_ID);
        //when
        builder.build();
        //then an exception is thrown
    }

    @Test
    public void testBuild__melonsCheck() throws AccountConflictException {
        //given
        int melonsCost = 15;
        UUID playerId = givenAPlayerWithMelons(melonsCost);
        PurchaseBuilder builder = client().startPurchase(playerId)
                .withProductId(PRODUCT_ID)
                .withMelonsCost(melonsCost);
        //when
        builder.build();
        //then
        thenThePlayerHasNoMoreMelonsLeft(playerId);
    }

    private void thenThePlayerHasNoMoreMelonsLeft(UUID playerId) {
        assertThat(findAccount(playerId).getMelonsCount(), is(0));
    }

    private void thenThePlayerId(Matcher<UUID> matcher, Purchase purchase) {
        assertThat(purchase.getPlayerId(), matcher);
    }

    private void thenTheResultingPurchaseIsValid(PurchaseBuilder builder, Purchase purchase) {
        assertNotNull(purchase);
        assertThat(purchase.getUniqueId(), is(builder.getPurchaseId()));
    }

    private void thenTheMelonsCostIsTheSameAsTheProductCost(Product product, Purchase purchase) {
        thenTheMelonsCost(is(product.getMelonsCost()), purchase);
    }

    private void thenTheMelonsCost(Matcher<Integer> matcher, Purchase purchase) {
        assertThat(purchase.getMelonsCost(), matcher);
    }

    private void thenTheData(Matcher<String> matcher, Purchase purchase) {
        assertThat(purchase.getData(), matcher);
    }

    private void thenTheComment(Matcher<String> matcher, Purchase purchase) {
        assertThat(purchase.getComment(), matcher);
    }

    private void thenThePurchaseWasMadeInThePast(Purchase purchase) {
        assertThat(Instant.now().plusMillis(1L).isAfter(purchase.getCreationInstant()), is(true));
    }
}
