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

package li.l1t.common.util.task;

import com.google.common.base.Preconditions;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

/**
 * Schedules tasks via Bukkit's Scheduler API.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-06
 */
public class BukkitTaskService implements TaskService {
    private final Plugin plugin;

    public BukkitTaskService(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void async(Runnable task) {
        delayedAsync(task, Duration.ZERO);
    }

    @Override
    public void serverThread(Runnable task) {
        delayed(task, Duration.ZERO);
    }

    @Override
    public void delayed(Runnable task, Duration delay) {
        Preconditions.checkNotNull(task, "task");
        getScheduler().runTaskLater(plugin, task, toTicks(delay));
    }

    private long toTicks(Duration delay) {
        if (delay == null) {
            return 0L;
        }
        return secondsToTicks(delay.getSeconds());
    }

    @Override
    public void delayedAsync(Runnable task, Duration delay) {
        Preconditions.checkNotNull(task, "task");
        getScheduler().runTaskLaterAsynchronously(plugin, task, toTicks(delay));
    }

    @Override
    public void repeating(Runnable task, Duration period) {
        repeating(task, period, period);
    }

    @Override
    public void repeatingAsync(Runnable task, Duration period) {
        repeatingAsync(task, period, period);
    }

    @Override
    public void repeating(Runnable task, Duration period, Duration delay) {
        Preconditions.checkNotNull(task, "task");
        getScheduler().runTaskTimer(plugin, task, toTicks(delay), toTicks(period));
    }

    @Override
    public void repeatingAsync(Runnable task, Duration period, Duration delay) {
        Preconditions.checkNotNull(task, "task");
        getScheduler().runTaskTimerAsynchronously(plugin, task, toTicks(delay), toTicks(period));
    }

    @Override
    public void later(Runnable task, Instant when) {
        Preconditions.checkNotNull(when, "when");
        Duration delay = getPositiveDurationFromNow(when);
        delayed(task, delay);
    }

    private Duration getPositiveDurationFromNow(Instant when) {
        Duration delay = Duration.between(Instant.now(), when);
        Preconditions.checkArgument(!delay.isNegative(), "when must be in the future, is %s ago", delay);
        return delay;
    }

    @Override
    public void laterAsync(Runnable task, Instant when) {
        Preconditions.checkNotNull(when, "when");
        Duration delay = getPositiveDurationFromNow(when);
        delayedAsync(task, delay);
    }

    @Override
    public void dailyAt(Runnable task, LocalTime when) {
        Preconditions.checkNotNull(when, "when");
        repeating(task, Duration.ofDays(1), Duration.between(LocalDateTime.now(), findNextOccurrence(when)));
    }

    @Override
    public void dailyAtAsync(Runnable task, LocalTime when) {
        Preconditions.checkNotNull(when, "when");
        repeating(task, Duration.ofDays(1), Duration.between(LocalDateTime.now(), findNextOccurrence(when)));
    }

    private LocalDateTime findNextOccurrence(LocalTime time) {
        LocalDateTime timeToday = LocalDateTime.now().with(time);
        if (timeToday.isAfter(LocalDateTime.now())) {
            return timeToday;
        } else {
            return LocalDateTime.now().plusDays(1).with(time);
        }
    }


    @Override
    public void weeklyAt(Runnable task, DayOfWeek day, LocalTime time) {
        Duration delay = findDelayUntilNextOccurrence(day, time);
        repeating(task, delay, Duration.ofDays(7));
    }

    private Duration findDelayUntilNextOccurrence(DayOfWeek day, LocalTime time) {
        Preconditions.checkNotNull(day, "day");
        Preconditions.checkNotNull(time, "time");
        LocalDateTime nextOccurrence = LocalDateTime.now()
                .with(TemporalAdjusters.nextOrSame(day)).with(time);
        return Duration.between(LocalDateTime.now(), nextOccurrence);
    }

    @Override
    public void weeklyAtAsync(Runnable task, DayOfWeek day, LocalTime time) {
        Duration delay = findDelayUntilNextOccurrence(day, time);
        repeatingAsync(task, delay, Duration.ofDays(7));
    }

    private long secondsToTicks(long seconds) {
        return seconds * 20L;
    }

    private BukkitScheduler getScheduler() {
        return plugin.getServer().getScheduler();
    }
}
