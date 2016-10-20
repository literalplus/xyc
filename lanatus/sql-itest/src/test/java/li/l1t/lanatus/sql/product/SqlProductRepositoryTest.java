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
import li.l1t.lanatus.sql.AbstractLanatusSqlTest;
import li.l1t.lanatus.sql.SqlLanatusClient;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * Tests the integration of the sql product repository with the database, verifying only using the
 * repository's own methods.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-20
 */
public class SqlProductRepositoryTest extends AbstractLanatusSqlTest {
    private static final UUID PRODUCT_ID = UUID.randomUUID();

    @BeforeClass
    public static void setupSampleData() throws AccountConflictException {
        SqlLanatusClient client = createClient();
        client.products().registration(PRODUCT_ID)
                .inThisModule()
                .withDisplayName("testing products")
                .withDescription("a test product")
                .withIcon("melon")
                .withMelonsCost(1)
                .register();
    }

    @Test
    public void testFindById__basic() {
        //given PRODUCT_ID
        //when
        Product product = repo().findById(PRODUCT_ID);
        //then
        Assert.assertNotNull(product);
    }

    @Test(expected = NoSuchProductException.class)
    public void testFindById__nonExisting() {
        //given
        UUID nonExistingProductId = UUID.randomUUID();
        //when
        repo().findById(nonExistingProductId);
        //then an exception is thrown
    }

    @Test
    public void testFindById__caching() {
        //given PRODUCT_ID
        //when
        Product first = repo().findById(PRODUCT_ID);
        Product second = repo().findById(PRODUCT_ID);
        //then
        assertSame("cache does not return the same object every time", first, second);
    }

    @Test
    public void testClearCache__single() {
        //given
        Product initial = repo().findById(PRODUCT_ID);
        //when
        repo().clearCache();
        //then
        assertNotSame("clear cache does not purge cache", initial, repo().findById(PRODUCT_ID));
    }

    private SqlProductRepository repo() {
        return client().products();
    }
}
