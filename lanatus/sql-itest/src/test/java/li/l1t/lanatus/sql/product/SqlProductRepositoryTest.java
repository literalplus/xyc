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
