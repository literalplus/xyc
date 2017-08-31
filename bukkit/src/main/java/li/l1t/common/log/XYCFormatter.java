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

package li.l1t.common.log;

import li.l1t.common.XycConstants;
import li.l1t.common.xyplugin.AbstractXyPlugin;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


/**
 * A custom log formatter!
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @deprecated Use Log4J2 and {@link Log4JContextInitialiser} instead.
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
    public XYCFormatter(AbstractXyPlugin plug, String logName, boolean printMethodName) {
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
