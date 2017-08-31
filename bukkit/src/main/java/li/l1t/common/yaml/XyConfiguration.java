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

package li.l1t.common.yaml;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import li.l1t.common.xyplugin.XyPluggable;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-09
 */
public class XyConfiguration extends YamlConfiguration {
    private static final Consumer<XyConfiguration> NOOP_CONSUMER = (mc) -> {
    }; //formatter puts this on the next line and I can't really blame it tbh
    private final File file;
    private Consumer<XyConfiguration> loadHandler = NOOP_CONSUMER;
    private Consumer<XyConfiguration> saveHandler = NOOP_CONSUMER;
    private Exception error;

    protected XyConfiguration(File file) {
        this.file = file;
    }

    /**
     * Creates a new {@link XyConfiguration}, loading from the given file. <p> Any errors
     * loading the Configuration will be logged and available at {@link #getError()}. If the
     * specified input is not a valid config, a blank config will be returned. </p> The encoding
     * used may follow the system dependent default.
     *
     * @param file the input file
     * @return the resulting configuration
     * @throws IllegalArgumentException if file is null or it didn't exist and couldn't be created
     */
    public static XyConfiguration fromFile(File file) {
        Validate.notNull(file, "file cannot be null");
        ensureReadable(file);

        XyConfiguration config = new XyConfiguration(file);
        config.tryLoad();

        return config;
    }

