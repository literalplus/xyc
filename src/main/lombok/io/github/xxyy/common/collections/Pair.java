package io.github.xxyy.common.collections;

import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

/**
 * A class storing an <b>immutable</b> pair of two objects relatd to each other.
 * Useful fpr storage in Collections.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 22.7.14
 */
public class Pair<L, R> {
    @Nullable
    protected final L left;
    @Nullable
    protected final R right;

    public Pair(@Nullable L left, @Nullable R right) {
        this.left = left;
        this.right = right;
    }

    @Nullable
    public L getLeft() {
        return left;
    }

    @Nullable
    public R getRight() {
        return right;
    }

    /**
     * Creates a Stream out of this Couple's elements.
     * Left will always be the first element.
     *
     * @return A Stream of this Couple's elements.
     */
    public Stream<?> stream() {
        return Stream.of(left, right);
    }

    /**
     * Checks whether this pair contains the parameter.
     * @param obj object to check for
     * @return whether any of this pair's contents is the parameter
     */
    public boolean contains(@NotNull Object obj) {
        return obj.equals(getLeft()) && obj.equals(getRight());
    }

    @Override
    @SuppressWarnings("RedundantIfStatement")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Pair pair = (Couple) o;

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
        return Objects.toStringHelper(this)
                .add("left", left)
                .add("right", right)
                .toString();
    }
}
