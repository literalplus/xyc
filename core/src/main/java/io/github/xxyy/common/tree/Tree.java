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

/**
 * Represents a single tree-style collection of nodes. The data structure is linked. Note that
 * this class does not offer any additional functionality compared to a
 * {@link SimpleTreeNode node}, but currently solely exists to make clearer that this is a root
 * node. This might change in the future.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-22
 */
@SuppressWarnings("WeakerAccess")
public class Tree<N extends TreeNode<N, V>, V> extends SimpleTreeNode<N, V> {

    /**
     * Creates a new tree.
     */
    public Tree(Class<N> nodeClass) {
        super(null, nodeClass);
    }
}
