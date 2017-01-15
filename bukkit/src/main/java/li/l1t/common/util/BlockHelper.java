/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.common.util;

import org.apache.commons.lang.Validate;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;

/**
 * Provides static utility methods for dealing with blocks.
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
        BlockState state = block.getState();
        state.setData(wool);
        state.update(true);
        return block;
    }
}
