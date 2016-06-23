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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Simple implementation of a tree node.
 *
 * @param <N> the node type of the tree this node is part of
 * @param <V> the value type of the tree this node is part of
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-22
 */
@SuppressWarnings("WeakerAccess")
public class SimpleTreeNode<N extends TreeNode<N, V>, V> implements TreeNode<N, V> {

    private final N parent;
    private final List<N> children = new ArrayList<>(4); //and even that is an exaggeration
    private V value;

    public SimpleTreeNode(N parent) {
        this.parent = parent;
    }

    @Override
    public N getParent() {
        return parent;
    }

    @Override
    public List<N> getChildren() {
        return children;
    }

    @Override
    public void addChild(N newChild) {
        children.add(newChild);
    }

    @Override
    public void removeChild(N oldChild) {
        children.remove(oldChild);
    }

    @Override
    public boolean isRoot() {
        return parent == null;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public boolean isBranching() {
        return children.size() > 1;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public void setValue(V newValue) {
        this.value = newValue;
    }

    @Override
    public boolean hasChild(int childId) {
        return childId < getChildren().size();
    }

    /**
     * <p><b>Attention:</b> Read {@link TreeNodeSpliterator the node spliterator's JavaDoc} for
     * important notes about its behaviour.</p>
     * {@inheritDoc}
     */
    @Override
    public Iterator<V> iterator() {
        return Spliterators.iterator(spliterator());
    }

    /**
     * <p><b>Attention:</b> Read {@link TreeNodeSpliterator the node spliterator's JavaDoc} for
     * important notes about its behaviour.</p>
     * {@inheritDoc}
     */
    @Override
    public void forEach(Consumer<? super V> action) {
        spliterator().forEachRemaining(action);
    }

    /**
     * <p><b>Attention:</b> Read {@link TreeNodeSpliterator the node spliterator's JavaDoc} for
     * important notes about its behaviour.</p>
     * {@inheritDoc}
     */
    @Override
    public TreeValueSpliterator<N, V> spliterator() {
        return new TreeValueSpliterator<>(nodeSpliterator());
    }

    /**
     * <p><b>Attention:</b> Read {@link TreeNodeSpliterator the node spliterator's JavaDoc} for
     * important notes about its behaviour.</p>
     *
     * @return a stream of this tree's values, including the root node's value
     * @see #spliterator()
     */
    public Stream<V> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * <p><b>Attention:</b> Read {@link TreeNodeSpliterator the node spliterator's JavaDoc} for
     * important notes about its behaviour.</p>
     *
     * @return a value spliterator for this tree node and its children.
     * @see #spliterator()
     */
    @SuppressWarnings("unchecked")
    public TreeNodeSpliterator<N, V> nodeSpliterator() {
        return new TreeNodeSpliterator<>((N) this);
    }

    /**
     * <p><b>Attention:</b> Read {@link TreeNodeSpliterator the node spliterator's JavaDoc} for
     * important notes about its behaviour.</p>
     *
     * @return a stream of this tree's nodes, including the root node
     * @see #nodeSpliterator()
     */
    public Stream<N> nodeStream() {
        return StreamSupport.stream(nodeSpliterator(), false);
    }

    /**
     * <p><b>Attention:</b> Read {@link TreeNodeSpliterator the node spliterator's JavaDoc} for
     * important notes about its behaviour.</p>
     * Performs an action on all nodes of this tree, including the root node.
     *
     * @param action the action to perform
     */
    public void forEachNode(Consumer<? super N> action) {
        nodeSpliterator().forEachRemaining(action);
    }
}
