/*
 * -- placeholder --
 */

package li.l1t.common.lib.com.mojang.api.http;

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
