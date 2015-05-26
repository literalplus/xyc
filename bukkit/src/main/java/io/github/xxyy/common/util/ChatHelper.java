/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.util;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that provides some static methods for chat formatting and filtering
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a> and Janmm14
 */
public abstract class ChatHelper {
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    private static final Pattern NAMED_DOTS_PATTERN = Pattern.compile("(?:\\(punkt\\)|\\(dot\\)|\\s\\.\\s|\\(point\\)|\\(\\.\\)|\\.)+", Pattern.CASE_INSENSITIVE);
    //private static final Pattern GENERIC_URL_ENDING_PATTERN = Pattern.compile("(\\S+)\\.(?:[^-\\./][a-z]{1,4})", Pattern.CASE_INSENSITIVE);
    private static final Pattern STANDARD_URL_PATTERN = Pattern.compile("(\\S+)\\.(?:me|de|at|to|tk|eu|com|net|org|ly|tv|cc|gl)", Pattern.CASE_INSENSITIVE);
    private static final Pattern DOTLIKE_CHARS_PATTERN = Pattern.compile("[-_\\|~\\.+*#$&%,]+");

    private static final Pattern NOOB_PATTERN = Pattern.compile("[n][o0u\\(\\)]+[b]", Pattern.CASE_INSENSITIVE);
    private static final Pattern SPAST_PATTERN = Pattern.compile("[s][p]+[a4]+[s]+[t]", Pattern.CASE_INSENSITIVE);
    private static final Pattern HUSO_PATTERN = Pattern.compile("[h]+[u]+[r]*[e3]*[n]*[s]+[o0u\\(\\)]+[h]*[n]*", Pattern.CASE_INSENSITIVE);
    private static final Pattern WIXXA_PATTERN = Pattern.compile("[w]+[i1]+[chsx]+(?:[e3]+[r]+|[a4]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern ALOCH_PATTERN = Pattern.compile("[a4]+[r]*[s]+[c]+[h]+[l1]+[o0\\(\\)]+[c]+[h]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern SPAM_SENTENCE_ENDING_PATTERN = Pattern.compile("[?!\\.]{2,}");
    private static final Pattern PER_CENT_PATTERN = Pattern.compile("%");
    /**
     * If global mute is enabled. This is just storage and chat listeners have to implement this themselves.
     */
    public static boolean isGlobalMute = false;
    /**
     * Reason for global mute. This is just storage and chat listeners have to implement this themselves.
     */
    public static String gloMuReason = "";
    /**
     * The level of Ad detection. Values go from 0 (off) to 2 (most aggressive). To minimize failure with an acceptable rate
     * of messages tricking the filter, use 1.
     */
    public static short adDetectionLevel = 1;
    /**
     * Whether to replace point-like characters in ad detection. this includes '_', '-', etc.
     */
    public static boolean adDetectionReplacePointLikeChars = false;
    /**
     * How much of a message must be CAPITALIZED in order for it to be marked as Caps.
     */
    public static float percentForCaps = 50;
    /**
     * These characters will cause '&amp;' to become {@link ChatColor#COLOR_CHAR} if followed by one of them.
     */
    public static String allowedChatColors = "012356789AaBbDdEeFfRr";

    /**
     * Converts a string for colorizing, using '&amp;' as color char and {@link ChatHelper#allowedChatColors} for allowed colors.
     *
     * @param message Message to colorize
     * @return Colorized message
     */
    public static String convertDefaultChatColors(String message) {
        char[] b = message.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && ChatHelper.allowedChatColors.indexOf(b[i + 1]) > -1) {
                b[i] = ChatColor.COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    /**
     * Converts a string for colorizing, using '&amp;' as color char and "012356789AaBbCcDdEeFfKkRr" for allowed colors.
     *
     * @param message Message to colorize
     * @return Colorized message
     */
    public static String convertStandardColors(String message) {
        char[] b = message.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && "012356789AaBbCcDdEeFfKkRr".indexOf(b[i + 1]) > -1) {
                b[i] = ChatColor.COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    /**
     * Detects if a message is an advertisement (aka a server or internet address) based on {@link ChatHelper#adDetectionLevel}.
     *
     * @param msg Message to check
     * @return If the message is probably an ad.
     */
    public static boolean isAdvertisement(String msg) {
        if (ChatHelper.adDetectionLevel >= 2) {
            if (msg.contains(" ")) {
                if (isAdvertisement(msg.replace(" ", ""))) {
                    return true;
                }
            }
        }

        msg = msg.toLowerCase();
        msg = NAMED_DOTS_PATTERN.matcher(msg).replaceAll(".");

        if (ChatHelper.adDetectionReplacePointLikeChars) {
            msg = DOTLIKE_CHARS_PATTERN.matcher(msg).replaceAll(".");
        }

        if (IP_PATTERN.matcher(msg).find()) {
            return true;
        }

        if (ChatHelper.adDetectionLevel >= 1) {
            final Matcher matcher = STANDARD_URL_PATTERN.matcher(msg);
            while (matcher.find()) {
                String matchGroup = matcher.group();
                if (matchGroup.endsWith(ChatHelper.adDetectionReplacePointLikeChars ? "nowak.at.net" : "nowak-at.net")) {
                    matchGroup = matcher.group(1); // everything before top-level domain part
                    if (!isAllowed_internal(matchGroup, "minotopia")) {
                        return true;
                    }
                    if (!isAllowed_internal(matchGroup, ChatHelper.adDetectionReplacePointLikeChars ? "living.bots" : "living-bots")) {
                        return true;
                    }
                } else {
                    if (!isAllowed_internal(matchGroup, ChatHelper.adDetectionReplacePointLikeChars ? "nowak.at.net" : "nowak-at.net")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isAllowed_internal(String matchGroup, String allowed) {
        if (matchGroup.endsWith(allowed)) {
            matchGroup = matchGroup.substring(0, matchGroup.length() - allowed.length());
            // check if domain name without allowed ending part is still a valid url to prevent falsely allowed ads if adDetectionLevel is 2 or two urls are without a space
            if (STANDARD_URL_PATTERN.matcher(matchGroup).find()) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if a message is CAPITALIZED, based on {@link ChatHelper#percentForCaps}.
     *
     * @param msg Message to check
     * @return Whether {@link ChatHelper#percentForCaps}% of this message are CAPITALIZED.
     */
    public static boolean isCaps(String msg) {
        if (ChatHelper.percentForCaps <= 0) {
            return false;
        }
        if (msg.length() <= 5) {
            return false;
        }
        int reqCaps = (int) (msg.length() * (ChatHelper.percentForCaps / 100F));
        int capsCaught = 0;
        for (int i = 0; i < msg.length(); i++) {
            if (Character.isUpperCase(msg.charAt(i))) {
                capsCaught++;
                if (capsCaught >= reqCaps) return true;
            }
        }
        return false;
    }

    /**
     * replaces some common insults with nice replacements (german!) and some other things (for example '&lt;3').
     * Remember to ship your plugin in UTF-8 if you use this method!
     *
     * @param msg Message to use
     * @return Censored and prettified message
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static String replaceSpecialWords(String msg) {
        msg = NOOB_PATTERN.matcher(msg).replaceAll("nette Person"); // noob
        msg = SPAST_PATTERN.matcher(msg).replaceAll("sehr nette Person"); // spast
        msg = HUSO_PATTERN.matcher(msg).replaceAll("Sohn einer lieben Person"); // huso
        msg = WIXXA_PATTERN.matcher(msg).replaceAll("belebte Person"); // wixxa
        msg = ALOCH_PATTERN.matcher(msg).replaceAll("elegante Person"); // alo
        msg = SPAM_SENTENCE_ENDING_PATTERN.matcher(msg).replaceAll(".");

        msg = msg.replace("<3", "❤");
        msg = msg.replace(";)", " ツ");
        msg = msg.replace("!", "❢");
        msg = PER_CENT_PATTERN.matcher(msg).replaceFirst("%%");

        return msg;
    }

}
