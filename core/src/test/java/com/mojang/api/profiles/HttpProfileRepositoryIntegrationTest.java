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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.github.xxyy.common.lib.com.mojang.api.profiles.HttpProfileRepository;
import io.github.xxyy.common.lib.com.mojang.api.profiles.NameData;
import io.github.xxyy.common.lib.com.mojang.api.profiles.Profile;
import io.github.xxyy.common.lib.com.mojang.api.profiles.ProfileRepository;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(JUnit4.class)
public class HttpProfileRepositoryIntegrationTest {

    @Test
    public void findProfilesByNames_existingNameProvided_returnsProfile() throws Exception {
        ProfileRepository repository = new HttpProfileRepository("minecraft");

        Profile[] profiles = repository.findProfilesByNames("mollstam");

        assertThat(profiles.length, is(1));
        assertThat(profiles[0].getName(), is(equalTo("mollstam")));
        assertThat(profiles[0].getId(), is(equalTo("f8cdb6839e9043eea81939f85d9c5d69")));
    }

    @Test
    public void findProfilesByNames_existingMultipleNamesProvided_returnsProfiles() throws Exception {
        ProfileRepository repository = new HttpProfileRepository("minecraft");

        Profile[] profiles = repository.findProfilesByNames("mollstam", "KrisJelbring");

        assertThat(profiles.length, is(2));
        assertThat(profiles[0].getName(), is(equalTo("KrisJelbring")));
        assertThat(profiles[0].getId(), is(equalTo("7125ba8b1c864508b92bb5c042ccfe2b")));
        assertThat(profiles[1].getName(), is(equalTo("mollstam")));
        assertThat(profiles[1].getId(), is(equalTo("f8cdb6839e9043eea81939f85d9c5d69")));
    }

    @Test
    public void findProfilesByNames_nonExistingNameProvided_returnsEmptyArray() throws Exception {
        ProfileRepository repository = new HttpProfileRepository("minecraft");

        Profile[] profiles = repository.findProfilesByNames("doesnotexist$*not even legal");

        assertThat(profiles.length, is(0));
    }

    @Test
    public void findProfileAtTime_unchangedNameProvided_returnsNull() throws Exception {
        ProfileRepository repository = new HttpProfileRepository("minecraft");

        Profile profile = repository.findProfileAtTime("mollstam", 0);

        assertThat(profile, is(nullValue()));
    }

    @Test
    public void findProfileAtTime_changedNameProvided_returnsUniqueId() throws Exception {
        ProfileRepository repository = new HttpProfileRepository("minecraft");

        Profile profile = repository.findProfileAtTime("xxyy98", 0);

        assertThat(profile, is(not(nullValue())));
        assertThat(profile.getId(), is("a9503380ff104e71b717ff05d129da13"));
    }

    @Test
    public void findNameHistory_unchangedNameProvided_returnsUniqueId() throws Exception {
        ProfileRepository repository = new HttpProfileRepository("minecraft");

        NameData[] names = repository.findNameHistory(UUID.fromString("a9503380-ff10-4e71-b717-ff05d129da13")); //Literallie

        assertThat(names.length, is(greaterThan(1))); //known to have changed name at least once
        assertThat(names[0].getChangedToAt(), is(equalTo(0L))); //first name shouldn't have time
        assertThat(names[0].getName(), is(equalTo("xxyy98")));
    }
}
