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

package li.l1t.common.misc;

import java.util.Calendar;

/**
 * Simple class to manage unix timestam
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class UnixTimestamp {
    private long seconds = 0;

    private UnixTimestamp(long seconds) {
        this.seconds = seconds;
    }

    /**
     * Gets a new {@link UnixTimestamp} from a amount of milliseconds that passed since 1.1.1970.
     *
     * @param millis the amount of milliseconds since Jan 1 1970 for this timestamp
     * @return a unix timestamp object with the specified parameters
     */
    public static UnixTimestamp fromMillis(long millis) {
        return new UnixTimestamp(millis / 1000);
    }

    /**
     * Gets a new {@link UnixTimestamp} from a amount of seconds that passed since 1.1.1970.
     *
     * @param seconds the amount of seconds since Jan 1 1970 for this timestamp
     * @return a unix timestamp object with the specified parameters
     */
    public static UnixTimestamp fromSeconds(long seconds) {
        return new UnixTimestamp(seconds);
    }

    /**
     * @return the current {@link UnixTimestamp}.
     */
    public static UnixTimestamp now() {
        return new UnixTimestamp(Calendar.getInstance().getTimeInMillis() / 1000);
    }

    /**
     * @return Amount of milliseconds that passed since 1.1.1970 for this object.
     */
    public long getMillis() {
        return this.seconds * 1000;
    }

    /**
     * @return Amount of seconds that passed since 1.1.1970 for this object.
     */
    public long getSeconds() {
        return this.seconds;
    }
}
