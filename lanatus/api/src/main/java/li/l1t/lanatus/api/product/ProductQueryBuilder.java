/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.api.product;

import li.l1t.common.exception.DatabaseException;
import li.l1t.lanatus.api.LanatusConnected;

import java.util.Collection;

/**
 * A builder for queries resulting in a collection of products.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
public interface ProductQueryBuilder extends LanatusConnected {
    /**
     * @param moduleName the name of the module to select
     * @return this builder
     */
    ProductQueryBuilder inModule(String moduleName);

    /**
     * Selects the current module.
     *
     * @return this builder
     */
    ProductQueryBuilder inThisModule();

    /**
     * Selects no module, making the query return products in all modules.
     *
     * @return this builder
     */
    ProductQueryBuilder inAnyModule();

    /**
     * @param nameFilter the name to select
     * @return this builder
     */
    ProductQueryBuilder withName(String nameFilter);

    /**
     * Selects only active products, meaning that the query will only return active products.
     *
     * @return this builder
     */
    ProductQueryBuilder andActive();

    /**
     * Executes the query resulting from the current state of this builder.
     *
     * @return the result of the query, or an empty collection if there is no such product
     * @throws DatabaseException if a database error occurs
     */
    Collection<Product> execute() throws DatabaseException;
}
