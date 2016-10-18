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

/**
 * A scoped session that provides itself as transaction object.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @see ScopedSession the superinterface's JavaDoc for important information about the behaviour of
 * implementations
 * @since 2016-10-18
 */
public interface RawScopedSession extends ScopedSession<RawScopedSession> {
}
