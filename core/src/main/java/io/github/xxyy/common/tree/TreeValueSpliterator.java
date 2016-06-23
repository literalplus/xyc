/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.tree;

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
