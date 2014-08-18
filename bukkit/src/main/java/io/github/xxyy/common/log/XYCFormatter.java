/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.log;

import org.bukkit.Bukkit;

import io.github.xxyy.common.XycConstants;
import io.github.xxyy.common.xyplugin.AbstractXyPlugin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


/**
 * A custom log formatter!
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class XYCFormatter extends Formatter {
    /**
     * Name of the plugin owning this {@link XYCFormatter}.
     */
    public String pluginName;
    /**
     * Version of the plugin owning this {@link XYCFormatter}.
     */
    public String pluginVersion;
    /**
     * Name of this log file.
     */
    public String logName;
    /**
     * Whether to print calling method names for log entries.
     */
    public boolean printMethodName = false;

    /**
     * Initializes class properties.
     *
     * @param plug            Plugin registering the formatter.
     * @param logName         Short description of this log file.
     * @param printMethodName Whether to print the calling method's name in every LogRecord.
     * @see XYCFormatter#XYCFormatter(String, String, String)
     */
    public XYCFormatter(AbstractXyPlugin plug, String logName, boolean printMethodName) { //TODO: This should be in xyc-core - Make generic interface for plugins
        this.pluginName = plug.getName();
        this.pluginVersion = plug.getDescription().getVersion();
        this.logName = logName;
        this.printMethodName = printMethodName;
    }

    /**
     * Initializes class properties.
     *
     * @param pluginName    Name of the plugin registering the formatter.
     * @param pluginVersion Version of the plugin registering the formatter.
     * @param logName       Short description of this log file.
     */
    public XYCFormatter(String pluginName, String pluginVersion, String logName) {
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.logName = logName;
    }

    /**
     * Initializes class properties.
     *
     * @param pluginName      Name of the plugin registering the formatter.
     * @param pluginVersion   Version of the plugin registering the formatter.
     * @param logName         Short description of this log file.
     * @param printMethodName Whether to print the name of the method ending a LogRecord.
     */
    public XYCFormatter(String pluginName, String pluginVersion, String logName, boolean printMethodName) {
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.logName = logName;
        this.printMethodName = printMethodName;
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Override
    public String format(LogRecord rec) {
        if (rec.getThrown() != null) {
            String traceStr = rec.getThrown().getClass().getName() + ": " + rec.getThrown().getLocalizedMessage();
            int i = 0;
            for (StackTraceElement trcEl : rec.getThrown().getStackTrace()) {
                traceStr += "\n[TRACE#" + i + "] " + trcEl.toString();
                i++;
            }
            traceStr += "\n";
            return "[" + new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss:S").format(rec.getMillis()) + "|THROWN@" + rec.getLoggerName() + "] " +
                    "@" + rec.getSourceClassName() + "#" + rec.getSourceMethodName() + traceStr;
        }
        return "[" + new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss:S").format(rec.getMillis()) + "|" + rec.getLevel() + "@" + rec.getLoggerName() + "] " + rec.getMessage() +
                ((this.printMethodName) ? "  {@" + rec.getSourceMethodName() + "}" : "") + "\n";
    }

    @Override
    public String getHead(Handler h) {
        return "******************** XYC LOG FILE ********************\n" +
                " * Start: " + (new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTimeInMillis()) + "\n" +
                " * Bukkit Version: " + Bukkit.getVersion() + "\n" +
                " * XYC Version: " + XycConstants.VERSION.toString() + "\n" +
                " * Encoding: " + h.getEncoding() + "\n" +
                " * Plugin Name: " + this.pluginName + "\n" +
                " * Plugin Version: " + this.pluginVersion + "\n" +
                " * Log Name: " + this.logName + "\n" +
                " * Log Level: " + h.getLevel().toString() + "\n" +
                " * Formatter: XYCFormatter\n" +
                "******************** XYC LOG FILE ********************\n");
    }

}
