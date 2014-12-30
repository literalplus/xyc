/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.log;

import io.github.xxyy.common.xyplugin.AbstractXyPlugin;
import io.github.xxyy.common.xyplugin.GenericXyPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A simple LogHelper.
 * It is intended that you create static Loggers with Getters (and - if needed - Setters)
 * and initialize them in {@link LogHelper#initLoggers()}.
 * To initialize logging, you would call the following in you Plugin's {@link GenericXyPlugin#onEnable()}:
 * {@code (class extends LogHelper).initLoggers();}
 * (reference will not be needed any more)
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class LogHelper { //TODO this shouldn't be static and accept a plugin instance. And the JavaDoc is um..bad

    protected static LogHelper instance;
    private List<Logger> loggers = new ArrayList<>();

    /**
     * Override this method with logic to initialize your loggers using {@link LogHelper#initLogger(Logger, String, Formatter)}.
     */
    public abstract void initLoggers();

    /**
     * <p>Flushes all loggers an then releases the
     * associated file locks.
     * </p>
     * <p>Especially helpful when disabling your
     * plugin.
     * </p>
     * <b>Heads up!</b> Other plugins may invoke
     * this method too, so be careful when using
     * your loggers in {@link AbstractXyPlugin#disable()}.
     */
    public static void flushAndRelease() {
        if (LogHelper.instance == null) {
            System.out.println("TDLogHelper: instance is null.");
            return;
        }
        if (LogHelper.instance.loggers == null) return; //Loggers have already been flushed
        Iterator<Logger> it = LogHelper.instance.loggers.iterator();
        while (it.hasNext()) {
            Logger lgr = it.next();
            lgr.log(Level.INFO, "TDLogHelper: Closing and flushing logger..");
            for (Handler handler : lgr.getHandlers()) {
                handler.flush();
                handler.close();
                lgr.removeHandler(handler);
            }
            it.remove();
        }
    }

    /**
     * Tries to initialize a new logger.
     * Prints a message to console in case of failure.
     *
     * @param lgr        Logger to initialize
     * @param fileName   file to write to (creates a new {@link FileHandler})
     * @param formatter  formatter used to format the log (preferably {@link XYCFormatter})
     * @param loggerName the nice name of the logger
     * @see LogHelper#initLogger(java.util.logging.Logger, String, java.util.logging.Formatter)
     */
    protected void tryInitLogger(Logger lgr, String fileName, String loggerName, Formatter formatter) {
        try {
            this.initLogger(lgr, fileName, formatter);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">>XYC exception when tryin to initialize " + loggerName + ".");
        }
    }

    /**
     * Initializes a new logger.
     *
     * @param lgr       Logger to initialize
     * @param fileName  file to write to (creates a new {@link FileHandler})
     * @param formatter formatter used to format the log (preferably {@link XYCFormatter})
     * @throws Exception Various things
     * @see LogHelper#tryInitLogger(Logger, String, String, Formatter)
     */
    void initLogger(Logger lgr, String fileName, Formatter formatter) throws Exception {
        if (LogHelper.instance == null) {
            LogHelper.instance = this;
        }
        //noinspection ResultOfMethodCallIgnored
        (new File(fileName)).getParentFile().mkdirs(); //REFACTOR   Result of File.mkdirs() is ignored at line 76
        FileHandler hdlr = new FileHandler(fileName);
        hdlr.setLevel(Level.FINEST);
        hdlr.setFormatter(formatter);
        hdlr.setEncoding("UTF-8");
        lgr.addHandler(hdlr);
        lgr.setLevel(Level.FINEST);
        this.loggers.add(lgr);
    }
}
