/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.lanatus.api.account;

import li.l1t.common.lanatus.api.LanatusRepository;
import li.l1t.common.lanatus.api.exception.AccountConflictException;

import java.util.UUID;

/**
 * A repository for Lanatus account information, providing both mutable and immutable objects.
 * <p>Mutable objects are regenerated for every call and may be changed at wish. Changes may be
 * written back to the database using the {@link #save(MutableAccount)} method. Note that mutable
 * accounts cannot be updated from the database since they might have been modified concurrently. A
 * fresh object must be obtained from this repository if an update is desired.</p> <p>Immutable
 * objects may not be changed and are not guaranteed to be regenerated at every call. However, their
 * data may change due to concurrent modifications by this client or another client. If an update is
 * required, it may be forced using {@link #refresh(AccountSnapshot)}.</p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public interface AccountRepository extends LanatusRepository {

    /**
     * Gets the current state of an account for mutation. If the account does not yet exist, it is
     * created. <p><b>Note:</b> Try to keep the scope for your mutable accounts as small as
     * possible. See the {@link MutableAccount} class JavaDoc for details.</p>
     *
     * @param playerId the unique id of the player whose account information to retrieve
     * @return the mutable account state
     */
    MutableAccount findMutable(UUID playerId);

    /**
     * Attempts to merge the state of given mutable account with its current state in the database.
     *
     * @param localCopy the local copy of the account to merge
     * @throws AccountConflictException if it is not possible to safely merge the changes into the
     *                                  database because it was concurrently modified
     */
    void save(MutableAccount localCopy) throws AccountConflictException;

    /**
     * Gets the a snapshot of an account for read-only purposes.
     *
     * @param playerId the unique id of the player whose account information to snapshot
     * @return an immutable snapshot of that player's account, or a snapshot of the defaults if
     * there is no account for given player
     */
    AccountSnapshot find(UUID playerId);

    /**
     * Refreshes the state of an immutable account from the database and returns a new immutable
     * account with the latest state.
     *
     * @param account the account to refresh
     * @return a new account that represents the same player but with more recent data, or a
     * snapshot of the defaults if there is no account for given player
     */
    AccountSnapshot refresh(AccountSnapshot account);
}
