package io.github.xxyy.common.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helps dealing with a multithreading environment.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 14.4.14
 */
public class ThreadHelper {
    public void printThreadDump(Logger logger) {
        ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
        for (ThreadInfo thread : threads) {
            dumpThread(thread, logger);
        }
    }

    private static void dumpThread(ThreadInfo thread, Logger log) {
        log.log(Level.WARNING, "------------------------------");
        //
        log.log(Level.WARNING, " Thread: " + thread.getThreadName());
        log.log(Level.WARNING, "\tPID: " + thread.getThreadId()
                + " | Suspended? " + thread.isSuspended()
                + " | State: " + thread.getThreadState());
        if (thread.getLockedMonitors().length != 0) {
            log.log(Level.WARNING, "\tThread is waiting on monitor(s):");
            for (MonitorInfo monitor : thread.getLockedMonitors()) {
                log.log(Level.WARNING, "\t\tLocked on:" + monitor.getLockedStackFrame());
            }
        }
        log.log(Level.WARNING, "\tStack:");
        //
        for (StackTraceElement stack : thread.getStackTrace()) {
            log.log(Level.WARNING, "\t\t" + stack);
        }
    }
}
