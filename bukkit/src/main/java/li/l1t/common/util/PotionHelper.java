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

import com.google.common.base.Preconditions;
import org.bukkit.Color;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides some utility methods for working with potions.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-12-16
 */
public class PotionHelper {
    private PotionHelper() {

    }

    private static final Pattern EFFECT_SPEC_PATTERN = Pattern.compile("([a-zA-Z_]+)(:[0-9]|)(\\*[0-9]+|)");

    /**
     * Creates a potion effect from a string specifying type, duration, and amplifier. The effects are matched using
     * {@link PotionEffectType#getByName(String)}. <p> Specs look like this: {@code EFFECT:AMPLIFIER*DURATION} </p> <p>
     * Only EFFECT is required, all other parameters may be omitted. Therefore, the following strings are all valid:</p>
     * <table summary="examples of valid specifiers">
     * <thead>
     * <tr>
     * <td>valid spec</td>
     * <td>resulting effect</td>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>{@code SPEED}</td>
     * <td>Speed I for Integer.MAX_VALUE ticks</td>
     * </tr>
     * <tr>
     * <td>{@code SPEED:1}</td>
     * <td>Speed II for Integer.MAX_VALUE ticks</td>
     * </tr>
     * <tr>
     * <td>{@code SPEED:1*6000}</td>
     * <td>Speed II for 6000 ticks (=5 minutes)</td>
     * </tr>
     * <tr>
     * <td>{@code SLOW_DIGGING*6000}</td>
     * <td>Mining Fatigue I for 6000 ticks (=5 minutes)</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param spec the specifier string for the potion effect to create
     * @return the potion effect parsed from given spec
     * @throws IllegalArgumentException if given spec does not match the format outlined above or the potion type does
     *                                  not exist
     */
    public static PotionEffect effectFromString(String spec) {
        Matcher matcher = EFFECT_SPEC_PATTERN.matcher(spec);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Potion spec does not match expected patttern: " + spec);
        }
        String effectTypeName = matcher.group(1);
        PotionEffectType effectType = PotionEffectType.getByName(effectTypeName);
        Preconditions.checkArgument(effectType != null, "unknown effect type : %s", effectTypeName, spec);
        int amplifier = findIntIgnoringFirstCharacter(matcher.group(2)).orElse(0);
        int duration = findIntIgnoringFirstCharacter(matcher.group(3)).orElse(Integer.MAX_VALUE);
        return createEffect(effectType, amplifier, duration);
    }

    private static PotionEffect createEffect(PotionEffectType effectType, int amplifier, int duration) {
        return new PotionEffect(effectType, duration, amplifier, true, false, Color.MAROON);
    }

    private static Optional<Integer> findIntIgnoringFirstCharacter(String input) {
        if (input.isEmpty()) {
            return Optional.empty();
        } else {
            return findInt(input.substring(1));
        }
    }

    private static Optional<Integer> findInt(String input) {
        try {
            return Optional.of(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Creates a string from a potion effect that may be passed back to {@link #effectFromString(String)} to create
     * given effect.
     *
     * @param effect the effect to serialise into a string
     * @return the string representing given effect
     */
    public static String stringFromEffect(PotionEffect effect) {
        return String.format("%s:%d*%d", effect.getType().getName(), effect.getAmplifier(), effect.getAmplifier());
    }

    /**
     * Creates a potion data from a string specifying type, duration, and amplifier. The effects are matched using
     * {@link PotionType#valueOf(String)}. <p> Specs look like this: {@code EFFECT.up.ext} </p> <p> Only EFFECT is
     * required, all other parameters may be omitted. The parameters specify whether the potion is upgraded in power and
     * has extended duration, respectively. Therefore, the following strings are all valid:</p>
     * <table summary="examples of valid specifiers">
     * <thead><tr>
     * <td>valid input</td>
     * <td>resulting potion data</td>
     * </tr></thead>
     * <tbody>
     * <tr>
     * <td>{@code SPEED}</td>
     * <td>Speed I with default duration</td>
     * </tr>
     * <tr>
     * <td>{@code SPEED.up}</td>
     * <td>Speed II with default duration</td>
     * </tr>
     * <tr>
     * <td>{@code SPEED.up.ext}</td>
     * <td>Speed II with extended duration</td>
     * </tr>
     * <tr>
     * <td>{@code SLOW_DIGGING.ext}</td>
     * <td>Mining Fatigue II with default duration</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param spec the specifier string for the potion data to create
     * @return the potion data parsed from given spec
     * @throws IllegalArgumentException if given spec does not match the format outlined above or the potion type does
     *                                  not exist
     */
    public static PotionData dataFromString(String spec) {
        Preconditions.checkNotNull(spec, "spec");
        String[] parts = spec.split("\\.");
        PotionType type = PotionType.valueOf(parts[0].toUpperCase().replace("[- ]", "_"));
        boolean extended = false;
        boolean upgraded = false;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.equalsIgnoreCase("up")) {
                upgraded = true;
            } else if (part.equalsIgnoreCase("ext")) {
                extended = true;
            } else {
                throw new IllegalArgumentException("unparseable data: " + part + "; " + spec);
            }
        }
        return new PotionData(type, extended, upgraded);
    }

    /**
     * Creates a string from a potion data that may be passed back to {@link #dataFromString(String)} (String)} to
     * create given data.
     *
     * @param data the data to serialise into a string
     * @return the string representing given data
     */
    public static String stringFromData(PotionData data) {
        StringBuilder sb = new StringBuilder(data.getType().name());
        appendIf(sb, data, PotionData::isUpgraded, ".up");
        appendIf(sb, data, PotionData::isExtended, ".ext");
        return sb.toString();
    }

    private static <T> void appendIf(StringBuilder sb, T param, Predicate<? super T> condition, String text) {
        if (condition.test(param)) {
            sb.append(text);
        }
    }
}
