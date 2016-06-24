/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.exception;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Thrown if an error occurs loading a yaml configuration.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-23
 */
public class YamlLoadException extends RuntimeException {
    private final YamlConfiguration configuration;

    public YamlLoadException(String message, Throwable cause, YamlConfiguration configuration) {
        super(message, cause);
        this.configuration = configuration;
    }

    public YamlLoadException(String message, YamlConfiguration configuration) {
        super(message);
        this.configuration = configuration;
    }

    public YamlLoadException(Throwable cause, YamlConfiguration configuration) {
        super(cause);
        this.configuration = configuration;
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }
}
