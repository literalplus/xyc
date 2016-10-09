/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.util;

import javax.annotation.Nullable;

/**
 * Provides a static utility method to silently close {@link AutoCloseable} instances.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
public class Closer {
    private Closer() {

    }

    /**
     * Attempts to close a closeable thing, swallowing any exception and doing nothing if it is
     * null.
     *
     * @param closeable the thing to close
     * @return {@code true} if the {@link AutoCloseable#close()} method did not throw any exception
     * or the argument was {@code null}
     */
    public static boolean close(@Nullable AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return true;
            } catch (Exception ignore) {
                return false;
            }
        } else {
            return true;
        }
    }
}
