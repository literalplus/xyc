/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.util.config;

import com.google.common.base.Preconditions;
import li.l1t.common.exception.YamlLoadException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

/**
 * Static utility class to help make {@link org.bukkit.configuration.file.YamlConfiguration} at
 * least a little bit more intuitive.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-23
 */
public class YamlHelper {
    private YamlHelper() {

    }

    /**
     * Loads a yaml configuration from a file, actually throwing errors as they occur.
     *
     * @param file      the file to load from
     * @param mustExist whether to throw an exception if the file does not exist
     *                  (ignored otherwise)
     * @return the loaded configuration
     * @throws java.io.FileNotFoundException if the file does not exist and {@code mustExist} is true
     * @throws IOException                   if there was an error reading the file
     * @throws InvalidConfigurationException if the configuration syntax is invalid
     */
    public static YamlConfiguration load(File file, boolean mustExist) throws IOException,
            InvalidConfigurationException {
        Preconditions.checkNotNull(file, "file");
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (FileNotFoundException e) {
            if (mustExist) {
                throw e;
            }
        }
        return config;
    }

    /**
     * Loads a yaml configuration from a reader, actually throwing errors as they occur.
     *
     * @param reader the reader to load from
     * @return the loaded configuration
     * @throws IOException                   if there was an error reading the reader
     * @throws InvalidConfigurationException if the configuration syntax is invalid
     */
    public static YamlConfiguration load(Reader reader) throws IOException,
            InvalidConfigurationException {
        Preconditions.checkNotNull(reader, "reader");
        YamlConfiguration config = new YamlConfiguration();
        config.load(reader);
        return config;
    }

    /**
     * Attempts to load a yaml configuration from a file, wrapping any thrown errors in an
     * unchecked wrapper exceptions. The wrapper has access to the configuration object.
     *
     * @param file      the file to load from
     * @param mustExist whether to throw an exception if the file doesn't exist (ignored otherwise)
     * @return the loaded configuration
     * @throws YamlLoadException if an error occurs reading the configuration or the
     *                           configuration syntax is invalid. If {@code mustExist} is true,
     *                           also if the file does not exist. The cause will be set to
     *                           {@link IOException}, {@link InvalidConfigurationException}
     *                           or {@link FileNotFoundException}, respectively.
     */
    public static YamlConfiguration tryLoad(File file, boolean mustExist) {
        Preconditions.checkNotNull(file, "file");
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (FileNotFoundException e) {
            if (mustExist) {
                throw new YamlLoadException(e, config);
            }
        } catch (IOException | InvalidConfigurationException e) {
            throw new YamlLoadException(e, config);
        }
        return config;
    }

    /**
     * Attempts to load a yaml configuration from a reader, wrapping any thrown errors in an
     * unchecked wrapper exceptions. The wrapper has access to the configuration object.
     *
     * @param reader the reader to load from
     * @return the loaded configuration
     * @throws YamlLoadException if an error occurs reading the configuration, or the
     *                           configuration syntax is invalid. The cause will be set to
     *                           {@link IOException} or {@link InvalidConfigurationException},
     *                           respectively.
     */
    public static YamlConfiguration tryLoad(Reader reader) {
        Preconditions.checkNotNull(reader, "reader");
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(reader);
        } catch (IOException | InvalidConfigurationException e) {
            throw new YamlLoadException(e, config);
        }
        return config;
    }
}
