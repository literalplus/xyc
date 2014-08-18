/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.util.task;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * An {@link io.github.xxyy.common.util.task.ImprovedBukkitRunnable} which throws an {@link java.lang.UnsupportedOperationException}
 * when any of the async schedule methods are called to prevent scheduling it as an async task.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 20.7.14
 */
public abstract class NonAsyncBukkitRunnable extends ImprovedBukkitRunnable {
    @Override
    public synchronized BukkitTask runTaskAsynchronously(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized BukkitTask runTaskLaterAsynchronously(Plugin plugin, long delay) throws IllegalArgumentException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized BukkitTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        throw new UnsupportedOperationException();
    }
}