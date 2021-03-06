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

package li.l1t.common.localisation;

import com.google.common.io.Files;
import li.l1t.common.XyHelper;
import li.l1t.common.util.Closer;
import li.l1t.common.util.CommandHelper;
import li.l1t.common.xyplugin.AbstractXyPlugin;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that provides some static methods to deal with localising XyPlugins.
 */
public class LangHelper {
    public static final String LANG_FILE_EXTENSION = ".lng.yml";
    public static final Pattern PATTERN = Pattern.compile("`([^` ]+)`");
    static HashMap<String, HashMap<String, YamlConfiguration>> langCache = new HashMap<>();
    @SuppressWarnings("unused")
    private static HashMap<String, String> chosenLangCache = new HashMap<>();

    static {
        XyHelper.getLocale(); //Ensure that class is loaded and the global locale is registered
    }

    /**
     * Parses a String so that a) {@link ChatColor} is parsed with '&amp;' instead of the paragraph sign b) html entities are replaced with their unicode representations.
     *
     * @param str String to process
     * @return the parsed string
     */
    public static String applyCodes(String str) {
        return ChatColor.translateAlternateColorCodes('&', StringEscapeUtils.unescapeHtml(str));
    }

    /**
     * Clears the language cache for a plugin, so that language files will be reloaded.
     *
     * @param pluginId The plugin providing String of the target plugin
     */
    public static void clearPluginLangCache(String pluginId) {
        if (!LangHelper.langCache.containsKey(pluginId)) {
            return;
        }
        LangHelper.langCache.remove(pluginId);
    }

