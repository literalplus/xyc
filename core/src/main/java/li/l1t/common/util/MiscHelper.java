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

package li.l1t.common.util;

import org.apache.commons.lang.math.RandomUtils;

/**
 * A class providing some miscellaneous static utilities.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class MiscHelper {
    /**
     * @param clazz Class object of the enum class to get an  element from
     * @param <T>   Enum class to get a random element from
     * @return a semi-random element from {@code clazz}.
     */
    public static <T extends Enum<?>> T randomEnumElement(Class<T> clazz) {
        T[] values = clazz.getEnumConstants();//each call copies an array!
        return values[RandomUtils.nextInt(values.length)];
    }

    /**
     * Inverts a comparator result, i.e. if the value passed is positive, -1 will be returned and if it's negative,
     * +1 will be returned. 0 values will be returned directly.
     *
     * @param i the comparator result to invert
     * @return the inverted comparator result of the parameter
     */
    public static int invertCompare(int i) {
        if (i == 0) {
            return i;
        }

        return i > 0 ? -1 : 1;
    }
}
