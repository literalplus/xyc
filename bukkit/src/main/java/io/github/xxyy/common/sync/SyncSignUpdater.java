/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without permission from the
 *  original author and may result in legal steps being taken.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.xxyy.common.sync;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;

import io.github.xxyy.common.xyplugin.AbstractXyPlugin;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

/**
 * Updates a Sign on the main server thread.
 *
 * @author xxyy
 */
public class SyncSignUpdater implements Callable<Void> {

    private WeakReference<Sign> signRef;
    private String[] lines;

    private SyncSignUpdater(Sign sign, String[] lines) {
        this.signRef = new WeakReference<>(sign);
        this.lines = lines;
    }

    @Override
    public Void call() throws Exception {
        Sign sign = this.signRef.get();
        if (sign == null) {
            System.err.println("Sign turned into garbage before call() could be performed for SyncSignUpdater.");
            return null;
        }
        for (int i = 0; i <= 3; i++) {
            if (lines.length < i) {
                break;
            }
            if (lines[i] != null) {
                sign.setLine(i, lines[i]);
            }
        }
        sign.update();
        return null;
    }

    /**
     * Invokes a new instance on the Main Server Thread, non-blocking. This is perfect if you know that no Bukkit API should be called asynchronously, but need to update a (lonely) Sign.
     *
     * @param sign  Sign to update
     * @param lines Lines to change, Expected size is 4 elements; Set any element to null if you ant to keep the previous line value.
     * @return Always true, for methods which want to return booleans in a single line.
     */
    public static boolean invoke(Sign sign, String[] lines) {
        Bukkit.getScheduler().callSyncMethod(AbstractXyPlugin.getInstances().get(0), new SyncSignUpdater(sign, lines));
        return true;
    }

    /**
     * Invokes a new instance on the Main Server Thread, non-blocking. This is perfect if you know that no Bukkit API should be called asynchronously, but need to update a (lonely) Sign line.
     *
     * @param sign   Sign to update
     * @param lineId Line to change
     * @param line   New content of this line
     * @return Always true, for methods which want to return booleans in a single statement.
     */
    public static boolean invoke(Sign sign, int lineId, String line) {
        String[] lines = new String[4];
        lines[lineId] = line;
        return invoke(sign, lines);
    }
}
