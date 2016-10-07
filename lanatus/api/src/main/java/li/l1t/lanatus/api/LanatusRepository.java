/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.api;

/**
 * A repository associated with a Lanatus client. <p>Note that all methods that access the database
 * may throw {@link li.l1t.common.exception.InternalException}s if a database failure occurs.</p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-09-28
 */
public interface LanatusRepository {
    /**
     * @return the client this repository is associated with
     */
    LanatusClient client();
}
