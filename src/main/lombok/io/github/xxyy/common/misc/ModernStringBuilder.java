package io.github.xxyy.common.misc;

import java.util.stream.IntStream;

/**
 * A  builder for Strings, like {@link StringBuilder}, but more wow.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 1.8.14
 */
// This class could use some more difference from StringBuilder. Actually.
public class ModernStringBuilder implements Appendable, CharSequence {
    private StringBuilder proxy;

    public ModernStringBuilder(StringBuilder proxy) {
        this.proxy = proxy;
    }

    public ModernStringBuilder appendIf(boolean condition, CharSequence sequence) {
        if(condition) {
            append(sequence);
        }

        return this;
    }

    ////// blah, blah, proxy stuff, blah (Nothing interesting from here)

    public ModernStringBuilder append(Object obj) {
        proxy.append(obj);
        return this;
    }

    public ModernStringBuilder append(char c) {
        proxy.append(c);
        return this;
    }

    public void ensureCapacity(int minimumCapacity) {
        proxy.ensureCapacity(minimumCapacity);
    }

    public ModernStringBuilder insert(int index, char[] str, int offset, int len) {
        proxy.insert(index, str, offset, len);
        return this;
    }

    public void setLength(int newLength) {
        proxy.setLength(newLength);
    }

    public void setCharAt(int index, char ch) {
        proxy.setCharAt(index, ch);
    }

    public ModernStringBuilder insert(int offset, Object obj) {
        proxy.insert(offset, obj);
        return this;
    }

    public char charAt(int index) {
        return proxy.charAt(index);
    }

    public int codePointCount(int beginIndex, int endIndex) {
        return proxy.codePointCount(beginIndex, endIndex);
    }

    public ModernStringBuilder append(long lng) {
        proxy.append(lng);
        return this;
    }

    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        proxy.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public int indexOf(String str, int fromIndex) {
        return proxy.indexOf(str, fromIndex);
    }

    public ModernStringBuilder insert(int offset, long l) {
        proxy.insert(offset, l);
        return this;
    }

    public ModernStringBuilder insert(int offset, boolean b) {
        proxy.insert(offset, b);
        return this;
    }

    public int capacity() {
        return proxy.capacity();
    }

    public int offsetByCodePoints(int index, int codePointOffset) {
        return proxy.offsetByCodePoints(index, codePointOffset);
    }

    public String substring(int start, int end) {
        return proxy.substring(start, end);
    }

    public ModernStringBuilder insert(int dstOffset, CharSequence s, int start, int end) {
        proxy.insert(dstOffset, s, start, end);
        return this;
    }

    public ModernStringBuilder append(float f) {
        proxy.append(f);
        return this;
    }

    public CharSequence subSequence(int start, int end) {
        return proxy.subSequence(start, end);
    }

    public ModernStringBuilder insert(int offset, String str) {
        proxy.insert(offset, str);
        return this;
    }

    public ModernStringBuilder insert(int offset, char[] str) {
        proxy.insert(offset, str);
        return this;
    }

    public ModernStringBuilder replace(int start, int end, String str) {
        proxy.replace(start, end, str);
        return this;
    }

    public ModernStringBuilder append(double d) {
        proxy.append(d);
        return this;
    }

    public ModernStringBuilder insert(int offset, float f) {
        proxy.insert(offset, f);
        return this;
    }

    public ModernStringBuilder append(CharSequence s, int start, int end) {
        proxy.append(s, start, end);
        return this;
    }

    public int length() {
        return proxy.length();
    }

    public ModernStringBuilder deleteCharAt(int index) {
        proxy.deleteCharAt(index);
        return this;
    }

    public ModernStringBuilder insert(int dstOffset, CharSequence s) {
        proxy.insert(dstOffset, s);
        return this;
    }

    public ModernStringBuilder delete(int start, int end) {
        proxy.delete(start, end);
        return this;
    }

    public int lastIndexOf(String str) {
        return proxy.lastIndexOf(str);
    }

    public ModernStringBuilder append(CharSequence s) {
        proxy.append(s);
        return this;
    }

    public ModernStringBuilder append(char[] str) {
        proxy.append(str);
        return this;
    }

    public IntStream chars() {
        return proxy.chars();
    }

    public ModernStringBuilder append(String str) {
        proxy.append(str);
        return this;
    }

    public IntStream codePoints() {
        return proxy.codePoints();
    }

    public void trimToSize() {
        proxy.trimToSize();
    }

    public int lastIndexOf(String str, int fromIndex) {
        return proxy.lastIndexOf(str, fromIndex);
    }

    public int codePointAt(int index) {
        return proxy.codePointAt(index);
    }

    public int indexOf(String str) {
        return proxy.indexOf(str);
    }

    public ModernStringBuilder append(boolean b) {
        proxy.append(b);
        return this;
    }

    public ModernStringBuilder insert(int offset, char c) {
        proxy.insert(offset, c);
        return this;
    }

    public String substring(int start) {
        return proxy.substring(start);
    }

    public ModernStringBuilder appendCodePoint(int codePoint) {
        proxy.appendCodePoint(codePoint);
        return this;
    }

    public ModernStringBuilder append(char[] str, int offset, int len) {
        proxy.append(str, offset, len);
        return this;
    }

    public ModernStringBuilder append(int i) {
        proxy.append(i);
        return this;
    }

    public int codePointBefore(int index) {
        return proxy.codePointBefore(index);
    }

    public ModernStringBuilder reverse() {
        proxy.reverse();
        return this;
    }

    public ModernStringBuilder insert(int offset, int i) {
        proxy.insert(offset, i);
        return this;
    }

    public ModernStringBuilder append(StringBuffer sb) {
        proxy.append(sb);
        return this;
    }

    public ModernStringBuilder insert(int offset, double d) {
        proxy.insert(offset, d);
        return this;
    }
}
