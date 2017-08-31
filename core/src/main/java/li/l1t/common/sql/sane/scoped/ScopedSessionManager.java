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

package li.l1t.common.sql.sane.scoped;

import li.l1t.common.exception.InternalException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Keeps record of a single scoped session per thread. Messes up everything if anybody fails to
 * properly join and close their sessions.
 *
 * @param <S> the type of scoped session provided
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-18
 */
public class ScopedSessionManager<S extends ScopedSession<?>> {
    private final Supplier<? extends S> sessionFactory;
    private ThreadLocal<S> sessionLocal = new ThreadLocal<>();
    private ReentrantLock sessionLock = new ReentrantLock();

    /**
     * @param sessionFactory the supplier to get new scoped session instances from
     */
    public ScopedSessionManager(Supplier<? extends S> sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Gets the current session used by the current thread if a session scope already exists or
     * crates a new session and scope otherwise.
     *
     * @return a scoped session
     * @throws InternalException if the session lock cannot be obtained
     */
    public S scoped() {
        attemptLockSessionLock();
        try {
            S currentSession = sessionLocal.get();
            if (!isValidSession(currentSession)) {
                currentSession = sessionFactory.get();
                sessionLocal.set(currentSession);
            }
            return currentSession;
        } finally {
            sessionLock.unlock();
        }
    }

    private boolean isValidSession(ScopedSession session) {
        return session != null && session.acceptsFurtherReferences();
    }

    private void attemptLockSessionLock() {
        try {
            if (!sessionLock.tryLock(1, TimeUnit.SECONDS)) {
                throw new InternalException("Failed to obtain lock for getting a session in a second");
            }
        } catch (InterruptedException e) {
            throw new InternalException("Interrupted while waiting for session lock", e);
        }
    }
}
