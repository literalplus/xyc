/*
 * -- placeholder --
 */

package li.l1t.common.lib.com.mojang.api.http;

public class HttpBody {

    public static final HttpBody EMPTY_BODY = new HttpBody(null);

    private String bodyString;

    public HttpBody(String bodyString) {
        this.bodyString = bodyString;
    }

    public byte[] getBytes() {
        return bodyString != null ? bodyString.getBytes() : new byte[0];
    }

    public boolean isEmpty() {
        return bodyString == null || bodyString.isEmpty();
    }
}
