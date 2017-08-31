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

import java.util.Comparator;
import java.util.Spliterators;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * A spliterator for traversing tree nodes. Has no capabilities. Traverses the tree starting at
 * the root, continuing up all the way to the leaves and then staying at the lowest possible
 * child id it hasn't visited until it reaches the root again.
 *
 * <p><b>Note:</b> This too traverses the root element, that is, the skill tree itself. This
 * behaviour may lead to infinite recursion if this spliterator is used improperly.</p>
 *
 * @param <V> the value type of the tree that is spliterated
 * @param <N> the node type of the tree that is spliterated
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @see TreeValueSpliterator a spliterator that spliterates values instead of nodes
 * @since 2016-06-22
 */
@SuppressWarnings("WeakerAccess")
public class TreeNodeSpliterator<N extends TreeNode<N, V>, V>
        extends Spliterators.AbstractSpliterator<N> {
    private Stack<Integer> position = new Stack<>();
    private N next;

    /**
     * Creates a spliterator for given root.
     *
     * @param root the root of the tree to spliterate
     */
    @SuppressWarnings("unchecked")
    public TreeNodeSpliterator(N root) {
        super(Long.MAX_VALUE, 0);
        next = root; //the root node is the root itself
    }

    @Override
    public boolean tryAdvance(Consumer<? super N> action) {
        //This algorithm goes all the way up to the leaves and then continues to stay as far up
        //as possible, traversing low child ids first.
        //
        //We already know what node is next because the previous call figured that out
        if (next == null) { //if we don't have anything left, exit
            return false;
        }
        action.accept(next); //do it now, because we'll lost that reference later
        if (tryContinueUp(next, 0)) { //if we can, traverse up (to leaves)
            return true;
        }
        //If not, we must go back and seek a different path
        N parent = next.getParent();

        while (canContinueDownFrom(parent)) { //while we can go farther down
            //Top of the stack is where we are currently, so the next adjacent node is that plus one
            int nextChildId = position.pop() + 1;
            if (tryContinueUp(parent, nextChildId)) { //if the parent has another child we haven't
                return true;                          //seen yet, go up there
            }
            //otherwise, we must continue going down until we find an unvisited path
            parent = parent.getParent(); //position is updated on next iteration
        }
        //We can't go farther down, that means we have reached the root again
        next = null; //Nothing to see for the next call
        return true; //This last time we did it though
    }

    private boolean canContinueDownFrom(N from) {
        //noinspection SimplifiableIfStatement
        if (position.isEmpty()) {
            return false; //this happens if the iteration base node is not the root node
        }
        return from != null;
    }

    private boolean tryContinueUp(N relativeTo, int childId) {
        //noinspection ConstantConditions
        return relativeTo.hasChild(childId) && continueUp(relativeTo, childId);
    }

    private boolean continueUp(N relativeTo, int childId) {
        next = relativeTo.getChildren().get(childId);
        position.push(childId);
        return true;
    }

    @Override
    public void forEachRemaining(Consumer<? super N> action) {
        //noinspection StatementWithEmptyBody
        while (tryAdvance(action)) ; //Just advance as long as possible
    }

    @Override
    public long getExactSizeIfKnown() {
        return -1;
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return false;
    }

    @Override
    public Comparator<? super N> getComparator() {
        throw new IllegalStateException("not SORTED");
    }
}
