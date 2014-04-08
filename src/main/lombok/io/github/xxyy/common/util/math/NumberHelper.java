package io.github.xxyy.common.util.math;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

/**
 * Class that provides some static methods to deal with numbers.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class NumberHelper {

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        symbols.setGroupingSeparator(' ');
        symbols.setDecimalSeparator(' ');//hacky!
        SPACE_FORMAT = new DecimalFormat(",###", symbols);
        CLASSES_TO_OPERATORS =
                ImmutableMap.<Class<? extends Number>, MathOperator<? extends Number>>builder()
                        .put(Integer.class, MathOperator.INTEGER_MATH_OPERATOR)
                        .put(Long.class, MathOperator.LONG_MATH_OPERATOR)
                        .put(Double.class, MathOperator.DOUBLE_MATH_OPERATOR)
                        .put(Float.class, MathOperator.FLOAT_MATH_OPERATOR)
                .build();

    }
    private static final DecimalFormat SPACE_FORMAT;
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
    private static final Map<Class<? extends Number>, MathOperator<? extends Number>> CLASSES_TO_OPERATORS;

    /**
     * Tries to get a predefined {@link io.github.xxyy.common.util.math.MathOperator} for a class.
     * @param clazz Target class - Must be the actual Object one, not the primitive.
     * @return A MathOperator corresponding to {@code clazz}, if available.
     */
    @Nullable @SuppressWarnings("unchecked") //The Map is guaranteed to always have the corresponding object
    public static <N extends Number> MathOperator<N> getOperator(@NonNull Class<N> clazz){
        return (MathOperator<N>) CLASSES_TO_OPERATORS.get(clazz);
    }

    /**
     * Formats a number with spaces as group separator for use on signs. The number will be colored in black, if it's to short to actually
     * automatically be aligned right, it will be prefixed with grey underscores. Possible outputs: &amp;7________&amp;0123 123456789012345
     *
     * @param number The number to be formatted
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
     * {@code def} is returned.
     *
     * @param source String to get the int from.
     * @param def    int to return if {@code source} does not contain a valid int.
     *
     * @return int contained in {@code source} or {@code def}.
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
