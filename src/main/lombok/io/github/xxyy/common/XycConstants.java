package io.github.xxyy.common;

import io.github.xxyy.common.version.PluginVersion;

/**
 * Class that provides some constants that are mostly outdated.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class XycConstants {
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
     * An Unicode check mark, especially useful for To<a></a>Do-lists.
     */ //Prevent IDEA from seeing this as TO_DO comment
    public static final String CHECK_MARK = "✔";
    /**
     * An Unicode ballot X, especially useful for To<a></a>Do-lists.
     */
    public static final String BALLOT_X = "✘";
}
