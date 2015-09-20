/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.lib.com.mojang.api.profiles;

/**
 * Represents a Mojang user's name at a certain time.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 20/09/15
 */
public class NameData {
    private String name;
    private long changedToAt;

    /**
     * @return the properly-cased name represented by this object
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the amount of milliseconds elapsed since the beginning of the Unix epoch at the time this name was
     * changed to.
     *
     * @return the amount or null if this is the initial name of the account
     */
    public long getChangedToAt() {
        return changedToAt;
    }

    public void setChangedToAt(long changedToAt) {
        this.changedToAt = changedToAt;
    }
}
