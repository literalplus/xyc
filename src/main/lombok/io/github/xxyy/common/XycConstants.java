package io.github.xxyy.common;

import io.github.xxyy.common.version.PluginVersion;

/**
 * Class that provides some constants that are mostly outdated.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class XycConstants {
    /**
     * A full version string.
     */
    public static final PluginVersion VERSION = PluginVersion.ofClass(XycConstants.class);
    /**
     * short name of XYC.
     */
    public static final String pluginID = "XYC";//not actually a plugin
    /**
     * XYC chat prefix.
     */
    public static String chatPrefix = "§7[§8XYC§7] ";
    /**
     * Codename of this version.
     */
    public static final String versionName = "Albert";

    /**
     * An Unicode check mark, especially useful for ToDo-lists.
     */
    public static final String CHECK_MARK = "✔";
    /**
     * An Unicode ballot X, especially useful for ToDo-lists.
     */
    public static final String BALLOT_X = "✘";
}
