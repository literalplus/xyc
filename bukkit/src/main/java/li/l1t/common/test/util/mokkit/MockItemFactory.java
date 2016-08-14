/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.test.util.mokkit;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A mock item factory because Bukkit loves being hard to unit test and we need this for getItemMeta
 * in ItemStack.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-14
 */
public class MockItemFactory implements ItemFactory {
    @Override
    public ItemMeta getItemMeta(Material material) {
        return new MockItemMeta();
    }

    @Override
    public boolean isApplicable(ItemMeta meta, ItemStack stack) throws IllegalArgumentException {
        return true; //let's see how much this breaks
    }

    @Override
    public boolean isApplicable(ItemMeta meta, Material material) throws IllegalArgumentException {
        return true;
    }

    @Override
    public boolean equals(ItemMeta meta1, ItemMeta meta2) throws IllegalArgumentException {
        return meta1 == meta2 || !(meta1 != null && !meta1.equals(meta2)) && meta2.equals(meta1);
    }

    @Override
    public ItemMeta asMetaFor(ItemMeta meta, ItemStack stack) throws IllegalArgumentException {
        return meta;
    }

    @Override
    public ItemMeta asMetaFor(ItemMeta meta, Material material) throws IllegalArgumentException {
        return meta;
    }

    @Override
    public Color getDefaultLeatherColor() {
        return Color.AQUA;
    }
}
