/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.common.util;

import com.google.common.base.Preconditions;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;
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
     * Only EFFECT is required, all other parameters may be omitted. Therefore, the following strings are all valid:
     * <ul> <li>{@code SPEED} -> Speed I for Integer.MAX_VALUE ticks</li> <li>{@code SPEED:1} -> Speed II for
     * Integer.MAX_VALUE ticks</li> <li>{@code SPEED:1*6000} -> Speed II for 6000 ticks (=5 minutes)</li> <li>{@code
     * SLOW_DIGGING*6000} -> Mining Fatigue I for 6000 ticks (=5 minutes)</li> </ul> </p>
     *
     * @param spec the specifier string for the potion effect to create
     * @return the potion effect parsed from given spec
     * @throws IllegalArgumentException if given spec does not match the format outlined above
     */
    public static PotionEffect effectFromString(String spec) {
        Matcher matcher = EFFECT_SPEC_PATTERN.matcher(spec);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Potion spec does not match expected patttern: " + spec);
        }
        String effectTypeName = matcher.group(1);
        PotionEffectType effectType = PotionEffectType.getByName(effectTypeName);
        Preconditions.checkNotNull(effectType, "unknown effect type : %s", effectTypeName, spec);
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
}
