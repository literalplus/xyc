/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.util;

import org.apache.commons.lang.Validate;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;

/**
 * provides static utility methods for dealing with blocks.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 05/01/15
 */
public final class BlockHelper {
    private BlockHelper() {

    }

    /**
     * Updates a block's wool color.
     * @param block the wool block to update
     * @param color the color to set
     * @return the input block
     * @throws java.lang.IllegalArgumentException if block is not wool
     */
    public static Block updateWoolColor(Block block, DyeColor color) {
        MaterialData data = block.getState().getData();
        Validate.isTrue(data instanceof Wool, "Can only set wool color");
        //noinspection ConstantConditions
        Wool wool = (Wool) data;
        wool.setColor(color);
        block.getState().setData(wool);
        block.getState().update(true);
        return block;
    }
}
