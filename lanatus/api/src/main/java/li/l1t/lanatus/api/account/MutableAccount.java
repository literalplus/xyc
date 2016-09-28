/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.api.account;

/**
 * A mutable representation of an account. Mutable accounts should not be shared between scopes.
 * They must written back to the database as soon as possible to avoid conflicts.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public interface MutableAccount {
    /**
     * @return an immutable snapshot of the account as it was before this mutable account was
     * fetched
     */
    AccountSnapshot getInitialState();

    /**
     * @return the amount of melons owned by this account in its mutated state
     */
    int getMelonsCount();

    /**
     * Sets the amount of melons owned by this account in its mutated state.
     *
     * @param newMelonsCount the amount of melons to be owned by this account
     */
    void setMelonsCount(int newMelonsCount);

    /**
     * @return the latest known rank of this account in its mutated state
     */
    String getLastRank();

    /**
     * @param lastRank the latest known rank of this account in its mutated state
     */
    void setLastRank(String lastRank);

    /**
     * Adds an integer value to the amount of melons owned by this account in its mutated state.
     *
     * @param melonsModifier the amount of melons to add, may be negative
     */
    void modifyMelonsCount(int melonsModifier);
}
