/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.collections;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * An immutable implementation of a Tuple2 with left and right element of the same type.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2014-07-21
 */
public class Couple<V> extends Pair<V, V> {

    /**
     * Creates a new couple instance.
     *
     * @param left  the left element
     * @param right the right element
     * @deprecated was not intended to be exposed - use {@link #of(Object, Object)}
     */
    @Deprecated
    @SuppressWarnings("deprecation")
    public Couple(@Nullable V left, @Nullable V right) {
        super(left, right);
    }

    /**
     * Creates a new couple with given elements.
     *
     * @param left  the left element
     * @param right the right element
     * @param <V>   the couple's value type
     * @return the created couple
     */
    public static <V> Couple<V> of(@Nullable V left, @Nullable V right) {
        return new Couple<>(left, right);
    }

    /**
     * This method gets an element from this Couple different from the argument. If both elements
     * are different from the argument, NULL is returned. If both elements match the argument, left
     * is returned first.
     *
     * @param element the element to compare against the ones in this Couple
     * @return an element different from the argument or NULL if there are none.
     */
    @Nullable
    public V getOther(@Nullable V element) {
        if (left == null && right == null) {
            return null;
        }

        if (element == null) {
            return left == null ? right : left;
        }

        if (left == null || !left.equals(element)) {
            return left;
        }

        if (right == null || !right.equals(element)) {
            return right;
        }

        return null;
    }

    /**
     * Creates a stream out of this couple's elements from left to right.
     *
     * @return a stream of this couple's elements.
     */
    public Stream<V> stream() {
        return Stream.of(left, right);
    }

    /**
     * Applies a Consumer to this couple's elements.
     *
     * @param consumer the consumer to apply
     */
    public void forEach(Consumer<V> consumer) {
        consumer.accept(left);
        consumer.accept(right);
    }

    /**
     * Returns a new couple with this couple's right element and a new left element.
     *
     * @param newLeft the new left element
     * @return a new couple
     */
    public Couple<V> withLeft(@Nullable V newLeft) {
        return of(newLeft, this.right);
    }

    /**
     * Returns a new couple with this couple's left element and a new right element.
     *
     * @param newRight the new right element
     * @return a new couple
     */
    public Couple<V> withRight(@Nullable V newRight) {
        return of(this.left, newRight);
    }
}
