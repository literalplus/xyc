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
