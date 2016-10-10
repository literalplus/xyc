/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql;

import li.l1t.common.sql.sane.AbstractSqlConnected;
import li.l1t.common.sql.sane.SaneSql;
import li.l1t.lanatus.api.LanatusClient;
import li.l1t.lanatus.api.account.AccountRepository;
import li.l1t.lanatus.api.position.PositionRepository;
import li.l1t.lanatus.api.product.ProductRepository;
import li.l1t.lanatus.api.purchase.PurchaseRepository;
import li.l1t.lanatus.sql.account.SqlAccountRepository;
import li.l1t.lanatus.sql.product.SqlProductRepository;

/**
 * An implementation of a Lanatus client using a SQL database as backend.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public class SqlLanatusClient extends AbstractSqlConnected implements LanatusClient {
    private final String module;
    private SqlAccountRepository accountRepository = new SqlAccountRepository(this);
    private ProductRepository productRepository = new SqlProductRepository(this);

    /**
     * Constructs a new SQL Lanatus client.
     *
     * @param sql    the database connection to use
     * @param module the name of the module using this client
     */
    public SqlLanatusClient(SaneSql sql, String module) {
        super(sql);
        this.module = module;
    }

    @Override
    public String getModuleName() {
        return module;
    }

    @Override
    public AccountRepository accounts() {
        return accountRepository;
    }

    @Override
    public PositionRepository positions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PurchaseRepository purchases() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductRepository products() {
        return productRepository;
    }
}
