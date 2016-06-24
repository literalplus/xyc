/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.util;

import java.util.function.Predicate;

/**
 * Provides some static utility methods for working with {@link java.util.function.Predicate}s.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 18/06/15
 */
public class PredicateHelper {
    private PredicateHelper() {

    }

    /**
     * <p>Negates a {@link Predicate} using {@link Predicate#negate()}.</p>
     * <p>This is intended for use with method references, like this:
     * <code>s.filter(not(someList::contains));</code></p>
     *
     * @param t   the predicate to negate
     * @param <T> the argument type of the predicate
     * @return a negated version of given predicate
     */
    public static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }
}
