/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.util.task;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * Adds some extra functionality to {@link org.bukkit.scheduler.BukkitRunnable}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 20.7.14
 */
public abstract class ImprovedBukkitRunnable extends BukkitRunnable {
    /**
     * Cancels this task if it's scheduled.
     * Does not throw an exception if it's not scheduled.
     *
     * @see #cancel()
     */
    public void tryCancel() {
        try {
            this.cancel();
        } catch(IllegalStateException ignore) {
            //Bukkit won't even return a -1 task id - They'll just throw this and nothing to do about it
            //This is totally how you API properly
        }
    }
}
