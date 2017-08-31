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

package li.l1t.common.games.kits.objective;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Resolves objectives which provide access control for kits using strings as identifiers. The
 * objective string format is completely up to implementations.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-21
 */
public interface ObjectiveResolver {
    /**
     * Checks whether given player has completed an objective and can therefore access any kit
     * guarded by an objective. If the objective is not known to this resolver, access is denied.
     *
     * @param objective a string representing the objective
     * @param player    the player to check
     * @return whether given player may access kits guarded by given objective
     */
    boolean isCompleted(String objective, Player player);

    /**
     * Checks whether this resolver knows about an objective and can handle requests for it.
     *
     * @param objective a string representing the objective
     * @return whether this resolver can resolve given objective
     */
    boolean isKnown(String objective);

    /**
     * Calculates the human-readable name of an objective, or null if the objective is
     * unknown.
     *
     * @param objective a string representing the objective
     * @return the human-readable name of an objective
     */
    String getName(String objective);

    /**
     * Gets an item stack that is shown as kit icon if access is denied by given objective. If
     * the objective is unknown to this resolver, null is returned.
     *
     * @param objective a string representing the objective
     * @return an item stack shown as kit icon if access is denied
     */
    ItemStack getUnavailIcon(String objective);
}
