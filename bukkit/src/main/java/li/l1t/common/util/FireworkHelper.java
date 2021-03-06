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

package li.l1t.common.util;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * Class that provides some static methods to deal
 * with {@link Firework}s.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class FireworkHelper {
    /**
     * Launches a random Firework at a random Location in
     * {@code radius} around {@code loc}.
     *
     * @param loc    the base location of where to launch a rocket
     * @param radius the radius in which the rocket will spawn from loc
     */
    public static void launchRandomRocketNear(Location loc, int radius) {
        loc = LocationHelper.randomiseLocation(loc, radius);
        Firework firework = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(RandomUtils.nextInt(5) + 1);
        meta.addEffect(
                FireworkEffect.builder()
                        .trail(RandomUtils.nextBoolean())
                        .with(MiscHelper.randomEnumElement(FireworkEffect.Type.class))
                        .withColor(FireworkHelper.randomColor(), FireworkHelper.randomColor())
                        .withFade(FireworkHelper.randomColor())
                        .flicker(RandomUtils.nextBoolean()).build()
        );
        firework.setFireworkMeta(meta);
    }

    /**
     * There seems to not be a better way of doing this if not Reflection (but that's intensive)
     * Anyway returns a random {@link Color}.
     */
    private static Color randomColor() {
        int i = RandomUtils.nextInt(17) + 1;
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
        }

        return c;
    }
}
