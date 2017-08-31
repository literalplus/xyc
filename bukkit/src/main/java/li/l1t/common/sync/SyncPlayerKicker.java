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

package li.l1t.common.sync;

import li.l1t.common.xyplugin.AbstractXyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
}
