/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.lanatus.sql.product;

import li.l1t.lanatus.api.exception.AccountConflictException;
import li.l1t.lanatus.api.exception.NoSuchProductException;
import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.api.product.ProductRegistrationBuilder;
import li.l1t.lanatus.sql.AbstractLanatusSqlTest;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.junit.Assume.assumeTrue;

/**
 * Tests the integration of the sql product registration builder with the database, verifying only
 * using the repository's methods.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-20
 */
public class SqlProductRegistrationBuilderTest extends AbstractLanatusSqlTest {
    private static final UUID PRODUCT_ID = UUID.randomUUID();
    private static final String OTHER_MODULE = "xyc-it-something";
    private static final String SOME_DISPLAY_NAME = "display name";
    private static final String SOME_DESCRIPTION = "description";
    private static final String SOME_ICON_NAME = "melon";
    private static final int SOME_MELONS_COST = 420;

    @BeforeClass
    public static void setupSampleData() throws AccountConflictException {
//        SqlLanatusClient client = createClient();
//        client.products().registration(PRODUCT_ID)
//                .inThisModule()
//                .withDisplayName("testing products")
//                .withDescription("a test product")
//                .withIcon("melon")
//                .withMelonsCost(1)
//                .register();
    }

    @Test
    public void testRegister__minimal() {
        //given
        UUID productId = UUID.randomUUID();
        givenTheProductDoesNotExist(productId);
        ProductRegistrationBuilder registration = repo().registration(productId);
        //when
        Product result = registration.register();
        //then
        thenTheResultingProductMatchesTheDefaults(productId, result);
    }

    private void thenTheResultingProductMatchesTheDefaults(UUID productId, Product result) {
        assertNotNull(result);
        thenTheProductId(is(productId), result);
        thenTheModuleName(is(client().getModuleName()), result);
        thenTheDisplayName(isEmptyString(), result);
        thenTheDescription(isEmptyString(), result);
        thenTheIcon(isEmptyString(), result);
        thenTheMelonsCost(is(1), result);
        thenThePermanentState(is(true), result);
        thenTheActiveState(is(true), result);
    }

    @Test
    public void testRegister__allParameters() {
        //given
        UUID productId = UUID.randomUUID();
        givenTheProductDoesNotExist(productId);
        ProductRegistrationBuilder registration = givenABuilderWithConstantValues(productId);
        //when
        Product result = registration.register();
        //then
        thenTheResultingProductMatchesTheseValues(productId, result);
    }

    private ProductRegistrationBuilder givenABuilderWithConstantValues(UUID productId) {
        return repo().registration(productId)
                .inModule(OTHER_MODULE)
                .withDisplayName(SOME_DISPLAY_NAME)
                .withDescription(SOME_DESCRIPTION)
                .withIcon(SOME_ICON_NAME)
                .withMelonsCost(SOME_MELONS_COST)
                .withPermanent(false);
    }

    private void thenTheResultingProductMatchesTheseValues(UUID productId, Product result) {
        assertNotNull(result);
        thenTheProductId(is(productId), result);
        thenTheModuleName(is(OTHER_MODULE), result);
        thenTheDisplayName(is(SOME_DISPLAY_NAME), result);
        thenTheDescription(is(SOME_DESCRIPTION), result);
        thenTheIcon(is(SOME_ICON_NAME), result);
        thenTheMelonsCost(is(SOME_MELONS_COST), result);
        thenThePermanentState(is(false), result);
        thenTheActiveState(is(true), result);
    }

    @Test
    public void testRegister__existing() {
        //given
        UUID productId = UUID.randomUUID();
        givenABuilderWithConstantValues(productId).register();
        givenTheProductExists(productId);
        ProductRegistrationBuilder builder = givenABuilderWithModifiedValues(productId);
        //when
        Product result = builder.register();
        //then
        thenTheProductIsNotChangedFromThePreviousValues(productId, result);
    }

    private ProductRegistrationBuilder givenABuilderWithModifiedValues(UUID productId) {
        return repo().registration(productId)
                .inModule(OTHER_MODULE + "aa")
                .withDisplayName(SOME_DISPLAY_NAME + "aa")
                .withDescription(SOME_DESCRIPTION + "aa")
                .withIcon(SOME_ICON_NAME + "aa")
                .withMelonsCost(SOME_MELONS_COST + 1337)
                .withPermanent(true);
    }

    private void thenTheProductIsNotChangedFromThePreviousValues(UUID productId, Product result) {
        thenTheResultingProductMatchesTheseValues(productId, result);
    }

    private void givenTheProductExists(UUID productId) {
        assumeThat(repo().findById(productId), is(not(nullValue())));
    }

    private void thenTheProductId(Matcher<UUID> matcher, Product result) {
        assertThat("expected unique id", result.getUniqueId(), matcher);
    }

    private void thenTheModuleName(Matcher<String> matcher, Product result) {
        assertThat("expected module name", result.getModule(), matcher);
    }

    private void thenTheDisplayName(Matcher<String> matcher, Product result) {
        assertThat("expected display name", result.getDisplayName(), matcher);
    }

    private void thenTheDescription(Matcher<String> matcher, Product result) {
        assertThat("expected description", result.getDescription(), matcher);
    }

    private void thenTheIcon(Matcher<String> matcher, Product result) {
        assertThat("expected icon", result.getIconName(), matcher);
    }

    private void thenTheMelonsCost(Matcher<Integer> matcher, Product result) {
        assertThat("expected melons cost", result.getMelonsCost(), matcher);
    }

    private void thenThePermanentState(Matcher<Boolean> matcher, Product result) {
        assertThat("expected permanent", result.isPermanent(), matcher);
    }

    private void thenTheActiveState(Matcher<Boolean> matcher, Product result) {
        assertThat("expected active", result.isActive(), matcher);
    }

    private void givenTheProductDoesNotExist(UUID productId) {
        try {
            repo().findById(productId);
            assumeTrue("product already exists!", false);
        } catch (NoSuchProductException ignore) {
            //expected behaviour
        }
    }

    private SqlProductRepository repo() {
        return client().products();
    }
}
