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

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Represents a node in a skill tree.
 *
 * @param <V> the value type of the tree this node is part of
 * @param <N> the node type of the tree this node is part of
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-22
 */
@SuppressWarnings("WeakerAccess")
public interface TreeNode<N extends TreeNode<N, V>, V> extends Iterable<V> {

    /**
     * @return the immediate parent node of this node, of null if this is a root node
     */
    N getParent();

    /**
     * Gets the immediate children of this node. Children are mutually exclusive, that is, once
     * one is obtained, others cannot be obtained without unlearning the first. If this is a
     * branch node, returns only one child. If this is a leaf node, that is, this node has no
     * children, returns an empty set.
     *
     * @return the immutable set of children of this node
     */
    List<N> getChildren();

    /**
     * Adds a new child to this node. The behaviour of this method is undefined if the new child
     * has children of itself already - implementations may choose to throw an exception in that
     * case, if noted in their overriding JavaDoc.
     *
     * @param newChild the child to add
     * @throws IllegalArgumentException if the child is not of type &lt;N&gt;
     */
    void addChild(N newChild);

    /**
     * Removes a child from this node. Note that implementations must call
     * {@link #updatePosition()} on all children whose position changes as a result of this call.
     * Whether other children are updated too is up to the implementation.
     *
     * @param oldChild the child to remove
     */
    void removeChild(N oldChild);

    /**
     * @return whether this is a root node, that is, this node has no parents
     */
    boolean isRoot();

    /**
     * @return whether this is a leaf node, that is, this node has no children
     */
    boolean isLeaf();

    /**
     * @param childId the zero-based identifier of a child
     * @return whether this node has a child with that id
     */
    boolean hasChild(int childId);

    /**
     * @return whether this is a branching node, that is, this node has multiple children
     */
    boolean isBranching();

    /**
     * Gets the value carried by this node. If this node has no value, null is returned.
     *
     * @return the value carried by this node
     */
    V getValue();

    /**
     * Sets the value carried by this node.
     *
     * @param newValue the new value to be carried by this node, or null for no value
     */
    void setValue(V newValue);

    /**
     * Gets the child id of a direct child of this node. The child id is the index of the child
     * in the list of children. Note that the child id changes if a child with a lower child id
     * is removed.
     * <p><b>Note:</b> The behaviour of this method is undefined if called from a node
     * constructor. In such case, use {@link #getChildren() the size of the parent's children list}
     * as child id.
     * </p>
     *
     * @param child the direct child to get the child id for
     * @return the index of child in the list of children of this node
     * @throws IllegalArgumentException if child is not a direct child of this node
     * @since 3.3.9.0
     */
    int getChildId(N child);

    /**
     * The child coordinates of this node relative to the root node of this tree. This is,
     * starting from the root node, the {@link #getChildId(TreeNode)} child id} of every node
     * on the shortest path to this node. Since the root node does not have a parent and
     * therefore no child id, it is represented as an empty array.
     *
     * @return the shortest path to this node relative to the root node
     * @since 3.3.9.0
     */
    int[] getPosition();

    /**
     * Causes this node to recompute its position in the tree. By interface contract, this method
     * must be called by parent nodes if the position changes, that is, a child somewhere is
     * removed.
     *
     * @since 3.3.9.0
     */
    void updatePosition();

    /**
     * Gets a direct child of this node by its child id.
     *
     * @param childId the id of the child to get
     * @return the child with given child id
     * @throws IndexOutOfBoundsException if there is no such child
     * @since 3.3.9.0
     */
    N getChild(int childId);

    /**
     * Gets a child of this node by its {@link #getPosition() child position} relative to this
     * node. The position of this node is an empty array. Note that implementations may call
     * {@link #getChild(int)} directly instead of recursing directly in order to save unnecessary
     * array copies.
     *
     * @param position the child position relative to this node
     * @return the node at given position
     * @throws IndexOutOfBoundsException if any of the child ids are out of range for the
     *                                   referenced node
     * @since 3.3.9.0
     */
    N getChild(int[] position);

    /**
     * <p><b>Attention:</b> Read {@link TreeNodeSpliterator the node spliterator's JavaDoc} for
     * important notes about its behaviour.</p>
     *
     * @return a stream of this tree's values, including the root node's value
     * @see #spliterator()
     */
    Stream<V> stream();

    /**
     * <p><b>Attention:</b> Read {@link TreeNodeSpliterator the node spliterator's JavaDoc} for
     * important notes about its behaviour.</p>
     *
     * @return a value spliterator for this tree node and its children.
     * @see #spliterator()
     */
    @SuppressWarnings("unchecked")
    TreeNodeSpliterator<N, V> nodeSpliterator();

    /**
     * <p><b>Attention:</b> Read {@link TreeNodeSpliterator the node spliterator's JavaDoc} for
     * important notes about its behaviour.</p>
     *
     * @return a stream of this tree's nodes, including the root node
     * @see #nodeSpliterator()
     */
    Stream<N> nodeStream();

    /**
     * <p><b>Attention:</b> Read {@link TreeNodeSpliterator the node spliterator's JavaDoc} for
     * important notes about its behaviour.</p>
     * Performs an action on all nodes of this tree, including the root node.
     *
     * @param action the action to perform
     */
    void forEachNode(Consumer<? super N> action);
}
