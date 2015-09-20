/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.lib.com.mojang.api.http;

import java.io.IOException;

/**
 * Signals that a request to a HTTP server has resulted in an unexpected response code.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 20/09/15
 */
public class InvalidResponseCodeException extends IOException {
    private final int responseCode;

    public InvalidResponseCodeException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    /**
     * @return the response code returned by the server
     */
    public int getResponseCode() {
        return responseCode;
    }
}
