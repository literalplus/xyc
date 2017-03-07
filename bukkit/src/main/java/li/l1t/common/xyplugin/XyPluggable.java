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

import li.l1t.common.version.PluginVersion;

/**
 * Common interface for plugins interfacing with XYC using the {@link AbstractXyPlugin} API.
 */
public interface XyPluggable {
    String pluginPrefix = "§7[§8XYC§7]";

    void disable();

    void enable();

    PluginVersion getPluginVersion();

    /**
     * Convenience shorthand for scheduling an asynchronous task with the server's scheduler for immediate execution.
     *
     * @param task the task to execute
     * @see #serverThread(Runnable) (Runnable) for scheduling in the server thread
     */
    void async(Runnable task);

    /**
     * Convenience shorthand for scheduling an task with the server's scheduler for immediate execution in the server
     * thread.
     *
     * @param task the task to execute
     * @see #async(Runnable) for async scheduling
     */
    void serverThread(Runnable task);
}
