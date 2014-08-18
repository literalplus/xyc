/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without permission from the
 *  original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This states that a method is never used and will probably removed soon-ish if no use is
 * found for it.
 * If {@link #keep()} is TRUE, this means that it is not used, but will not be removed.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @see Deprecated
 * @since 22.4.14
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Unused {
    boolean keep() default false;
}
