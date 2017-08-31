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

package li.l1t.common.xyplugin;

import li.l1t.common.util.task.TaskService;
import li.l1t.common.version.PluginVersion;
import org.bukkit.plugin.Plugin;

/**
 * Common interface for plugins interfacing with XYC using the {@link AbstractXyPlugin} API.
 */
public interface XyPluggable extends Plugin {
    String pluginPrefix = "§7[§8XYC§7]";

    void disable();

    void enable();

    PluginVersion getPluginVersion();

    /**
     * Convenience shorthand for scheduling an asynchronous task with the server's scheduler for immediate execution.
     *
     * @param task the task to execute
     * @see #tasks() for more advanced configurations
     */
    void async(Runnable task);

    /**
     * Convenience shorthand for scheduling a task with the server's scheduler for immediate execution in the server
     * thread.
     *
     * @param task the task to execute
     * @see #tasks() for more advanced configurations
     */
    void serverThread(Runnable task);

    /**
     * @return a service for scheduling tasks related to this plugin
     */
    TaskService tasks();
}
