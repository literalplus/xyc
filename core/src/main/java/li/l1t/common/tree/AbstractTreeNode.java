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
 * Abstract base class for tree nodes. The reason why this class is abstract is because recursive
 * (self-referencing) generics cannot be satisfied when creating an instance, hence a subclass is
 * required to be able to instantiate a node.
 *
 * @param <N> the node type of the tree this node is part of
 * @param <V> the value type of the tree this node is part of
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-22
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractTreeNode<N extends TreeNode<N, V>, V> implements TreeNode<N, V> {

    private final N parent;
    private final Class<N> nodeClass; //for checking of nodes
    private final List<N> children = new ArrayList<>(4); //and even that is an exaggeration
    private V value;
    private int[] position;

    /**
     * Creates a new abstract tree node. Note that {@code nodeClass} must actually be of type {@code
     * Class<N>}, but Java's type system doesn't allow to pass instances of subclasses if they have
     * generic type parameters themselves. This constructor checks that this node and the parent are
     * actually instances of the node class.
     *
     * @param parent    the parent node, or null if this is a root node
     * @param nodeClass the raw class of the nodes
     */
    @SuppressWarnings("unchecked")
    public AbstractTreeNode(N parent, Class nodeClass) { //we can't avoid this if we want to have subclasses have generic type params
        this.nodeClass = (Class<N>) nodeClass;
        checkNodeType(getClass());
        this.parent = parent;
        if (parent != null) {
            checkNodeType(parent.getClass());
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
        checkNodeType(newChild.getClass());
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
    public boolean hasChildren() {
        return !children.isEmpty();
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
        if (parent == null) {
            position = new int[0];
            return;
        }
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
     * important notes about its behaviour.</p> {@inheritDoc}
     */
    @Override
    public Iterator<V> iterator() {
        return Spliterators.iterator(spliterator());
    }

    /**
     * <p><b>Attention:</b> Read {@link TreeNodeSpliterator the node spliterator's JavaDoc} for
     * important notes about its behaviour.</p> {@inheritDoc}
     */
    @Override
    public void forEach(Consumer<? super V> action) {
        spliterator().forEachRemaining(action);
    }

    /**
     * <p><b>Attention:</b> Read {@link TreeNodeSpliterator the node spliterator's JavaDoc} for
     * important notes about its behaviour.</p> {@inheritDoc}
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
        Preconditions.checkArgument(nodeClass.isAssignableFrom(aClass),
                "node must be subclass of <N> (%s), is: %s", nodeClass, aClass);
    }
}
