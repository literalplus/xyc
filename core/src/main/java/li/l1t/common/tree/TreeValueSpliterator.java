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

package li.l1t.common.tree;

import java.util.Comparator;
import java.util.Spliterators;
import java.util.function.Consumer;

/**
 * Spliterates the values of a tree node. Internally uses a converter for consumers and
 * {@link TreeNodeSpliterator}. See that class's JavaDoc for important information.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-23
 */
@SuppressWarnings("WeakerAccess")
public class TreeValueSpliterator<N extends TreeNode<N, V>, V>
        extends Spliterators.AbstractSpliterator<V> {
    private final TreeNodeSpliterator<N, V> nodeSpliterator;

    public TreeValueSpliterator(TreeNodeSpliterator<N, V> nodeSpliterator) {
        super(Long.MAX_VALUE, 0);
        this.nodeSpliterator = nodeSpliterator;
    }

    @Override
    public boolean tryAdvance(Consumer<? super V> action) {
        return nodeSpliterator.tryAdvance(toNodeConsumer(action));
    }

    @Override
    public void forEachRemaining(Consumer<? super V> action) {
        nodeSpliterator.forEachRemaining(toNodeConsumer(action));
    }

    @Override
    public long getExactSizeIfKnown() {
        return nodeSpliterator.getExactSizeIfKnown();
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return nodeSpliterator.hasCharacteristics(characteristics);
    }

    @Override
    public Comparator<? super V> getComparator() {
        throw new IllegalStateException("not SORTED");
    }

    private Consumer<? super N> toNodeConsumer(Consumer<? super V> valueConsumer) {
        return node -> valueConsumer.accept(node.getValue());
    }
}
