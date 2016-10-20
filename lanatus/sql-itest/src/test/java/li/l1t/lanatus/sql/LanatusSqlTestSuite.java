/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.lanatus.sql;

import li.l1t.lanatus.sql.account.SqlAccountRepositoryTest;
import li.l1t.lanatus.sql.builder.melons.SqlCreditMelonsBuilderTest;
import li.l1t.lanatus.sql.product.SqlProductRegistrationBuilderTest;
import li.l1t.lanatus.sql.product.SqlProductRepositoryTest;
import li.l1t.lanatus.sql.purchase.SqlPurchaseBuilderTest;
import li.l1t.lanatus.sql.purchase.SqlPurchaseRepositoryTest;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A test suite for all Lanatus-SQL integration tests, handling database setup once for all included
 * tests.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-20
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SqlAccountRepositoryTest.class,
        SqlProductRepositoryTest.class, SqlProductRegistrationBuilderTest.class,
        SqlPurchaseRepositoryTest.class, SqlPurchaseBuilderTest.class,
        SqlCreditMelonsBuilderTest.class
})
public class LanatusSqlTestSuite {
    @ClassRule
    public static final DatabaseSetup SETUP = new DatabaseSetup();
}
