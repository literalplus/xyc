/*
 * -- placeholder --
 */

package li.l1t.common.lib.com.mojang.api.http;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.List;

/*
    TODO: refactor so unit tests can be written :)
 */
public class BasicHttpClient implements HttpClient {

    private static BasicHttpClient instance;

    private BasicHttpClient() {
    }

    public static BasicHttpClient getInstance() {
        if (instance == null) {
            instance = new BasicHttpClient();
        }
        return instance;
    }

    @Override
    public String post(URL url, HttpBody body, List<HttpHeader> headers) throws IOException {
        return post(url, null, body, headers);
    }

    @Override
    public String post(URL url, Proxy proxy, HttpBody body, List<HttpHeader> headers) throws IOException {
        return request(url, "POST", proxy, body, headers);
    }

    @Override
    public String get(URL url, List<HttpHeader> headers) throws IOException {
        return get(url, null, headers);
    }

    @Override
    public String get(URL url, Proxy proxy, List<HttpHeader> headers) throws IOException {
        return request(url, "GET", proxy, HttpBody.EMPTY_BODY, headers);
    }

    @Nonnull
    private String request(URL url, String method, Proxy proxy, HttpBody body, List<HttpHeader> headers) throws IOException {
        if (proxy == null) proxy = Proxy.NO_PROXY;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
        connection.setRequestMethod(method);


        for (HttpHeader header : headers) {
            connection.setRequestProperty(header.getName(), header.getValue());
        }

        connection.setUseCaches(false);
        connection.setDoInput(true);

        connection.setDoOutput(!body.isEmpty());
        if (!body.isEmpty()) {
            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
            writer.write(body.getBytes());
            writer.flush();
            writer.close();
        }

        if (connection.getResponseCode() < HttpURLConnection.HTTP_OK || // error reading response results in -1
                connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
            throw new InvalidResponseCodeException(String.format("Server returned HTTP response code: %d for URL: %s",
                    connection.getResponseCode(), url), connection.getResponseCode());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }

        reader.close();
        return response.toString();
    }
}
