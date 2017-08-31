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
