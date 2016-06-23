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

import java.util.List;

/**
 * Represents a node in a skill tree.
 *
 * @param <V> the value type of the tree this node is part of
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-22
 */
@SuppressWarnings("WeakerAccess")
public interface TreeNode<V> extends Iterable<V> {

    /**
     * @return the immediate parent node of this node, of null if this is a root node
     */
    TreeNode<V> getParent();

    /**
     * Gets the immediate children of this node. Children are mutually exclusive, that is, once
     * one is obtained, others cannot be obtained without unlearning the first. If this is a
     * branch node, returns only one child. If this is a leaf node, that is, this node has no
     * children, returns an empty set.
     *
     * @return the immutable set of children of this node
     */
    List<TreeNode<V>> getChildren();

    /**
     * Adds a new child to this node.
     *
     * @param newChild the child to add
     */
    void addChild(TreeNode<V> newChild);

    /**
     * Removes a child from this node.
     *
     * @param oldChild the child to remove
     */
    void removeChild(TreeNode<V> oldChild);

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
}
