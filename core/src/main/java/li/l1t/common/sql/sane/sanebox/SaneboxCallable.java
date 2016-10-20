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

import java.sql.SQLException;

/**
 * Something that runs unsafe code that may throw {@link SQLException} and returns a value if
 * successful.
 *
 * @param <R> the return type of the code
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-18
 */
@FunctionalInterface
public interface SaneboxCallable<R> {
    R call() throws SQLException;
}
