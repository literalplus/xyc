/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.lanatus.api.exception;

/**
 * Thrown if there is no row matching given criteria, but a row was required.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public class NoSuchRowException extends RuntimeException {
    public NoSuchRowException(String message) {
        super(message);
    }

    public NoSuchRowException(String message, Throwable cause) {
        super(message, cause);
    }
}
