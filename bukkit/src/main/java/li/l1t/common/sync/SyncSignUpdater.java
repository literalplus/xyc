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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package li.l1t.common.sync;

import li.l1t.common.xyplugin.AbstractXyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;

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
}
