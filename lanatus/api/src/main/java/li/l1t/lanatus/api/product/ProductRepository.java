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
import li.l1t.lanatus.api.LanatusRepository;
import li.l1t.lanatus.api.exception.NoSuchRowException;

import java.util.UUID;

/**
 * A repository for products that may be owned through Lanatus. Note that results may be heavily
 * cached in order to reduce database calls at the cost that product information may not always be
 * up to date.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public interface ProductRepository extends LanatusRepository {
    /**
     * Finds a product by its unique id, first querying any caches that might exist and then falling
     * back to the database if the product in question is not cached. Note that subsequent calls for
     * the same non-existent product id trigger subsequent database queries.
     *
     * @param productId the unique id of the product
     * @return the product with given unique id
     * @throws NoSuchRowException if there is no product with given id
     * @throws DatabaseException  if a database error occurs
     */
    Product findById(UUID productId) throws NoSuchRowException, DatabaseException;

    /**
     * Creates a new query builder. Note that queries ignore the product cache.
     *
     * @return a builder for product queries in this repository
     */
    ProductQueryBuilder query();
}
