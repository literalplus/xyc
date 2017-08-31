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
import org.bukkit.command.CommandSender;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;


/**
 * Class that helps with sending messages to a {@link CommandSender} when operating in an asynchronous environment.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class SyncCommandSenderMessage implements Callable<Boolean> {
    private WeakReference<CommandSender> senderRef;
    private String message;

    private SyncCommandSenderMessage(CommandSender sender, String message) {
        this.senderRef = new WeakReference<>(sender);
        this.message = message;
    }

    /**
     * Invokes a new instance on the Main Server Thread, non-blocking.
     * Use this if you don't care when the message is being sent.
     *
     * @param sender  Sender to be messaged
     * @param message Message to send
     * @return Always true, for methods which want to return booleans in a single statement.
     */
    public static boolean send(CommandSender sender, String message) {
        Bukkit.getScheduler().callSyncMethod(AbstractXyPlugin.getInstances().get(0), new SyncCommandSenderMessage(sender, message));
        return true;
    }

    @Override
    public Boolean call() throws Exception {
        CommandSender sender = this.senderRef.get();
        if (sender == null) {
            System.err.println("CommandSender turned garbage before run() could be performed for SyncCommandMessage.");
            return false;
        }
        sender.sendMessage(this.message);
        return true;
    }
}
