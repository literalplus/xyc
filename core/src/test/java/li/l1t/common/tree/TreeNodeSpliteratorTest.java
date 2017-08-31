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

    @Test
    public void testStartAtChild() {
        //given
        TestTreeNode child = givenADirectChildOf(givenARootNode());
        TestTreeNode grandchild = givenADirectChildOf(child);
        TreeNodeSpliterator<TestTreeNode, Object> spliterator = child.nodeSpliterator();
        //when
        whenWeAdvanceToTheNextNodeWhichIs(child, spliterator);
        whenWeAdvanceToTheNextNodeWhichIs(grandchild, spliterator);
        //then
        thenNoNodesAreLeft(spliterator);
    }
}
