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

import com.google.common.base.Preconditions;
import li.l1t.common.exception.UserException;
import li.l1t.lanatus.api.account.LanatusAccount;

/**
 * Thrown if there are not enough melons in an account for a transaction, e.g. a purchase.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-10-07
 */
public class NotEnoughMelonsException extends UserException {
    private final LanatusAccount account;
    private final int melonsMissing;

    public NotEnoughMelonsException(LanatusAccount account, int melonsMissing) {
        super(String.format(
                "Das kannst du dir nicht leisten! (Du hast %d Melonen, dir fehlen %d)",
                Preconditions.checkNotNull(account, "account").getMelonsCount(),
                melonsMissing
        ));
        this.account = account;
        this.melonsMissing = melonsMissing;
    }

    /**
     * @return the amount of melons additionally required in the account to complete the transaction
     */
    public int getMelonsMissing() {
        return melonsMissing;
    }

    /**
     * @return the account involved in the transaction
     */
    public LanatusAccount getAccount() {
        return account;
    }
}
