/*
 * -- placeholder --
 */

package li.l1t.common.lib.com.mojang.api.profiles;


import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import li.l1t.common.lib.com.mojang.api.http.BasicHttpClient;
import li.l1t.common.lib.com.mojang.api.http.HttpBody;
import li.l1t.common.lib.com.mojang.api.http.HttpClient;
import li.l1t.common.lib.com.mojang.api.http.HttpHeader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class HttpProfileRepository implements ProfileRepository {

    // You're not allowed to request more than 100 profiles per go.
    private static final int PROFILES_PER_REQUEST = 100;

    private static Gson gson = new Gson();
    private final String agent;
    private HttpClient client;

    public HttpProfileRepository(String agent) {
        this(agent, BasicHttpClient.getInstance());
    }

    public HttpProfileRepository(String agent, HttpClient client) {
        this.agent = agent;
        this.client = client;
    }

    private static HttpBody getHttpBody(String... namesBatch) {
        return new HttpBody(gson.toJson(namesBatch));
    }

    @Override
    public Profile[] findProfilesByNames(String... names) {
        List<Profile> profiles = new ArrayList<>();
        try {

            List<HttpHeader> headers = new ArrayList<>();
            headers.add(new HttpHeader("Content-Type", "application/json"));

            int namesCount = names.length;
            int start = 0;
            int i = 0;
            do {
                int end = PROFILES_PER_REQUEST * (i + 1);
                if (end > namesCount) {
                    end = namesCount;
                }
                String[] namesBatch = Arrays.copyOfRange(names, start, end);
                HttpBody body = getHttpBody(namesBatch);
                Profile[] result = post(getProfilesUrl(), body, headers);
                profiles.addAll(Arrays.asList(result));

                start = end;
                i++;
            } while (start < namesCount);
        } catch (Exception e) {
            //Mojang doesn't throw anything here
            throw new IllegalStateException("Failed to query Mojang", e);
        }

        return profiles.toArray(new Profile[profiles.size()]);
    }

    @Override
    public Profile findProfileAtTime(String name, long unixTime) {
        Profile profile;
        try {
            profile = getSingle(getProfileAtUrl(name, unixTime), Collections.emptyList());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to query Mojang", e);
        }

        return profile;
    }

    @Override
    public NameData[] findNameHistory(UUID uniqueId) {
        NameData[] names;
        try {
            names = getNameHistory(getNameHistoryUrl(uniqueId), Collections.emptyList());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to query Mojang", e);
        }

        return names;
    }

    private URL getProfileAtUrl(String name, long at) throws MalformedURLException {
        Preconditions.checkNotNull(name, "name");
        return new URL(String.format("https://api.mojang.com/users/profiles/%s/%s?at=%d", agent, name, at));
    }

    private URL getNameHistoryUrl(UUID uuid) throws MalformedURLException {
        Preconditions.checkNotNull(uuid, "uuid");
        return new URL(String.format("https://api.mojang.com/user/profiles/%s/names", uuid.toString().replace("-", "")));
    }

    private URL getProfilesUrl() throws MalformedURLException {
        // To lookup Minecraft profiles, agent should be "minecraft"
        return new URL("https://api.mojang.com/profiles/" + agent);
    }

    private Profile[] post(URL url, HttpBody body, List<HttpHeader> headers) throws IOException {
        String response = client.post(url, body, headers);

        if (response.contains("error")) {
            throw new IllegalStateException("Mojang responded with an error: " +
                    gson.fromJson(response, MojangError.class).getErrorMessage());
        }

        return gson.fromJson(response, MojangProfile[].class);
    }

    private Profile getSingle(URL url, List<HttpHeader> headers) throws IOException {
        String response = client.get(url, headers);

        if (response.contains("error")) {
            throw new IllegalStateException("Mojang responded with an error: " +
                    gson.fromJson(response, MojangError.class).getErrorMessage());
        }

        return gson.fromJson(response, MojangProfile.class);
    }

    private NameData[] getNameHistory(URL url, List<HttpHeader> headers) throws IOException {
        String response = client.get(url, headers);

        if (response.contains("error")) {
            throw new IllegalStateException("Mojang responded with an error: " +
                    gson.fromJson(response, MojangError.class).getErrorMessage());
        }

        return gson.fromJson(response, NameData[].class);
    }
}