    /**
     * Ensures that given file and its parent directories exist, and that it can be read by the
     * application.
     *
     * @param file the file to check
     */
    protected static void ensureReadable(File file) {
        if (!file.exists()) {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                throw new IllegalArgumentException("Could not create managed config file's parent dirs for some reason: " + file.getAbsolutePath());
            }
            try {
                Files.touch(file);
            } catch (IOException e) {
                throw new IllegalStateException("Caught IOException", e);
            }
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException("Config file is not readable: " + file.getAbsolutePath());
        }
    }

    /**
     * Creates a new {@link XyConfiguration}, loading from the given file path relative to the
     * given plugin's data folder. <p> Any errors loading the Configuration will be logged and
     * available at {@link #getError()}. If the specified input is not a valid config, a blank
     * config will be returned. </p> The encoding used may follow the system dependent default.
     *
     * @param filePath the input file path, relative to the plugin's data folder
     * @param plugin   the plugin whose data folder to use
     * @return the resulting configuration
     * @throws IllegalArgumentException Thrown if file is null or it didn't exist and couldn't be created
     */
    public static XyConfiguration fromDataFolderPath(String filePath, Plugin plugin) {
        File file = new File(plugin.getDataFolder(), filePath);
        return fromFile(file);
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        setError(null);
        super.loadFromString(contents);
        loadHandler.accept(this);
    }

    @Override
    public String saveToString() {
        String result = super.saveToString();
        saveHandler.accept(this);
        return result;
    }

    /**
     * Saves this configuration to its corresponding file.
     *
     * @throws IOException if an error occurs saving the file
     */
    public void save() throws IOException {
        save(file);
    }

    /**
     * Tries to save this configuration to a string and then save it to the associated file in an
     * async thread. Any exceptions that might occur will be ignored and logged to the plugin's
     * logger.
     *
     * @param plugin the plugin to use for interacting with Bukkit's scheduler
     */
    public void asyncSave(XyPluggable plugin) {
        Validate.notNull(file, "File cannot be null");

        try {
            com.google.common.io.Files.createParentDirs(file); //We're using the other Files class in this file too
        } catch (IOException e) {
            logError("Unable to create parent dirs for " + file.getAbsolutePath() + ":", e);
            setError(e);
            return; //We're not throwing the other exception so we might as well swallow this one
        }

        String data = saveToString();

        if (plugin.isEnabled()) {
            plugin.tasks().async(() -> internalSave(plugin, data));
        } else {
            internalSave(plugin, data);
        }

    }

    private void logError(String msg, Exception e) {
        Bukkit.getLogger().log(Level.WARNING, msg, e);
    }

    private void internalSave(Plugin plugin, String data) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8)) {
            writer.write(data);
        } catch (IOException e) {
            logError("Unable to save managed config to " + file.getAbsolutePath() + ":", e);
            setError(e);
        }
    }

    /**
     * Attempts to save this configuration to its file and logs any error. Also stores the error to {@link #getError()}.
     *
     * @return whether an error occurred
     */
    public boolean trySave() {
        try {
            save();
        } catch (IOException e) {
            logError("Couldn't save managed configuration to " + file.getAbsolutePath() + "!", e);
            setError(e);
            return false;
        }
        return true;
    }


    /**
     * Attempts to load this configuration from its file and logs any error. Also stores the error to {@link
     * #getError()}.
     *
     * @return whether an error occurred
     */
    public boolean tryLoad() {
        try {
            load(file);
        } catch (FileNotFoundException ex) {
            Bukkit.getLogger().info(String.format("Attempted to load configuration from %s, but file didn't exist!", file.getAbsolutePath()));
            setError(ex);
            return false;
        } catch (IOException ex) {
            logError("Cannot load " + file, ex);
            setError(ex);
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc} <p> This method also tries to save a backup file if the loaded file contains
     * any syntax errors and will print the location to console.
     *
     * @param file the file to load from
     * @throws IOException If an error occurs loading the file
     */
    public void load(File file) throws IOException {
        if(!getFile().exists()) {
            Files.touch(getFile());
        }
        try {
            super.load(file);
        } catch (InvalidConfigurationException ex) { //Handle backups
            try {
                File backupFile = new File(file.getParentFile().getAbsolutePath(), file.getName() + ".mtcbak");
                Files.copy(file, backupFile); //We're using the other Files class in this class too
                logError(String.format("Invalid configuration syntax detected for %s! Backup is available at %s",
                        file, backupFile.getName()), ex);
            } catch (IOException e) {
                logError("Failed to save backup for invalid configuration file at " + file + "!", e);
                logError("YAMl error: ", ex);
            }
            setError(ex);
        }
    }

    /**
     * @return the exception encountered while loading this configuration, if any
     */
    public Exception getError() {
        return error;
    }

    protected void setError(Exception error) {
        this.error = error;
    }

    public void setLoadHandler(Consumer<XyConfiguration> loadHandler) {
        this.loadHandler = loadHandler;
    }

    public void setSaveHandler(Consumer<XyConfiguration> saveHandler) {
        this.saveHandler = saveHandler;
    }

    public File getFile() {
        return file;
    }

    /**
     * Retrieves a list of strings from this configuration, it it exists. Returns an empty list
     * otherwise.
     *
     * @param configPath the path of the list
     * @return the list, or the defaults, if it was created
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getListChecked(String configPath, Class<? extends T> listType) {
        List<?> anyList = getList(configPath);
        if (anyList == null) {
            return new ArrayList<>();
        }
        Preconditions.checkArgument(
                anyList.stream().allMatch(entry -> listType.isAssignableFrom(entry.getClass())),
                "config list %s is of wrong type: expected %s",
                configPath, listType
        );
        return (List<T>) anyList;
    }

    /**
     * Finds the value at given path.
     *
     * @param path the path to look up
     * @return an optional containing the value, or an empty optional if the path does not exist or the value is null
     */
    public Optional<Object> find(String path) {
        Preconditions.checkNotNull(path, "path");
        return Optional.ofNullable(get(path));
    }

    /**
     * Finds the value at given path if it is of given type.
     *
     * @param path the path to look up
     * @param type the type of object to retrieve
     * @param <T>  the type of object to retrieve
     * @return an optional containing the object at given path that's of given type, or an empty optional if the object
     * is not present, null, or of a different type
     */
    public <T> Optional<T> findTyped(String path, Class<T> type) {
        Preconditions.checkNotNull(type, "type");
        return find(path)
                .filter(obj -> type.isAssignableFrom(obj.getClass()))
                .map(type::cast);
    }

    /**
     * Finds the integer value at given path.
     *
     * @param path the path to look up
     * @return an optional containing the {@link Number#intValue() converted} number at given path, or an empty optional
     * of the object is not present, null, or not a {@link Number number}
     */
    public Optional<Integer> findInt(String path) {
        return findTyped(path, Number.class).map(Number::intValue);
    }

    /**
     * Finds the double value at given path.
     *
     * @param path the path to look up
     * @return an optional containing the {@link Number#doubleValue() converted} number at given path, or an empty
     * optional of the object is not present, null, or not a {@link Number number}
     */
    public Optional<Double> findDouble(String path) {
        return findTyped(path, Number.class).map(Number::doubleValue);
    }
}
