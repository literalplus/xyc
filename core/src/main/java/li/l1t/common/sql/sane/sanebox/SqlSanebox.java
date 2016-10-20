/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.sql.sane.sanebox;

import com.google.common.base.Preconditions;
import li.l1t.common.exception.DatabaseException;

import java.sql.SQLException;

/**
 * Utility class providing a way for client code to execute while safely transforming any {@link
 * SQLException} into a {@link DatabaseException}, providing a kind of sandbox, for the sane SQL
 * API, get it haha
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-18
 */
public class SqlSanebox {
    private SqlSanebox() {
        //no construction intended
    }

    /**
     * Runs given action immediately, wrapping any {@link SQLException} it may throw in a more
     * friendly {@link DatabaseException}.
     *
     * @param action the action to execute
     * @throws DatabaseException if the action throws an {@link SQLException}
     */
    public static void run(SaneboxRunnable action) throws DatabaseException {
        Preconditions.checkNotNull(action, "action");
        try {
            action.run();
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    /**
     * Runs given action immediately, wrapping any {@link SQLException} it may throw in a more
     * friendly {@link DatabaseException} and returning its return value.
     *
     * @param action the action to execute
     * @throws DatabaseException if the action throws an {@link SQLException}
     */
    public static <R> R run(SaneboxCallable<? extends R> action) throws DatabaseException {
        Preconditions.checkNotNull(action, "action");
        try {
            return action.call();
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }
}
