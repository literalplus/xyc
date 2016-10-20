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

/**
 * Thrown if no such purchase exists, but a purchase was required for some action.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-17
 */
public class NoSuchPurchaseException extends NoSuchRowException {
    public NoSuchPurchaseException(String message) {
        super(message);
    }

    public NoSuchPurchaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
