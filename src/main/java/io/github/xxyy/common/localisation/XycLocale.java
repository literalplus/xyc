package io.github.xxyy.common.localisation;

import io.github.xxyy.common.XyHelper;
import io.github.xxyy.common.util.CommandHelper;
import org.bukkit.configuration.file.YamlConfiguration;

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

    @Override
    public String getName() {
        return "XYC-Core";
    }

    /**
     * Initialises localisation, loads files from JAR if they don't exist.
     */
    public XycLocale() {
        HashMap<String, YamlConfiguration> map = new HashMap<>();
        for (String lang : this.getShippedLocales()) {
            String dir = "plugins/XYC/lang/";
            String fl = lang + ".lng.yml";
            File destFl = new File(dir + fl);
            File destDir = new File(dir);
            if (destFl.exists()) {
                map.put(lang, YamlConfiguration.loadConfiguration(destFl));
            } else {
                try {
                    destDir.mkdirs();
                    destFl.createNewFile();
                    FileOutputStream out = new FileOutputStream(destFl);
                    InputStream in = XyHelper.getPlugins().get(0).getResource("xyc_lang/" + lang + ".lng.yml");
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
                String fl = lang + ".lng.yml";
                File destFl = new File(dir, fl);
                File destDir = new File(dir);
                try {
                    destDir.mkdirs();
                    destFl.createNewFile();
                    FileOutputStream out = new FileOutputStream(destFl);
                    InputStream in = XyHelper.getPlugins().get(0).getResource("xyc_lang/" + lang + ".lng.yml");
                    int read = -1;
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
                LangHelper.reloadLang(XyHelper.getPlugins().get(0), lang);
            }

            LangHelper.langCache.put("XYC", map);
        } catch (Exception e) {
            CommandHelper.sendMessageToOpsAndConsole("§c[MTC] Could not reset languages from JAR!");
            e.printStackTrace();
        }
    }

    /**
     * Localises a String using the internal language files.
     *
     * @param id         language key
     * @param senderName name of receiver
     * @return Localised String or <code>id</code> on failure.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     * @see LangHelper#localiseString(String, String, String)
     */
    public static String getString(String id, String senderName) {
        return LangHelper.localiseString(id, senderName, "XYC");
    }
}
