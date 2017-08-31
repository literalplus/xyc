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
import li.l1t.common.exception.DatabaseException;
import li.l1t.common.sql.sane.AbstractSqlConnected;
import li.l1t.lanatus.api.LanatusClient;
import li.l1t.lanatus.api.LanatusConnected;
import li.l1t.lanatus.api.exception.NoSuchProductException;
import li.l1t.lanatus.api.product.Product;
import li.l1t.lanatus.api.product.ProductRegistrationBuilder;

import java.util.UUID;

/**
 * A product registration builder backed by a JDBC SQL data source.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-18
 */
public class SqlProductRegistrationBuilder extends AbstractSqlConnected implements ProductRegistrationBuilder, LanatusConnected {
    private final SqlProductRepository repository;
    private final UUID productId;
    private String moduleName;
    private String displayName = "";
    private String description = "";
    private String iconName = "";
    private int melonsCost = 1;
    private boolean permanent = true;

    public SqlProductRegistrationBuilder(SqlProductRepository repository, UUID productId) {
        super(repository.client().sql());
        this.repository = repository;
        this.productId = productId;
        inThisModule(); //sane default
    }

    @Override
    public ProductRegistrationBuilder inModule(String moduleName) {
        Preconditions.checkNotNull(moduleName, "moduleName");
        this.moduleName = moduleName;
        return this;
    }

    @Override
    public ProductRegistrationBuilder inThisModule() {
        return inModule(client().getModuleName());
    }

    @Override
    public ProductRegistrationBuilder withDisplayName(String displayName) {
        Preconditions.checkNotNull(displayName, "displayName");
        this.displayName = displayName;
        return this;
    }

    @Override
    public ProductRegistrationBuilder withDescription(String description) {
        Preconditions.checkNotNull(description, "description");
        this.description = description;
        return this;
    }

    @Override
    public ProductRegistrationBuilder withIcon(String iconName) {
        Preconditions.checkNotNull(iconName, "iconName");
        this.iconName = iconName;
        return this;
    }

    @Override
    public ProductRegistrationBuilder withPermanent(boolean permanent) {
        this.permanent = permanent;
        return this;
    }

    @Override
    public ProductRegistrationBuilder withMelonsCost(int melonsCost) {
        Preconditions.checkArgument(melonsCost >= 0, "melonsCost must not be negative!");
        this.melonsCost = melonsCost;
        return this;
    }

    @Override
    public Product register() throws DatabaseException {
        try {
            return repository.findById(productId);
        } catch (NoSuchProductException e) {
            return createProductFromBuilderState();
        }
    }

    private Product createProductFromBuilderState() {
        SqlProduct createdProduct = new SqlProduct(
                productId, moduleName, displayName, description, iconName, melonsCost, true, permanent
        );
        repository.createNewProduct(createdProduct);
        return createdProduct;
    }

    @Override
    public LanatusClient client() {
        return repository.client();
    }
}
