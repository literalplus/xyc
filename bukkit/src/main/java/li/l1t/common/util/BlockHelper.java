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
