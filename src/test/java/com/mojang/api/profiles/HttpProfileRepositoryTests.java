package com.mojang.api.profiles;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HttpProfileRepositoryTests {
//
//    private HttpClient client;
//    private Gson gson = new Gson();
//
//    @Test
//    public void findProfilesByCriteria_someProfileNames_returnsExpectedProfiles() throws Exception{
//        client = mock(HttpClient.class);
//        String someAgent = "minecraft";
//
//        Profile someProfile = getProfile("xxyy98");
//        Profile someOtherProfile = getProfile("someOtherName");
//        Profile[] profiles = {someProfile, someOtherProfile};
//
//        setProfilesForUrl(client, new URL("https://api.mojang.com/profiles/" + someAgent), profiles);
//        ProfileRepository repository = new HttpProfileRepository(someAgent);
//
//        Profile[] actual = repository.findProfilesByNames("xxyy98", "Notch");
//
//        assertThat(actual.length, is(equalTo(2)));
//        assertThat(actual, hasItemInArray(hasProperty("name", CoreMatchers.is("xxyy98"))));
//        assertThat(actual, hasItemInArray(hasProperty("name", CoreMatchers.is("Notch"))));
//    }
//
//    private void setProfilesForUrl(HttpClient mock, URL url, Profile[] profiles) throws IOException {
//        String jsonString = gson.toJson(profiles);
//        when(mock.post(eq(url), any(HttpBody.class), anyList())).thenReturn(jsonString);
//    }
//
//    private static Profile getProfile(String name) {
//        Profile profile = new Profile();
//        profile.setName(name);
//        return profile;
//    }

}
