package io.github.xxyy.common.util;

import org.apache.commons.lang.math.RandomUtils;

/**
 * A class providing some miscellaneous static utilities.
 * 
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class MiscHelper
{
    /**
     * @return a semi-random element from {@code clazz}.
     */
    public static <T extends Enum<?>> T randomEnumElement(Class<T> clazz){
        T[] values = clazz.getEnumConstants();//each call copies an array!
        return values[RandomUtils.nextInt(values.length)];
    }

    /**
     * Inverts a comparator result, i.e. if the value passed is positive, -1 will be returned and if it's negative,
     * +1 will be returned. 0 values will be returned directly.
     * @param i the comparator result to invert
     * @return the inverted comparator result of the parameter
     */
    public static int invertCompare(int i) {
        if(i == 0) {
            return i;
        }

        return i > 0 ? -1 : 1;
    }
}
