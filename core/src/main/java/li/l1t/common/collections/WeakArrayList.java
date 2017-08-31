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

package li.l1t.common.collections;

import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * An alternative {@link java.util.ArrayList} implementation that only keeps weak references to its contents.
 * <b>Works exactly the same as a normal ArrayList!</b>
 * Note that this implementation does <b>NOT</b> support {@code null} elements!
 * Also note that the indexes of elements are subject to change, since garbage-collected elements are removed from the list, shifting the indexes.
 * Further note that listIterator() and subList() methods are <b>not</b> supported by this implementation.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 31/01/14
 * @deprecated This class is incomplete and has some serious API issues (namely indexes shifting and therefore not
 * complying to List interface specification). There is a working alternative in the Java API, which can be found in
 * <a href="http://stackoverflow.com/a/4062950/1117552">this StackOverflow answer</a>.
 */
@Deprecated @SuppressWarnings("all")
public final class WeakArrayList<E> implements List<E> {

    private final ArrayList<WeakReference<E>> dataList;

    public WeakArrayList() {
        dataList = new ArrayList<>();
    }

    public WeakArrayList(final int initialCapacity) {
        dataList = new ArrayList<>(initialCapacity);
    }

    public WeakArrayList(final Collection<E> collectionToCopy) {
        dataList = new ArrayList<>();

        addAll(collectionToCopy);
    }

    @Override
    public int size() {
        return dataList.size();
    }

    @Override
    public boolean isEmpty() {
        return dataList.isEmpty();
    }

    @Override
    public boolean contains(@Nonnull final Object toCheck) {
        for (final WeakReference<E> aDataList : dataList) {
            if (toCheck.equals(aDataList.get())) {
                return true;
            }
        }

        return false;
    }

    @Override
    @Nonnull
    public Iterator<E> iterator() {
        return new WeakListIterator<>(dataList.iterator());
    }

    @Override
    @Nonnull
    public Object[] toArray() {
        return dataList.toArray();
    }

    @Override
    @Nonnull
    public <T1> T1[] toArray(@Nonnull final T1[] a) {
        //noinspection SuspiciousToArrayCall
        return dataList.toArray(a);
    }

    @Override
    public boolean add(@Nonnull final E t) {
        return dataList.add(new WeakReference<>(t));
    }

    @Override
    public boolean remove(@Nonnull final Object toDelete) {
        final Iterator<WeakReference<E>> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            if (toDelete.equals(iterator.next().get())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(@Nonnull final Collection<?> toCheck) {
        for (final Object obj : toCheck) {
            if (!contains(obj)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(@Nonnull final Collection<? extends E> toAdd) {
        //Two iterations, but it's more important not to get into an illegal state
        Validate.noNullElements(toAdd, "This implementation does not support null elements!");

        //noinspection Convert2streamapi
        for (final E newElement : toAdd) {
            add(newElement);
        }

        return true;
    }

    @Override
    public boolean addAll(final int index, @Nonnull final Collection<? extends E> c) {
        throw new UnsupportedOperationException(); //I don't see an need for this. Anyone have some spare time?
    }

    @Override
    public boolean removeAll(@Nonnull final Collection<?> toRemove) {
        boolean changed = false;
        for (final Object obj : toRemove) {
            if (changed) {
                remove(obj); //If it's already true, ignore subsequent calls
            } else {
                changed = remove(obj); //It is already false, nothing can break
            }
        }

        return changed;
    }

    @Override
    public boolean retainAll(@Nonnull final Collection<?> toRetain) {
        final Iterator<WeakReference<E>> iterator = dataList.iterator();
        boolean changed = false;

        while (iterator.hasNext()) {
            if (!toRetain.contains(iterator.next())) {
                iterator.remove();
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public void clear() {
        dataList.clear();
    }

    @Override
    public E get(final int index) {
        return dataList.get(index).get();
    }

    @Override
    public E set(final int index, final E element) {
        final E prevOccupier = dataList.get(index).get();
        dataList.set(index, new WeakReference<>(element));
        return prevOccupier;
    }

    @Override
    public void add(final int index, final E element) {
        dataList.add(index, new WeakReference<>(element));
    }

    @Override
    public E remove(final int index) {
        return dataList.remove(index).get();
    }

    @Override
    public int indexOf(@Nonnull final Object toCheck) {
        for (int i = 0; i < size(); i++) {
            if (toCheck.equals(get(i))) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(final Object toCheck) {
        for (int i = size(); i >= 0; i--) { //Iterate backwards for #efficiency
            if (toCheck.equals(get(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    @Nonnull
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Nonnull
    public ListIterator<E> listIterator(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Nonnull
    public List<E> subList(final int fromIndex, final int toIndex) {
        throw new UnsupportedOperationException();
    }

}
