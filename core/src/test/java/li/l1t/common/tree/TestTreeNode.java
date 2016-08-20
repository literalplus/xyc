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

/**
 * A SimpleTreeNode subclass for testing.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-20
 */
class TestTreeNode extends SimpleTreeNode<TestTreeNode, Object> {
    public TestTreeNode(TestTreeNode parent) {
        super(parent, TestTreeNode.class);
    }
}
