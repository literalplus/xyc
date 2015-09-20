/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package com.mojang.api.profiles;

import com.google.gson.Gson;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import io.github.xxyy.common.lib.com.mojang.api.http.HttpBody;
import io.github.xxyy.common.lib.com.mojang.api.http.HttpClient;
import io.github.xxyy.common.lib.com.mojang.api.http.HttpHeader;
import io.github.xxyy.common.lib.com.mojang.api.profiles.HttpProfileRepository;
import io.github.xxyy.common.lib.com.mojang.api.profiles.MojangProfile;
import io.github.xxyy.common.lib.com.mojang.api.profiles.Profile;
import io.github.xxyy.common.lib.com.mojang.api.profiles.ProfileRepository;

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
