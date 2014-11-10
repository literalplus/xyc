/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.util.inventory;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This factory helps with creating {@link org.bukkit.inventory.ItemStack}s.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 30/01/14
 */
@SuppressWarnings("UnusedDeclaration")
public final class ItemStackFactory {
    @NotNull
    private final ItemStack base;
    private String displayName;
    private List<String> lore;
    private MaterialData materialData;
    private ItemMeta meta;

    /**
     * Creates a factory from a base {@link org.bukkit.inventory.ItemStack}.
     *
     * @param source Item stack to use as base for this factory.
     */
    public ItemStackFactory(@NotNull final ItemStack source) {
        base = source;
        materialData = source.getData();

        if (source.hasItemMeta()) {
            meta = source.getItemMeta();
            if (meta.hasDisplayName()) {
                displayName = meta.getDisplayName();
            }
            if (meta.hasLore()) {
                lore = meta.getLore();
            }
        }
    }

    /**
     * Creates a factory by a {@link org.bukkit.Material}.
     * The resulting stack will have an amount of 1.
     *
     * @param material Material the product shall be
     * @see org.bukkit.inventory.ItemStack#ItemStack(org.bukkit.Material)
     */
    public ItemStackFactory(final Material material) {
        base = new ItemStack(material);
    }

    /**
     * Sets the product's amount.
     *
     * @param newAmount New amount
     * @return This object for chained calls.
     */
    public ItemStackFactory amount(final int newAmount) {
        base.setAmount(newAmount);
        return this;
    }

    /**
     * @param displayName Future display name of the product.
     * @return This object for chained calls.
     */
    public ItemStackFactory displayName(final String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Sets the display name of this factory if the resulting stack would not have a custom display name.
     * @param defaultDisplayName the display name to ste
     * @return this object
     */
    public ItemStackFactory defaultDisplayName(final String defaultDisplayName) {
        if (!(base.hasItemMeta() && base.getItemMeta().hasDisplayName()) || displayName == null) {
            return displayName(defaultDisplayName);
        }

        return this;
    }

    /**
     * Sets the resulting item stack's lore, overriding any previous values.
     *
     * @param lore Future lore of the product.
     * @return This object for chained calls.
     */
    public ItemStackFactory lore(final List<String> lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Appends a collection of strings to the resulting item stack's lore, treating every element as a separate line.
     * If this factory was constructed with a template item stack, this method will append to its existing lore, if any.
     * @param loreToAppend the lines to add to the lore
     * @return this object
     */
    public ItemStackFactory appendLore(final Collection<String> loreToAppend) {
        if (this.lore == null) {
            return lore(loreToAppend instanceof List ? (List<String>) loreToAppend : new ArrayList<>(loreToAppend));
        }

        this.lore.addAll(loreToAppend);

        return this;
    }

    /**
     * This method adds a String to the lore of the product.
     * If given a simple String, it will be added as new line.
     * If given a String containing newlines, it will split the input by {@code \n} and add each result String to the lore.
     * If the factory was constructed with a template item stack, this will be appended to its existing lore, if any.
     *
     * @param whatToAdd What to add to the future Lore of the product.
     * @return This object for chained calls.
     */
    public ItemStackFactory lore(final String whatToAdd) {
        if (lore == null) {
            lore = new LinkedList<>();
        }

        Collections.addAll(lore, whatToAdd.split("\n"));

        return this;
    }

    /**
     * Adds an enchmantment to the product.
     *
     * @param enchantment Enchantment to apply
     * @param level       Level of the enchantment
     * @return This object, for chained calls.
     */
    public ItemStackFactory enchant(final Enchantment enchantment, final int level) {
        base.addEnchantment(enchantment, level);
        return this;
    }

    /**
     * @param newData Future {@link org.bukkit.material.MaterialData} for the product.
     * @return This object for chained calls.
     */
    public ItemStackFactory materialData(final MaterialData newData) {
        materialData = newData;

        return this;
    }

    /**
     * Convenience method for making wool stacks.
     *
     * @param color Future DyeColor of the product.
     * @return This object for chained calls.
     * @throws IllegalArgumentException If the base stack is not of material WOOL.
     * @see ItemStackFactory#materialData(org.bukkit.material.MaterialData)
     */
    public ItemStackFactory woolColor(final DyeColor color) {
        Validate.isTrue(base.getType() == Material.WOOL, "Material of base stack must be WOOL (" + base.getType() + ')');

        materialData = new Wool(color);

        return this;
    }

    /**
     * Convenience method for making colored leather armor.
     *
     * @param color Future DyeColor of the product.
     * @return This object for chained calls.
     * @throws IllegalArgumentException If the base stack is not leather armor.
     * @see ItemStackFactory#materialData(org.bukkit.material.MaterialData)
     */
    public ItemStackFactory leatherArmorColor(final Color color) {
        if(meta == null) {
            meta = base.getItemMeta();
        }

        Validate.isTrue(meta instanceof LeatherArmorMeta, "Base stack must be leather armor (" + base.getType() + ')');

        ((LeatherArmorMeta) meta).setColor(color);

        return this;
    }

    /**
     * Convenience method for making player skulls.
     *
     * @param ownerName Future skull owner of the product.
     * @return This object for chained calls.
     * @throws IllegalArgumentException If the base stack is not of material SKULL_ITEM.
     * @see ItemStackFactory#materialData(org.bukkit.material.MaterialData)
     */
    public ItemStackFactory skullOwner(final String ownerName) {
        Validate.isTrue(base.getType() == Material.SKULL_ITEM, "Material of base stack must be SKULL_ITEM (" + base.getType() + ')');

        meta = base.getItemMeta();
        ((SkullMeta) meta).setOwner(ownerName);

        return this;
    }

    public ItemStack produce() {
        final ItemStack product = new ItemStack(base);

        if (materialData != null) {
            product.setData(materialData);
        }

        if (displayName != null || lore != null) {
            final ItemMeta finalMeta = (meta == null ? product.getItemMeta() : meta);

            if (lore != null) {
                finalMeta.setLore(lore);
            }

            if (displayName != null) {
                finalMeta.setDisplayName(displayName);
            }

            product.setItemMeta(finalMeta);
        }

        return product;
    }

    @NotNull
    public ItemStack getBase() {
        return this.base;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public MaterialData getMaterialData() {
        return this.materialData;
    }
}
