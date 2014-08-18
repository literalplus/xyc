/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without permission from the
 *  original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.collections;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 31/01/14
 */
final class WeakListIterator<E> implements Iterator<E> {
    private final Iterator<WeakReference<E>> baseIterator;
    private E next;

    @java.beans.ConstructorProperties({"baseIterator"})
    public WeakListIterator(Iterator<WeakReference<E>> baseIterator) {
        this.baseIterator = baseIterator;
    }

    @Override
    public void remove() {
        baseIterator.remove();
    }

    @Override
    public boolean hasNext() {
        if (next != null) {
            return true;
        }

        if (!baseIterator.hasNext()) {
            return false;
        }

        next = baseIterator.next().get();

        if (next == null) {
            baseIterator.remove();
            return hasNext();
        }

        return true;
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        final E tempNext = next;
        next = null;
        return tempNext;
    }
}
