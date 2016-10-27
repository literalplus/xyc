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

import com.google.common.base.Preconditions;
import li.l1t.lanatus.api.LanatusClient;
import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.api.product.ProductQueryBuilder;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Builder for database queries returning products.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class SqlProductQueryBuilder implements ProductQueryBuilder {
    private final SqlProductRepository repository;
    private String module = null;
    private String searchTerm = "";
    private boolean activeOnly = false;

    SqlProductQueryBuilder(SqlProductRepository repository) {
        this.repository = Preconditions.checkNotNull(repository, "repository");
    }

    @Override
    public ProductQueryBuilder inModule(String moduleName) {
        module = moduleName;
        return this;
    }

    @Override
    public ProductQueryBuilder inThisModule() {
        return inModule(repository.client().getModuleName());
    }

    @Override
    public ProductQueryBuilder inAnyModule() {
        return inModule(null);
    }

    @Override
    public ProductQueryBuilder withName(String nameFilter) {
        //no longer supported - deprecated
        return this;
    }

    @Override
    public ProductQueryBuilder containing(@Nonnull String searchTerm) {
        Preconditions.checkNotNull(searchTerm, "searchTerm");
        this.searchTerm = searchTerm;
        return this;
    }

    @Override
    public ProductQueryBuilder andActive() {
        activeOnly = true;
        return this;
    }

    @Override
    public Collection<Product> execute() {
        return repository.execute(new ProductQuery(this));
    }

    String getModule() {
        return module;
    }

    String getSearchTerm() {
        return searchTerm;
    }

    boolean isActiveOnly() {
        return activeOnly;
    }

    @Override
    public LanatusClient client() {
        return repository.client();
    }
}
