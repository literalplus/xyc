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

import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.api.product.ProductQueryBuilder;
import li.l1t.lanatus.sql.AbstractLanatusSqlTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

/**
 * Tests the integration of the sql product query builder with the database, verifying only using
 * the repository's methods.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-21
 */
public class SqlProductQueryBuilderTest extends AbstractLanatusSqlTest {
    @Test
    public void testQuery__any() {
        //given
        UUID productId = givenSomeProduct();
        ProductQueryBuilder builder = client().products().query().inAnyModule();
        //when
        Collection<Product> results = builder.execute();
        //then
        thenTheResultHasAnItemWithId(productId, results);
    }

    private UUID givenSomeProduct() {
        UUID productId = UUID.randomUUID();
        client().products().registration(productId).register();
        return productId;
    }

    @Test
    public void testQuery__activeOnlyNaive() {
        //given
        UUID productId = givenSomeProduct();
        ProductQueryBuilder builder = client().products().query().inAnyModule().andActive();
        //when
        Collection<Product> results = builder.execute();
        //then
        thenTheResultHasAnItemWithId(productId, results);
    }

    private void thenTheResultHasAnItemWithId(UUID productId, Collection<Product> results) {
        Assert.assertThat(results, hasItem(hasProperty("uniqueId", is(productId))));
    }
}
