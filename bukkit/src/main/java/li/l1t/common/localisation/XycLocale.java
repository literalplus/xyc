/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.localisation;

import li.l1t.common.internal.CommonPlugin;
import li.l1t.common.util.CommandHelper;
import li.l1t.common.util.FileHelper;
import li.l1t.common.xyplugin.AbstractXyPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Deals with internal localisation of XYC.
 *
 * @author xxyy
 */
public class XycLocale implements XyLocalizable {

    /**
     * Initialises localisation, loads files from JAR if they don't exist.
     */
    public XycLocale() {
        HashMap<String, YamlConfiguration> map = new HashMap<>();
        for (String lang : this.getShippedLocales()) {
            String dir = "plugins/XYC/lang/";
            String fl = lang + LangHelper.LANG_FILE_EXTENSION;
            File destFl = new File(dir + fl);
            File destDir = new File(dir);
            if (destFl.exists()) {
                map.put(lang, YamlConfiguration.loadConfiguration(destFl));
            } else {
                try {
                    FileHelper.mkdirsWithException(destDir);
                    //noinspection ResultOfMethodCallIgnored
                    destFl.createNewFile();
                    FileOutputStream out = new FileOutputStream(destFl);
                    JavaPlugin plugin = CommonPlugin.instance();
                    if(plugin == null) { //if we are not standalone, we're probably shipped
                        plugin = AbstractXyPlugin.getInstances().get(0);
                    }
                    InputStream in = plugin.getResource("xyc_lang/" + lang + LangHelper.LANG_FILE_EXTENSION);
                    int read;
                    while ((read = in.read()) != -1) {
                        out.write(read);
                    }
                    out.flush();
                    out.close();
                    in.close();
                    map.put(lang, YamlConfiguration.loadConfiguration(destFl));

                } catch (Exception e) {
                    System.out.println("Could not copy XYC localization files from JAR: " + lang);
                    e.printStackTrace();
                }
            }
        }
        LangHelper.langCache.put("XYC", map);
    }

    /**
     * Localises a String using the internal language files.
     *
     * @param id         language key
     * @param senderName name of receiver
     * @return Localised String or {@code id} on failure.
     * @see LangHelper#localiseString(String, String, String)
     */
    public static String getString(String id, String senderName) {
        return LangHelper.localiseString(id, senderName, "XYC");
    }

    @Override
    public String getName() {
        return "XYC-Core";
    }

    @Override
    public String[] getShippedLocales() {
        return new String[]{"de_AT", "en_US"};
    }

    /**
     * Resets all XYC language files.
     */
    public void resetLang() {
        try {
            HashMap<String, YamlConfiguration> map = new HashMap<>();
            for (String lang : (this.getShippedLocales())) {
                String dir = "plugins/XYC/lang/";
                String fl = lang + LangHelper.LANG_FILE_EXTENSION;
                File destFl = new File(dir, fl);
                try {
                    FileHelper.mkdirsWithException(new File(dir));
                    //noinspection ResultOfMethodCallIgnored
                    destFl.createNewFile();
                    FileOutputStream out = new FileOutputStream(destFl);
                    InputStream in = AbstractXyPlugin.getInstances().get(0).getResource("xyc_lang/" + lang + LangHelper.LANG_FILE_EXTENSION);
                    int read;
                    while ((read = in.read()) != -1) {
                        out.write(read);
                    }
                    out.flush();
                    out.close();
                    in.close();
                    map.put(lang, YamlConfiguration.loadConfiguration(destFl));

                } catch (Exception e) {
                    System.out.println("[MTC]Could not reset localization files from JAR: " + lang);
                    e.printStackTrace();
                }
                LangHelper.reloadLang(AbstractXyPlugin.getInstances().get(0), lang);
            }

            LangHelper.langCache.put("XYC", map);
        } catch (Exception e) {
            CommandHelper.sendMessageToOpsAndConsole("§c[MTC] Could not reset languages from JAR!");
            e.printStackTrace();
        }
    }
}
