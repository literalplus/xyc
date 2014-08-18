/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without permission from the
 *  original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.sync;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.xxyy.common.xyplugin.AbstractXyPlugin;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;


/**
 * Class that helps with kicking {@link Player}s when operating in an asynchronous environment.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class SyncPlayerKicker implements Callable<Boolean> {
    private WeakReference<Player> plrRef;
    private String message;

    private SyncPlayerKicker(Player plr, String message) {
        this.plrRef = new WeakReference<>(plr);
        this.message = message;
    }

    @Override
    public Boolean call() throws Exception {
        Player plr = this.plrRef.get();
        if (plr == null) {
            System.err.println("Player turned garbage before call() could be performed for SyncPlayerKicker.");
            return false;
        }
        plr.kickPlayer(this.message);
        return true;
    }

    /**
     * Invokes a new instance on the Main Server Thread, non-blocking.
     * Use this if you don't care when the message is being sent.
     *
     * @param plr     Player to be kicked
     * @param message kick message.
     * @return Always true, for methods which want to return booleans in a single statement
     */
    public static boolean kick(Player plr, String message) {
        Bukkit.getScheduler().callSyncMethod(AbstractXyPlugin.getInstances().get(0), new SyncPlayerKicker(plr, message));
        return true;
    }
}
