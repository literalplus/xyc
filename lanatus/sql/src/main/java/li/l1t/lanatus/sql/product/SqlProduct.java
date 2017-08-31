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
    private boolean permanent;

    SqlProduct(UUID uniqueId, String module, String displayName,
               String description, String iconName, int melonsCost, boolean active, boolean permanent) {
        this.uniqueId = Preconditions.checkNotNull(uniqueId, "uniqueId");
        this.module = Preconditions.checkNotNull(module, "module");
        this.displayName = Preconditions.checkNotNull(displayName, "diplayName");
        this.description = Preconditions.checkNotNull(description, "description");
        this.iconName = Preconditions.checkNotNull(iconName, "iconName");
        this.melonsCost = melonsCost;
        this.active = active;
        this.permanent = permanent;
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

    @Override
    public boolean isPermanent() {
        return permanent;
    }

    @Override
    public String toString() {
        return "SqlProduct: " + uniqueId + " " +
                "(\"" + displayName + "\") " +
                "in " + module + " " +
                "with icon='" + iconName + "', " +
                "description '" + description + "'," +
                "active: " + active +
                ", perm: " + permanent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SqlProduct)) return false;
        SqlProduct that = (SqlProduct) o;
        return uniqueId.equals(that.uniqueId);
    }

    @Override
    public int hashCode() {
        return uniqueId.hashCode();
    }
}
