/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.api.builder.melons;

import li.l1t.common.exception.DatabaseException;

/**
 * Fluent builder interface for crediting melons to a player's account.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-18
 */
public interface CreditMelonsBuilder {
    /**
     * @return whether the melons were credited already
     */
    boolean hasBeenExecuted();

    /**
     * Actually credits the amount of melons defined in this builder to the player. Note that this
     * method can only be executed once per builder instance.
     *
     * @throws IllegalStateException {@linkplain #hasBeenExecuted()if the melons have already been
     *                               credited}
     * @throws DatabaseException     if a database error occurs
     */
    void build() throws IllegalStateException, DatabaseException;

    /**
     * Sets the melons count to be credited to the player.
     *
     * @param melonsCount the amount of melons to credit
     * @return this builder
     * @throws IllegalArgumentException if melonsCount is negative
     */
    CreditMelonsBuilder withMelonsCount(int melonsCount);
}
