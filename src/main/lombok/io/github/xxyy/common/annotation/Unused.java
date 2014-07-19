package io.github.xxyy.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This states that a method is never used and will probably removed soon-ish if no use is
 * found for it.
 * If {@link #keep()} is TRUE, this means that it is not used, but will not be removed.
 *
 * @see Deprecated
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 22.4.14
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Unused {
    boolean keep() default false;
}
