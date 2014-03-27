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
}
