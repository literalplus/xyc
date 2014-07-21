package io.github.xxyy.common.collections;

import com.google.common.base.Objects;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

/**
 * An <b>immutable</b> implementation of a Tuple2 with left and right element of the same type.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 21.7.14
 */
public class Couple<V> {
    @Nullable
    private final V left;
    @Nullable
    private final V right;

    public Couple(@Nullable V left, @Nullable V right) {
        this.left = left;
        this.right = right;
    }

    @Nullable
    public V getLeft() {
        return left;
    }

    @Nullable
    public V getRight() {
        return right;
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
        if(left == null && right == null) {
            return null;
        }

        if(element == null) {
            return left == null ? right : left;
        }

        if(left == null || !left.equals(element)) {
            return left;
        }

        if(right == null || !right.equals(element)) {
            return right;
        }

        return null;
    }

    /**
     * Creates a Stream out of this Couple's elements.
     * Left will always be the first element.
     * @return A Stream of this Couple's elements.
     */
    public Stream<V> stream() {
        return Stream.of(left, right);
    }

    @Override
    @SuppressWarnings("RedundantIfStatement")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Couple couple = (Couple) o;

        if (left != null ? !left.equals(couple.left) : couple.left != null) return false;
        if (right != null ? !right.equals(couple.right) : couple.right != null) return false;

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
        return Objects.toStringHelper(this)
                .add("left", left)
                .add("right", right)
                .toString();
    }
}
