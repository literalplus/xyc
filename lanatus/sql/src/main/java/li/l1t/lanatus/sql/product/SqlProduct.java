/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql.product;

import li.l1t.lanatus.api.product.Product;

import java.util.UUID;

/**
 * Represents a product backed by a SQL data source.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class SqlProduct implements Product {
    private final UUID uniqueId;
    private final String module;
    private String displayName;
    private String description;
    private String iconName;
    private int melonsCost;
    private boolean active;

    SqlProduct(UUID uniqueId, String module, String displayName,
               String description, String iconName, int melonsCost, boolean active) {
        this.uniqueId = uniqueId;
        this.module = module;
        this.displayName = displayName;
        this.description = description;
        this.iconName = iconName;
        this.melonsCost = melonsCost;
        this.active = active;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getModule() {
        return module;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getIconName() {
        return iconName;
    }

    @Override
    public int getMelonsCost() {
        return melonsCost;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
