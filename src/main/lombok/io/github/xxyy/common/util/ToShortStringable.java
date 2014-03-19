package io.github.xxyy.common.util;

/**
 * Interface for objects that provide a method to
 * turn it into a shorter String for display.
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public interface ToShortStringable {
    /**
     * @return A short String representation of this object.
     * @see Object#toString()
     */
    public String toShortString();
}