    private static void updateLanguageFile(FileConfiguration cfg, FileConfiguration defaultCfg, File file, AbstractXyPlugin plug) {
        Map<String, Object> newEntryMap = defaultCfg.getValues(true);
        newEntryMap.keySet().removeAll(cfg.getValues(true).keySet());//changes on key set are reflected to map.

        if (!newEntryMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : newEntryMap.entrySet()) {
                if (!cfg.contains(entry.getKey())) {
                    cfg.set(entry.getKey(), entry.getValue());
                }
            }
            try {
                cfg.save(file);// vv Will not be printed in case of exception
                CommandHelper.sendMessageToOpsAndConsole("§a[" + plug.getName() + "] Your language files were outdated and"
                        + " have automagically been updated for you. You may want to check if the changes reflect your opinion :)");
                System.out.println("Updated Language Nodes: " + newEntryMap.keySet());
            } catch (IOException e) {
                System.out.println("Could not save modified language file. That's an error.");
            }
        }
    }

    /**
     * loc &amp; pl are normally the same object. Files will be pulled from /lang/*.lng.yml folder in JAR languages are defined by getShippedLocales()
     *
     * @param loc    The localization object to pull the names from
     * @param plugin The plugin to copy the files from
     */
    public static void copyLangsFromJar(XyLocalizable loc, AbstractXyPlugin plugin) {
        HashMap<String, YamlConfiguration> languages = new HashMap<>();

        for (String language : loc.getShippedLocales()) {
            try {
                File destinationFile = new File(plugin.getDataFolder().getAbsolutePath() + "/lang/", language + LANG_FILE_EXTENSION);
                Files.createParentDirs(destinationFile);
                YamlConfiguration loadedCfg = YamlConfiguration.loadConfiguration(destinationFile);

                if (destinationFile.exists()) {
                    InputStream in = plugin.getResource("lang/" + language + LANG_FILE_EXTENSION);
                    YamlConfiguration defaultCfg = YamlConfiguration.loadConfiguration(in);
                    Closer.close(in);
                    updateLanguageFile(loadedCfg, defaultCfg, destinationFile, plugin);
                    languages.put(language, loadedCfg);
                } else {
                    if (!destinationFile.createNewFile()) {
                        throw new IOException("Could not create new language file: " + destinationFile.getAbsolutePath());
                    }
                    InputStream in;
                    try (FileOutputStream out = new FileOutputStream(destinationFile)) {
                        in = plugin.getResource("lang/" + language + LANG_FILE_EXTENSION);
                        int read;
                        while ((read = in.read()) != -1) {
                            out.write(read);
                        }
                        out.flush();
                    }
                    in.close();
                    languages.put(language, YamlConfiguration.loadConfiguration(destinationFile));
                }
            } catch (Exception exc) {
                plugin.getLogger().log(Level.WARNING, "Could not copy localization files from JAR: " + plugin.getName() + "/" + language, exc);
            }
        }
        LangHelper.langCache.put(plugin.getName(), languages);
    }

    /**
     * Gets the {@link YamlConfiguration} object that represents a specific language file of a specific plugin.
     *
     * @param lang     the language id to get the file for
     * @param pluginId the id of the plugin to get the file for
     * @return {@link YamlConfiguration} or {@code null} if not found.
     */
    public static YamlConfiguration getLangFile(String lang, String pluginId) {
        if (!LangHelper.langCache.containsKey(pluginId)) {
            return null;
        }
        return LangHelper.langCache.get(pluginId).get(lang);
    }

    /**
     * Gets an option from the language metadata provided at YAML node "info".
     *
     * @param option   the option to get (i.e. "info.OPTION")
     * @param lang     The language file to use.
     * @param pluginId The plugin of the file.
     * @return The option's value or "XYC-notexists"/"XYC-notexists2"
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static String getOption(String option, String lang, String pluginId) {
        if (!LangHelper.langCache.containsKey(pluginId)) {
            return "XYC-notloaded";
        }
        YamlConfiguration cfg = LangHelper.langCache.get(pluginId).get(lang);
        if (cfg == null) {
            return "XYC-notexists";
        }
        return cfg.getString("info." + option, "XYC-notexists2");
    }

//    /**
//     * Gets the lang {@code senderName} has chosen for all messages; Only returns {@link XyHelper#defaultLang} for now.
//     *
//     * @param senderName Name of the {@link CommandSender} whose lang will be fetched.
//     */
//    public static String getSenderChosenLang(String senderName) {
//        return XyHelper.defaultLang;
//    }

    /**
     * Returns a localised String.
     *
     * @param locId      Key to look for. (actually, the valuze of "lang.KEY" will be returned)
     * @param senderName Who will receive this message (for choosing languages) or "CONSOLE" to choose {@link XyHelper#defaultLang}.
     * @param pluginId   The plugin providing this file
     * @return A localised string or {@code locId} on failure.
     */
    public static String localiseString(String locId, @SuppressWarnings("UnusedParameters") String senderName, String pluginId) {
        String chLang = XyHelper.defaultLang;
        HashMap<String, YamlConfiguration> lMap = LangHelper.langCache.get(pluginId);

        if (lMap == null) {
            System.err.println("Error: No such plugin: " + pluginId + " @ LangHelper#localiseString");
            return locId;
        }

        if (!lMap.containsKey(chLang)) {
            chLang = XyHelper.defaultLang;
            if (!lMap.containsKey(chLang)) {
                return locId;
            }
        }

        String result = LangHelper.applyCodes(lMap.get(chLang).getString("lang." + locId, locId));
        Matcher matcher = PATTERN.matcher(result);
        StringBuffer buf = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(buf, localiseString(matcher.group(1), senderName, pluginId));
        }

        matcher.appendTail(buf);

        return buf.toString();
    }

    /**
     * Prints a full dump of the keys loaded for this file and plugin to {@link System#out}.
     *
     * @param lang the language key to dump
     * @param pl   the plugin id to dump
     */
    public static void printDebug(String pl, String lang) {
        HashMap<String, YamlConfiguration> map = LangHelper.langCache.get(pl);
        System.out.println(map.get(lang).toString());
        for (String key : map.get(lang).getKeys(true)) {
            System.out.println(key);
        }
    }

    /**
     * Reloads a language file for this plugin. Prints a message to {@link System#out} on failure.
     *
     * @param pl  Plugin to use
     * @param lng File to reload
     */
    public static void reloadLang(AbstractXyPlugin pl, String lng) {
        try {
            HashMap<String, YamlConfiguration> hm = LangHelper.langCache.get(pl.getName());
            if (hm == null) {
                CommandHelper.sendMessageToOpsAndConsole("§4[XYC] WARNING: Could not reload " + lng + " from "
                        + pl.getName() + ": No such plugin!");
                return;
            }
            YamlConfiguration cfg = hm.get(lng);
            if (cfg == null) {
                CommandHelper.sendMessageToOpsAndConsole("§4[XYC] WARNING: Could not reload " + lng + " from "
                        + pl.getName() + ": No such language!");
                return;
            }
            cfg.load("plugins/XYC/lang/" + pl.getName() + "/" + lng + LANG_FILE_EXTENSION);
        } catch (IOException | InvalidConfigurationException | NullPointerException e) {
            CommandHelper.sendMessageToOpsAndConsole("§4[XYC] WARNING: Could not reload " + lng + " from "
                    + pl.getName());
            e.printStackTrace();
        }
    }

    /**
     * Localises a String and sends it to
     * {@code sender}, prefixed with the result of getChatPrefix() for
     * {@code plug}, using getName() of
     * {@code plug} as plugin key.
     *
     * @param locId  the message id to send
     * @param sender the receiver of the message
     * @param plug   the plugin to get the message for
     * @see LangHelper#localiseString(String, String, String)
     */
    public static void sendLocalizedStringWithPrefix(String locId, CommandSender sender, AbstractXyPlugin plug) {
        String str = LangHelper.localiseString(locId, sender.getName(), plug.getName());
        if (!(str == null || str.isEmpty())) {
            sender.sendMessage(plug.getChatPrefix() + str);
        }
    }
}
