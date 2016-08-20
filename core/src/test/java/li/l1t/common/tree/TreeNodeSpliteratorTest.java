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

import org.junit.Test;

import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TreeNodeSpliteratorTest extends AbstractTreeTest {
    @Test
    public void testSingleNode() {
        //given
        TestTreeNode root = givenARootNode();
        TreeNodeSpliterator<TestTreeNode, Object> spliterator = root.nodeSpliterator();
        //when
        whenWeAdvanceToTheNextNodeWhichIs(root, spliterator);
        //then
        thenNoNodesAreLeft(spliterator);
    }

    private void whenWeAdvanceToTheNextNodeWhichIs(
            TestTreeNode root, TreeNodeSpliterator<TestTreeNode, Object> spliterator
    ) {
        assertThat(spliterator.tryAdvance(assertNodeIs(root)), is(true));
    }

    private Consumer<TestTreeNode> assertNodeIs(TestTreeNode root) {
        return node -> assertThat("spliterated wrong node", node, is(root));
    }

    private void thenNoNodesAreLeft(TreeNodeSpliterator<TestTreeNode, Object> spliterator) {
        boolean hasNext = spliterator.tryAdvance(assertNeverCalled());
        assertThat(hasNext, is(false));
    }

    private Consumer<TestTreeNode> assertNeverCalled() {
        return node -> assertThat("no more nodes should be left", node, is(nullValue()));
    }

    @Test
    public void testGrandchildren() {
        //given
        TestTreeNode root = givenARootNode();
        TestTreeNode child = givenADirectChildOf(root);
        TestTreeNode grandchild = givenADirectChildOf(child);
        TreeNodeSpliterator<TestTreeNode, Object> spliterator = root.nodeSpliterator();
        //when
        whenWeAdvanceToTheNextNodeWhichIs(root, spliterator);
        whenWeAdvanceToTheNextNodeWhichIs(child, spliterator);
        whenWeAdvanceToTheNextNodeWhichIs(grandchild, spliterator);
        //then
        thenNoNodesAreLeft(spliterator);
    }

    @Test
    public void testTwoGrandchildren() {
        //given
        TestTreeNode root = givenARootNode();
        TestTreeNode child = givenADirectChildOf(root);
        TestTreeNode grandchild1 = givenADirectChildOf(child);
        TestTreeNode grandchild2 = givenADirectChildOf(child);
        TreeNodeSpliterator<TestTreeNode, Object> spliterator = root.nodeSpliterator();
        //when
        whenWeAdvanceToTheNextNodeWhichIs(root, spliterator);
        whenWeAdvanceToTheNextNodeWhichIs(child, spliterator);
        whenWeAdvanceToTheNextNodeWhichIs(grandchild1, spliterator);
        whenWeAdvanceToTheNextNodeWhichIs(grandchild2, spliterator);
        //then
        thenNoNodesAreLeft(spliterator);
    }

    @Test
    public void testTwoGrandchildrenWithChildren() {
        //given
        TestTreeNode root = givenARootNode();
        TestTreeNode child = givenADirectChildOf(root);
        TestTreeNode grandchild1 = givenADirectChildOf(child);
        TestTreeNode greatgrandchild1 = givenADirectChildOf(grandchild1);
        TestTreeNode grandchild2 = givenADirectChildOf(child);
        TestTreeNode greatgrandchild2 = givenADirectChildOf(grandchild2);
        TreeNodeSpliterator<TestTreeNode, Object> spliterator = root.nodeSpliterator();
        //when
        whenWeAdvanceToTheNextNodeWhichIs(root, spliterator);
        whenWeAdvanceToTheNextNodeWhichIs(child, spliterator);
        whenWeAdvanceToTheNextNodeWhichIs(grandchild1, spliterator);
        whenWeAdvanceToTheNextNodeWhichIs(greatgrandchild1, spliterator);
        whenWeAdvanceToTheNextNodeWhichIs(grandchild2, spliterator);
        whenWeAdvanceToTheNextNodeWhichIs(greatgrandchild2, spliterator);
        //then
        thenNoNodesAreLeft(spliterator);
    }
}
