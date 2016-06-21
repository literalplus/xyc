/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.games.kits.objective;

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
