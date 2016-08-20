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

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.hasSize;

public class TreeNodeTest extends AbstractTreeTest {
    @Test
    public void testRemoveChild() {
        //given
        TestTreeNode root = givenARootNode();
        TestTreeNode child = givenADirectChildOf(root);
        //when
        root.removeChild(child);
        //then
        Assert.assertThat(root.getChildren(), hasSize(0));
    }
}
