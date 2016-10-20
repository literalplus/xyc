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
