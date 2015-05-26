package io.github.xxyy.common.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * currently only testing advertisment detection
 */
public class ChatHelperTest {

    private final List<String> adsAlways = Arrays.asList("minotopia.me ist kacke! joint alle minecrime.me",
            "WICHTIG! minotopia.me ist kacke! joint alle minecrime.me!",
            "123.2.12.5",
            "hitbox.tv/letsgay",
            "http://goo.gl/senb645e",
            "blöd: minotopia.me. gommehd.net. ist vieeeel besser und größer!",
            // following from http://pastebin.com/dnZzu9aw
            "Joint auf skypvpler.de!!! Es war sehr viel Arbeit und nun ist er da!! 40User Event!!!Rang Giveaway!!! 40User Event!!!Rang Giveaway!!",
            //".Joint auf skypvpler,de. Es war sehr viel Arbeit und nun ist er da. 40User Event.Rang Giveaway❢", //both not blocked if pointLikeChars is off
            //"&fJoint auf skypvpler,de.",
            "http://www.youtube.com/watch?v=jzSu5yR2OfE",
            "http://i.imgur.com/jCzOeLK.png",
            "https://www.youtube.com/watch?v=2JXhGz79Yu4&list=UU9sY9S-ddN-1E0jD2fFWLig",
            "104.28.8.125",
            "dasistkeinedomaindieexistiert.de",
            "dasistkeinedomaindieexistiert.de minotopia.me",
            "ipadresse.de gibt es nicht haha :D",
            "Zocken_mit_Skill DISER sky-g.de IST ein drecks server die killn ein und machen die ganse zeit /kill",
            "lol Giu bei minecraft.net hast du keinen skin",
            "lol Giu bei minecraft(.)net hast du keinen skin",
            "# http://schoener-fernsehen.com/live/stream/de/SRTL/SUPER%20RTL/",
            "kommt alle zu sgbggp.com");

    private final List<String> noAdsAlways = Arrays.asList("Schaut mal auf minotopia.me/forum vorbei!",
            "Dieser Bug ist bereits im Tracker, schau mal https://bugs.nowak-at.net/view.php?id=412",
            "Dieser Server wird von hosting.living-bots.net gesponsort",
            "www.minotopia.me");

    @Test
    public void testAdvertismentDetection() throws Exception {
        test(1, false, adsAlways, noAdsAlways);
        test(1, true, adsAlways, noAdsAlways);
        test(2, false, adsAlways, noAdsAlways);
        test(2, true, adsAlways, noAdsAlways);
    }

    private void test(int level, boolean pointLikeChars, List<String> ads, List<String> noAds) {
        ChatHelper.adDetectionLevel = (short) level;
        ChatHelper.adDetectionReplacePointLikeChars = pointLikeChars;

        ads.forEach(msg -> testAd(msg, true));

        noAds.forEach(msg -> testAd(msg, false));
    }

    private void testAd(String msg, boolean isAd) {
        if (isAd) {
            assertTrue("not recognized ad in '" + msg + "' (detection level: " + ChatHelper.adDetectionLevel +
                            ", replacePointLikeChars: " + ChatHelper.adDetectionReplacePointLikeChars + ")",
                    ChatHelper.isAdvertisement(msg));
        } else {
            assertFalse("false positive ad in '" + msg + "' (detection level: " + ChatHelper.adDetectionLevel +
                            ", replacePointLikeChars: " + ChatHelper.adDetectionReplacePointLikeChars + ")",
                    ChatHelper.isAdvertisement(msg));
        }
    }

    private static <T> List<T> combineLists(List<T> one, List<T> two) {
        final List<T> list = new ArrayList<>(one.size() + two.size());
        list.addAll(one);
        list.addAll(two);
        return list;
    }

}
