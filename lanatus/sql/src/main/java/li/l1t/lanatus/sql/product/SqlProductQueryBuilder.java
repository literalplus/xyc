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
