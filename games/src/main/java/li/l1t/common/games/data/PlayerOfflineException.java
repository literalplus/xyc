/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.common.games.data;

/**
 * Thrown when a {@link org.bukkit.entity.Player} is offline when it really shouldn't be.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @deprecated Part of the deprecated PlayerWrapper API. See {@link PlayerWrapper} for details.
 */
@Deprecated
public class PlayerOfflineException extends RuntimeException {

    private static final long serialVersionUID = -4816273007842678096L;
}
