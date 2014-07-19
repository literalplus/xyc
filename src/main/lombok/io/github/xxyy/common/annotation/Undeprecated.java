package io.github.xxyy.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicated that the target has been deprecated for unknown reason and the deprecation has
 * therefore been removed.
 * Care should be taken when using this annotation's targets.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 22.4.14
 */
@Documented
@Retention(RetentionPolicy.CLASS)
public @interface Undeprecated {
}
