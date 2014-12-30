/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.util.encryption;

import org.apache.commons.lang.UnhandledException;
import org.jetbrains.annotations.NotNull;

import io.github.xxyy.common.util.math.NumberHelper;

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
    public static String encrypt(@NotNull String password, String salt) {
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
