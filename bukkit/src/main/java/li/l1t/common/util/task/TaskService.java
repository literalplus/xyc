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

import java.time.*;

/**
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-06
 */
public interface TaskService {
    /**
     * Schedules an asynchronous task for immediate execution in another thread.
     *
     * @param task the task to execute
     * @see #serverThread(Runnable) for scheduling in the server thread
     */
    void async(Runnable task);

    /**
     * Schedules a task for immediate execution in the server thread.
     *
     * @param task the task to execute
     * @see #async(Runnable) for async scheduling
     */
    void serverThread(Runnable task);

    /**
     * Schedules a task for execution in the server thread after given delay has passed.
     *
     * @param task  the task to execute
     * @param delay the duration to wait before executing given task
     */
    void delayed(Runnable task, Duration delay);

    /**
     * Schedules a task for execution in another thread after given delay has passed.
     *
     * @param task  the task to execute
     * @param delay the duration to wait before executing given task
     */
    void delayedAsync(Runnable task, Duration delay);

    /**
     * Schedules a task for execution in the server thread periodically with given duration between executions. The task
     * is executed after the period has passed, i.e. the first execution will occur after given period has passed for
     * the first time.
     *
     * @param task   the task to execute
     * @param period the duration to wait between each execution
     */
    void repeating(Runnable task, Duration period);

    /**
     * Schedules a task for execution in another thread periodically with given duration between executions. The task
     * is executed after the period has passed, i.e. the first execution will occur after given period has passed for
     * the first time.
     *
     * @param task   the task to execute
     * @param period the duration to wait between each execution
     */
    void repeatingAsync(Runnable task, Duration period);

    /**
     * Schedules a task for execution in the server thread periodically with given duration between executions. The
     * first execution is delayed until given delay has passed.
     *
     * @param task   the task to execute
     * @param period the duration to wait between each execution
     * @param delay  the delay to wait before the first execution
     */
    void repeating(Runnable task, Duration period, Duration delay);

    /**
     * Schedules a task for execution in the server thread periodically with given duration between executions. The
     * first execution is delayed until given delay has passed.
     *
     * @param task   the task to execute
     * @param period the duration to wait between each execution
     * @param delay  the delay to wait before the first execution
     */
    void repeatingAsync(Runnable task, Duration period, Duration delay);

    /**
     * Schedules a task for execution at a given instant in the server thread.
     *
     * @param task the task to execute
     * @param when when to execute the task
     * @throws IllegalArgumentException if when is in the past
     */
    void later(Runnable task, Instant when);

    /**
     * Schedules a task for execution at a given instant in another thread.
     *
     * @param task the task to execute
     * @param when when to execute the task
     * @throws IllegalArgumentException if when is in the past
     */
    void laterAsync(Runnable task, Instant when);

    /**
     * Schedules a task for daily execution at given time in the server thread.
     *
     * @param task the task to execute
     * @param when when to execute the task
     */
    void dailyAt(Runnable task, LocalTime when);

    /**
     * Schedules a task for daily execution at given time in another thread.
     *
     * @param task the task to execute
     * @param when when to execute the task
     */
    void dailyAtAsync(Runnable task, LocalTime when);

    /**
     * Schedules a task for weekly execution at given time in the server thread.
     *
     * @param task the task to execute
     * @param day  the day of week to execute the task at
     * @param time the time to execute the task at
     */
    void weeklyAt(Runnable task, DayOfWeek day, LocalTime time);


    /**
     * Schedules a task for weekly execution at given time in another thread.
     *
     * @param task the task to execute
     * @param day  the day of week to execute the task at
     * @param time the time to execute the task at
     */
    void weeklyAtAsync(Runnable task, DayOfWeek day, LocalTime time);
}
