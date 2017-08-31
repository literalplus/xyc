/*
 * -- placeholder --
 */

package li.l1t.common.lib.com.mojang.api.http;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;

public interface HttpClient {
    String post(URL url, HttpBody body, List<HttpHeader> headers) throws IOException;

    String post(URL url, Proxy proxy, HttpBody body, List<HttpHeader> headers) throws IOException;

    String get(URL url, List<HttpHeader> headers) throws IOException;

    String get(URL url, Proxy proxy, List<HttpHeader> headers) throws IOException;
}
