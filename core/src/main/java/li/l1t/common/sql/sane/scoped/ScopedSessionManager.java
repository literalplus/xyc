/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
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
