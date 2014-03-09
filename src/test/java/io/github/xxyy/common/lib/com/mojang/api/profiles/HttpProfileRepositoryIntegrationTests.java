package io.github.xxyy.common.lib.com.mojang.api.profiles;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(JUnit4.class)
public class HttpProfileRepositoryIntegrationTests {

    public static final String AGENT = "minecraft";

    @Test
    public void findProfilesByCriteria_existingNameProvided_returnsProfile() throws Exception {
        ProfileRepository repository = new HttpProfileRepository();
        ProfileCriteria criteria = new ProfileCriteria("mollstam", AGENT);

        Profile[] profiles = repository.findProfilesByCriteria(criteria);

        assertThat(profiles.length, is(1));
        assertThat(profiles[0].getName(), is(equalTo("mollstam")));
        assertThat(profiles[0].getId(), is(equalTo("f8cdb6839e9043eea81939f85d9c5d69")));
    }

    @Test
    public void findProfilesByCriteria_existingMultipleNamesProvided_returnsProfiles() throws Exception {
        ProfileRepository repository = new HttpProfileRepository();

        Profile[] profiles = repository.findProfilesByCriteria(
                new ProfileCriteria("mollstam", AGENT),
                new ProfileCriteria("jeb", AGENT)
        );

        assertThat(profiles.length, is(2));
        assertThat(profiles[0].getName(), is(equalTo("jeb")));
        assertThat(profiles[0].getId(), is(equalTo("f498513ce8c84773be26ecfc7ed5185d")));
        assertThat(profiles[1].getName(), is(equalTo("mollstam")));
        assertThat(profiles[1].getId(), is(equalTo("f8cdb6839e9043eea81939f85d9c5d69")));
    }


    @Test
    public void findProfilesByCriteria_nonExistingNameProvided_returnsEmptyArray() throws Exception {
        ProfileRepository repository = new HttpProfileRepository();
        ProfileCriteria criteria = new ProfileCriteria("doesnotexist$*not even legal", AGENT);

        Profile[] profiles = repository.findProfilesByCriteria(criteria);

        assertThat(profiles.length, is(0));
    }

}
