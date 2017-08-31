/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.mojang.api.profiles;

import com.google.gson.Gson;
import li.l1t.common.lib.com.mojang.api.http.HttpBody;
import li.l1t.common.lib.com.mojang.api.http.HttpClient;
import li.l1t.common.lib.com.mojang.api.http.HttpHeader;
import li.l1t.common.lib.com.mojang.api.profiles.HttpProfileRepository;
import li.l1t.common.lib.com.mojang.api.profiles.MojangProfile;
import li.l1t.common.lib.com.mojang.api.profiles.Profile;
import li.l1t.common.lib.com.mojang.api.profiles.ProfileRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpProfileRepositoryTest {

    private HttpClient client;
    private Gson gson = new Gson();

    private static Profile getProfile(String name) {
        MojangProfile profile = new MojangProfile();
        profile.setName(name);
        return profile;
    }

    @Test
    public void findProfilesByNames_someProfileNames_returnsExpectedProfiles() throws Exception {
        client = mock(HttpClient.class);
        String someAgent = "someAgent";

        Profile someProfile = getProfile("someName");
        Profile someOtherProfile = getProfile("someOtherName");
        Profile[] profiles = {someProfile, someOtherProfile};

        setProfilesForUrl(client, new URL("https://api.mojang.com/profiles/" + someAgent), profiles);
        ProfileRepository repository = new HttpProfileRepository(someAgent, client);

        Profile[] actual = repository.findProfilesByNames("someName", "someOtherName");

        assertThat(actual.length, is(equalTo(2)));
        assertThat(actual, hasItemInArray(hasProperty("name", CoreMatchers.is("someName"))));
        assertThat(actual, hasItemInArray(hasProperty("name", CoreMatchers.is("someOtherName"))));
    }

    private void setProfilesForUrl(HttpClient mock, URL url, Profile[] profiles) throws IOException {
        String jsonString = gson.toJson(profiles);
        when(mock.post(eq(url), any(HttpBody.class), anyListOf(HttpHeader.class))).thenReturn(jsonString);
    }

}
