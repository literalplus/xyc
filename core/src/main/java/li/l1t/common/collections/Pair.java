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

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Represents an immutable pair of two objects related to each other.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2014-07-22
 */
public class Pair<L, R> {
    @Nullable
    protected final L left;
    @Nullable
    protected final R right;

    /**
     * Creates a new pair.
     *
     * @param left  the left element
     * @param right the right element
     * @deprecated was not intended to be exposed - use {@link #pairOf(Object, Object)}
     */
    @Deprecated
    public Pair(@Nullable L left, @Nullable R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Creates a new pair with given elements.
     *
     * @param left  the left element
     * @param right the right element
     * @param <L>   the left type
     * @param <R>   the right type
     * @return a new pair
     */
    public static <L, R> Pair<L, R> pairOf(L left, R right) {
        return new Pair<>(left, right);
    }

    /**
     * @return the left element of this pair
     */
    @Nullable
    public L getLeft() {
        return left;
    }

    /**
     * @return the right element of this pair
     */
    @Nullable
    public R getRight() {
        return right;
    }

    /**
     * Creates a Stream out of this Couple's elements. Left will always be the first element.
     *
     * @return A Stream of this Couple's elements.
     */
    public Stream<?> stream() {
        return Stream.of(left, right);
    }

    /**
     * Checks whether this pair contains the parameter.
     *
     * @param obj object to check for
     * @return whether any of this pair's contents is the parameter
     */
    public boolean contains(@Nonnull Object obj) {
        return obj.equals(getLeft()) && obj.equals(getRight());
    }

    /**
     * Returns a new couple with this couple's right element and a new left element.
     *
     * @param newLeft the new left element
     * @return a new couple
     */
    public Pair<L, R> withLeft(@Nullable L newLeft) {
        return pairOf(newLeft, this.right);
    }

    /**
     * Returns a new couple with this couple's left element and a new right element.
     *
     * @param newRight the new right element
     * @return a new couple
     */
    public Pair<L, R> withRight(@Nullable R newRight) {
        return pairOf(this.left, newRight);
    }

    @Override
    @SuppressWarnings("RedundantIfStatement")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Pair pair = (Pair) o;

        if (left != null ? !left.equals(pair.left) : pair.left != null) return false;
        if (right != null ? !right.equals(pair.right) : pair.right != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (left != null ? left.hashCode() : 0);
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName() + " L='" + left + "', R='" + right + "'}";
    }
}
