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
