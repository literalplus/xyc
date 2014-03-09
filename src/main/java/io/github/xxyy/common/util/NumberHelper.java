package io.github.xxyy.common.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

/**
 * Class that provides some static methods to deal with numbers.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class NumberHelper {

    static {
        DecimalFormatSymbols syms = new DecimalFormatSymbols(Locale.GERMAN);
        syms.setGroupingSeparator(' ');
        syms.setDecimalSeparator(' ');//hacky!
        SPACE_FORMAT = new DecimalFormat(",###", syms);
    }
    private static final DecimalFormat SPACE_FORMAT;
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    /**
     * Formats a number with spaces as group seperator for use on signs. The number will be colored in black, if it's to short to actually
     * automatically be aligned right, it will be prefixed with grey underscores. Possible outputs: &7________&0123 123456789012345
     *
     * @param number The number to be format* @author <a href="http://xxyy.github.io/">xxyy</a>xxyy98@gmail.com>
     */
    public static String formatForSignRightAligned(int number) {
//        String rtrn = NumberFormat.getInstance(Locale.GERMAN).format(number);
        String rtrn = NumberHelper.SPACE_FORMAT.format(number);
        if (rtrn.length() > 10) {
            return rtrn;
        }
        return "§7" + StringUtils.leftPad("§0" + rtrn, 13, '_');
    }

    /**
     * Tries to extract an int from a String. If the extraction fails (i.e. if a {@link NumberFormatException} is thrown by
     * {@link Integer#parseInt(String)}),
     * <code>def</code> is returned.
     *
     * @param source String to get the int from.
     * @param def    int to return if <code>source</code> does not contain a valid int.
     *
     * @return int contained in <code>source</code> or <code>def</code>.
     * @* @author <a href="http://xxyy.github.io/">xxyy</a>* @author xxyy98<xxyy98@gmail.com>
     */
    public static int tryParseInt(final String source, final int def) {
        int result;
        try {
            result = Integer.parseInt(source);
        } catch (NumberFormatException e) {
            return def;
        }
        return result;
    }

    /**
     * Converts an array of bytes to its hexadecimal representation. http://stackoverflow.com/a/9855338/1117552
     *
     * @param bytes Byte array to convert.
     *
     * @return Hexadecimal String representation of bytes.
     */
    public static String bytesToHex(final byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_CHARS[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHARS[v & 0x0F];
        }
        return new String(hexChars);
    }
}
