/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.api.exception;

import li.l1t.lanatus.api.account.AccountSnapshot;
import li.l1t.lanatus.api.account.MutableAccount;

/**
 * Thrown if a conflict arises while attempting to merge a local copy of an account back into the
 * database.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public class AccountConflictException extends Exception {
    private final AccountSnapshot databaseState;
    private final MutableAccount localState;

    public AccountConflictException(AccountSnapshot databaseState, MutableAccount localState) {
        this.databaseState = databaseState;
        this.localState = localState;
    }

    public AccountSnapshot getDatabaseState() {
        return databaseState;
    }

    public MutableAccount getLocalState() {
        return localState;
    }
}
