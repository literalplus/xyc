/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.test.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Mock of item meta for fun testing.
 * <p>
 * <b>Warning:</b> This impl does not support serialisation.
 * </p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-14
 */
public class MockItemMeta implements ItemMeta {
    private String displayName;
    private List<String> lore = new ArrayList<>();
    private Map<Enchantment, Integer> enchants = new HashMap<>();
    private Set<ItemFlag> itemFlags = EnumSet.noneOf(ItemFlag.class);
    private Spigot spigot = new Spigot();

    public MockItemMeta() {
    }

    public MockItemMeta(ItemMeta toCopy) {
        this(toCopy.getDisplayName(), toCopy.getLore(), toCopy.getEnchants(), toCopy.getItemFlags(), toCopy.spigot());
    }

    public MockItemMeta(String displayName, List<String> lore, Map<Enchantment, Integer> enchants, Set<ItemFlag> itemFlags, Spigot spigot) {
        this.displayName = displayName;
        this.lore = lore;
        this.enchants = enchants;
        this.itemFlags = itemFlags;
        this.spigot = spigot;
    }

    public static MockItemMeta deserialize(Map<String, Object> source) {
        return new MockItemMeta();
    }

    @Override
    public boolean hasDisplayName() {
        return displayName != null;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String name) {
        this.displayName = name;
    }

    @Override
    public boolean hasLore() {
        return !lore.isEmpty();
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    @Override
    public void setLore(List<String> lore) {
        if (lore == null) {
            lore = new ArrayList<>();
        }
        this.lore = lore;
    }

    @Override
    public boolean hasEnchants() {
        return !enchants.isEmpty();
    }

    @Override
    public boolean hasEnchant(Enchantment ench) {
        return enchants.containsKey(ench);
    }

    @Override
    public int getEnchantLevel(Enchantment ench) {
        return enchants.getOrDefault(ench, 0);
    }

    @Override
    public Map<Enchantment, Integer> getEnchants() {
        return enchants;
    }

    @Override
    public boolean addEnchant(Enchantment ench, int level, boolean ignoreLevelRestriction) {
        return !((level > ench.getMaxLevel()) && !ignoreLevelRestriction) &&
                enchants.put(ench, level) != level;
    }

    @Override
    public boolean removeEnchant(Enchantment ench) {
        return enchants.remove(ench) != null;
    }

    @Override
    public boolean hasConflictingEnchant(Enchantment ench) {
        return enchants.keySet().stream()
                .anyMatch(ench::conflictsWith);
    }

    @Override
    public void addItemFlags(ItemFlag... itemFlags) {
        this.itemFlags.addAll(Arrays.asList(itemFlags));
    }

    @Override
    public void removeItemFlags(ItemFlag... itemFlags) {
        this.itemFlags.removeAll(Arrays.asList(itemFlags));
    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    @Override
    public boolean hasItemFlag(ItemFlag flag) {
        return itemFlags.contains(flag);
    }

    @Override
    public ItemMeta clone() {
        return new MockItemMeta(this);
    }

    @Override
    public Spigot spigot() {
        return spigot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MockItemMeta)) return false;

        MockItemMeta that = (MockItemMeta) o;

        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null)
            return false;
        if (lore != null ? !lore.equals(that.lore) : that.lore != null) return false;
        if (enchants != null ? !enchants.equals(that.enchants) : that.enchants != null)
            return false;
        if (itemFlags != null ? !itemFlags.equals(that.itemFlags) : that.itemFlags != null)
            return false;
        return spigot != null ? spigot.equals(that.spigot) : that.spigot == null;

    }

    @Override
    public int hashCode() {
        int result = displayName != null ? displayName.hashCode() : 0;
        result = 31 * result + (lore != null ? lore.hashCode() : 0);
        result = 31 * result + (enchants != null ? enchants.hashCode() : 0);
        result = 31 * result + (itemFlags != null ? itemFlags.hashCode() : 0);
        result = 31 * result + (spigot != null ? spigot.hashCode() : 0);
        return result;
    }

    @Override
    public Map<String, Object> serialize() {
        return new HashMap<>();
    }
}
