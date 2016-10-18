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

/**
 * Fluent builder interface for product registrations, providing modules with a way to make sure
 * their products actually exist when they connect to Lanatus.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-18
 */
public interface ProductRegistrationBuilder {
    /**
     * @param moduleName the name of the module to select
     * @return this builder
     */
    ProductRegistrationBuilder inModule(String moduleName);

    /**
     * Selects the current module.
     *
     * @return this builder
     */
    ProductRegistrationBuilder inThisModule();

    /**
     * @param displayName the display name to set on the builder
     * @return this builder
     */
    ProductRegistrationBuilder withDisplayName(String displayName);

    /**
     * @param description the description to set on the builder
     * @return this builder
     */
    ProductRegistrationBuilder withDescription(String description);

    /**
     * @param iconName the {@linkplain Product#getIconName() icon name} to set on the builder
     * @return this builder
     */
    ProductRegistrationBuilder withIcon(String iconName);

    /**
     * @param melonsCost the default melons cost to set on the builder
     * @return this builder
     * @throws IllegalArgumentException if given cost is negative
     */
    ProductRegistrationBuilder withMelonsCost(int melonsCost);

    /**
     * Executes the registration represented by this builder. Registrations where there is already a
     * product by that unique id are silently ignored. Note that only the unique id is actually
     * checked, to allow administrators to customise display names and other parts of the product
     * appearance.
     *
     * @return the newly created product, or the already existing product with the builder's unique
     * id
     * @throws DatabaseException if a database error occurs
     */
    Product register() throws DatabaseException;
}
