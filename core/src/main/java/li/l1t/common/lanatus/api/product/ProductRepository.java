/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.lanatus.api.product;

import li.l1t.common.lanatus.api.LanatusRepository;
import li.l1t.common.lanatus.api.exception.NoSuchRowException;

import java.util.Collection;
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
     */
    Product findById(UUID productId) throws NoSuchRowException;

    /**
     * @return the collection of products that are currently known, or an empty collection for none
     */
    Collection<Product> findAll();

    /**
     * @return the collection of products that are currently known and assigned to the current
     * module, or an empty collection if none
     */
    Collection<Product> findByCurrentModule();

    /**
     * Finds a product by its unique name.
     *
     * @param name the name of the product to find
     * @return the product with given name
     * @throws NoSuchRowException if there is no product with given name
     */
    Product findByName(String name) throws NoSuchRowException;

    /**
     * Clears any caches that this repository may keep.
     */
    void clearCache();

    /**
     * Finds the most recent version of given product. If the product did not change, the argument
     * is returned.
     *
     * @param product the product to refresh
     * @return a product object representing the most recent state of given product
     * @throws NoSuchRowException if given product no longer exists
     */
    Product refresh(Product product) throws NoSuchRowException;
}
