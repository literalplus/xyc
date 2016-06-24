/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.tree;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
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
    private final Class<N> nodeClass; //for checking of nodes
    private final List<N> children = new ArrayList<>(4); //and even that is an exaggeration
    private V value;
    private int[] position;

    public SimpleTreeNode(N parent, Class<N> nodeClass) {
        this.nodeClass = nodeClass;
        checkNodeType(getClass());
        this.parent = parent;
        if (parent != null) {
            position = Arrays.copyOf(parent.getPosition(), parent.getPosition().length + 1);
            position[position.length - 1] = parent.getChildren().size(); //must be id of next child, that's us
        } else {
            position = new int[0]; //We're the root node
        }
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
        Preconditions.checkArgument(newChild.getClass().isAssignableFrom(nodeClass), "");
        children.add(newChild);
    }

    @Override
    public void removeChild(N oldChild) {
        //no type check necessary: child type gets checked on add, anything else won't have any effect
        children.remove(oldChild);
        forEachNode(TreeNode::updatePosition); //update children positions
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
    public int getChildId(N child) {
        Preconditions.checkArgument(children.contains(child), "%s must be a direct child!", child);
        return getChildren().indexOf(child);
    }

    @Override
    public int[] getPosition() {
        return position;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void updatePosition() {
        position = Arrays.copyOf(parent.getPosition(), parent.getPosition().length + 1);
        position[position.length - 1] = parent.getChildId((N) this); // <--  unchecked (check in constructor)
    }

    @Override
    public N getChild(int childId) {
        return children.get(childId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public N getChild(int[] position) {
        N node = (N) this; //<-- unchecked (not necessary because we check in constructor)
        for (int childId : position) { //go all the way down
            node = node.getChild(childId); //select the child at that position
        } //    v  zero-length arrays return ourselves
        return node;
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

    @Override
    public Stream<V> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public TreeNodeSpliterator<N, V> nodeSpliterator() {
        return new TreeNodeSpliterator<>((N) this);
    }

    @Override
    public Stream<N> nodeStream() {
        return StreamSupport.stream(nodeSpliterator(), false);
    }

    @Override
    public void forEachNode(Consumer<? super N> action) {
        nodeSpliterator().forEachRemaining(action);
    }

    private void checkNodeType(Class<?> aClass) {
        Preconditions.checkArgument(aClass.isAssignableFrom(nodeClass),
                "node must be subclass of <N> (%s), is: %s", nodeClass, aClass);
    }
}
