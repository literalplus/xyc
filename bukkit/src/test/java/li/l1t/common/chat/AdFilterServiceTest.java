/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.chat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AdFilterServiceTest {
    private static final List<String> advertisements = Arrays.asList(
            "Beschde server: yolo.me",
            "https://some-web.site/",
            "Shaut mal: https://www.youtube.com/watch?v=ss2T630Kx5M",
            "https://youtu.be/ss2T630Kx5M",
            "!!! JOINED ALLE AUF MINETIME.ME !!!",
            "alle auf meinen server: noobs.minecraft.to!",
            "ALLE AUF noobs.minecraft.to, nicht https://bugs.nowak-at.net/!!!",
            "WICHTIG! minotopia.me ist kacke! joint alle minecrime.me!",
            "127.0.0.1",
            "ALLE AUF 192.168.1.1",
            "Alle bhester Stream!!!! http://hitbox.tv/letsklay",
            "Joint auf skypvpler.de!!! Es war sehr viel Arbeit und nun ist er da!! 40User Event!!!Rang Giveaway!!! 40User Event!!!Rang Giveaway!!",
            "Zocken_mit_Skill DISER sky-g.de IST ein drecks server die killn ein und machen die ganse zeit /kill"
    );

    private static final List<String> allowedDomainAdvertisements = Arrays.asList(
            "https://bugs.nowak-at.net/view.php?id=303",
            "https://xxyy.nowak-at.net/",
            "j.nowak-at.net",
            "heyyyy ummm le https://meme.nowak-at.net/facepalm.jpg",
            "CHECK THIS MeMe.NoWaK-aT.nEt/404.html"
    );

    private static List<String> hiddenDotAdvertisements = Arrays.asList(
            "alle auf www (punkt) minotopia (punkt) me !!!",
            "bester server bei play (.) minotopia (punkt) me",
            "yolo (point) yolo",
            "www . minotopia . me",
            "join le minotopia..de",
            "geht alle auf play....minotopia (punkt) me"
    );

    private static List<String> ordinaryText = Arrays.asList(
            "hey, this is some ordinary text. Another sentence!",
            "Yes. At this",
            "Cause he's business cat (meow)",
            "Strictly business (meow)",
            "Doing all the business (meow!) In his litter tray... Business Cat is back from the conference"
    );

    private AdFilterService filterService;

    @Before
    public void setUp() throws Exception {
        filterService = new AdFilterService();
    }

    @Test
    public void testTest() throws Exception {
        assertAllMatch(advertisements, "test/advertisements");
        assertAllMatch(allowedDomainAdvertisements, "test/allowedDomainAdvertisements");
        assertAllMatch(hiddenDotAdvertisements, "test/hiddenDotAdvertisements");
        assertNoMatch(ordinaryText, "test/ordinaryText");
    }

    @Test
    public void testSetFindHiddenDots() throws Exception {
        filterService.setFindHiddenDots(false);

        assertAllMatch(advertisements, "findHiddenDots/advertisements");
        assertAllMatch(allowedDomainAdvertisements, "findHiddenDots/allowedDomainAdvertisements");
        assertNoMatch(hiddenDotAdvertisements, "findHiddenDots/hiddenDotAdvertisements");
        assertNoMatch(ordinaryText, "findHiddenDots/ordinaryText");
    }

    @Test
    public void testAddIgnoredDomains() throws Exception {
        filterService.addIgnoredDomains("nowak-at.net");

        assertAllMatch(advertisements, "ignoredDomains/advertisements");
        assertNoMatch(allowedDomainAdvertisements, "ignoredDomains/allowedDomainAdvertisements");
        assertAllMatch(hiddenDotAdvertisements, "ignoredDomains/hiddenDotAdvertisements");
        assertNoMatch(ordinaryText, "ignoredDomains/ordinaryText");
    }

    @Test
    public void testFindIpAddresses() throws Exception {
        filterService.setFindIpAddresses(false);

        //#advertisements contains ip addresses
        assertAllMatch(allowedDomainAdvertisements, "ips/allowedDomainAdvertisements");
        assertAllMatch(hiddenDotAdvertisements, "ips/hiddenDotAdvertisements");
        assertNoMatch(ordinaryText, "ips/ordinaryText");
        assertNoMatch(Arrays.asList("192.168.1.1", "127.0.0.1", "alle auf 192.168.1.37"), "ips/ips");
    }

    private void assertAllMatch(List<String> testStrings, String comment) {
        testStrings.forEach((msg) -> Assert.assertTrue(
                String.format("Ad not recognised: '%s' (%s)", msg, comment), filterService.test(msg)
        ));
    }

    private void assertNoMatch(List<String> testStrings, String comment) {
        testStrings.forEach((msg) -> Assert.assertFalse(
                String.format("Ad wrongly recognised: '%s' (%s)", msg, comment), filterService.test(msg)
        ));
    }
}
