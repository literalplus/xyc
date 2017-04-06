/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
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
                .with(TemporalAdjusters.next(day)).with(time);
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
