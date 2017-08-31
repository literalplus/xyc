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

package li.l1t.lanatus.sql;

import li.l1t.lanatus.sql.account.SqlAccountRepositoryTest;
import li.l1t.lanatus.sql.builder.melons.SqlCreditMelonsBuilderTest;
import li.l1t.lanatus.sql.position.SqlPositionRepositoryTest;
import li.l1t.lanatus.sql.product.SqlProductQueryBuilderTest;
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
        SqlPositionRepositoryTest.class,
        SqlProductRepositoryTest.class, SqlProductRegistrationBuilderTest.class, SqlProductQueryBuilderTest.class,
        SqlPurchaseRepositoryTest.class, SqlPurchaseBuilderTest.class,
        SqlCreditMelonsBuilderTest.class
})
public class LanatusSqlTestSuite {
    @ClassRule
    public static final DatabaseSetup SETUP = new DatabaseSetup();
}
