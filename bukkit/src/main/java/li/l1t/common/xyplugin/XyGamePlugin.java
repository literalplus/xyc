/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.xyplugin;

/**
 * A plugin using XyGameLib.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public interface XyGamePlugin {
    /**
     * Called when a fatal error occurred. If possible, this should shutdown or restart the server running this plugin.
     *
     * @param desc   Short description of the error.
     * @param caller Which method reported this error?
     */
    void setError(String desc, String caller);
}
