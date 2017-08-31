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

package li.l1t.common.util.encryption;

import li.l1t.common.util.math.NumberHelper;
import org.apache.commons.lang.UnhandledException;

import javax.annotation.Nonnull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * helps dealing with en- and decrypting passwords.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 16.5.14
 */
public final class PasswordHelper {

    private PasswordHelper() {
    }

    /**
     * Encrypts a password with a salt.
     * This uses SHA-512 as hashing algorithm and appends the salt after the password.
     *
     * @param password Password to encrypt
     * @param salt     Salt to use. Pass {@code null} or an empty string if you don't want to use a salt.
     * @return The encrypted password
     */
    public static String encrypt(@Nonnull String password, String salt) {
        try {
            return encrypt(password, salt, MessageDigest.getInstance("SHA-512"));
        } catch (NoSuchAlgorithmException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * Encrypts a password with a salt, using a custom MessageDigest.
     * This appends the salt after the password.
     *
     * @param password      Password to encrypt
     * @param salt          Salt to use. Pass {@code null} or an empty string if you don't want to use a salt.
     * @param messageDigest MessageDigest to use
     * @return The encrypted password
     */
    public static String encrypt(String password, String salt, MessageDigest messageDigest) {
        if (salt == null) {
            salt = "";
        }

        String plainText = password.concat(salt);
        byte[] hash = messageDigest.digest(plainText.getBytes());
        return NumberHelper.bytesToHex(hash);
    }

    /**
     * Checks whether a not encrypted password is equal to an encrypted password.
     * This is useful if you save passwords
     *
     * @param unEncryptedPassword Raw password that was entered.
     * @param salt                Salt to use. Pass {@code null} or an empty string if you don't want to use a salt.
     * @param encryptedPassword   Already encrypted password to check against.
     * @return whether the passwords equal.
     */
    public static boolean passwordsEqual(String unEncryptedPassword, String salt, String encryptedPassword) {
        return encrypt(unEncryptedPassword, salt).equals(encryptedPassword);
    }

    /**
     * Generates a random salt using the provided MessageDigest.
     *
     * @param messageDigest MessageDigest to use
     * @return Random salt
     */
    public static String generateSalt(MessageDigest messageDigest) {
        byte[] hash = messageDigest.digest(NumberHelper.randomInteger().toByteArray());
        return NumberHelper.bytesToHex(hash);
    }

    /**
     * Generates a random salt using the SHA-512 encryption.
     *
     * @return Random salt
     */
    public static String generateSalt() {
        try {
            return generateSalt(MessageDigest.getInstance("SHA-512"));
        } catch (NoSuchAlgorithmException e) {
            throw new UnhandledException(e);
        }
    }
}
