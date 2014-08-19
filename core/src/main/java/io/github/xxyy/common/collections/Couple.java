/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.collections;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * An <b>immutable</b> implementation of a Tuple2 with left and right element of the same type.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 21.7.14
 */
public class Couple<V> extends Pair<V, V> {

    public Couple(@Nullable V left, @Nullable V right) {
        super(left, right);
    }

    /**
     * This method gets an element from this Couple different from the argument.
     * If both elements are different from the argument, NULL is returned.
     * If both elements match the argument, left is returned first.
     *
     * @param element The element to compare against the ones in this Couple
     * @return An element different from the argument or NULL if there are none.
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
     * Creates a Stream out of this Couple's elements.
     * Left will always be the first element.
     *
     * @return A Stream of this Couple's elements.
     */
    public Stream<V> stream() {
        return Stream.of(left, right);
    }

    /**
     * Applies a Consumer to this Couple's elements.
     *
     * @param consumer Consumer to use
     * @see java.util.stream.Stream#forEach(java.util.function.Consumer)
     */
    public void forEach(Consumer<V> consumer) {
        consumer.accept(left);
        consumer.accept(right);
    }
}
