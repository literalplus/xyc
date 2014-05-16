package io.github.xxyy.common.util.encryption;

import io.github.xxyy.common.util.math.NumberHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang.UnhandledException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * helps dealing with en- and decrypting passwords.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 16.5.14
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PasswordHelper {

    /**
     * Encrypts a password with a salt.
     * This uses SHA-512 as hashing algorithm and appends the salt after the password.
     * @param password Password to encrypt
     * @param salt Salt to use. Pass {@code null} or an empty string if you don't want to use a salt.
     * @return The encrypted password
     */
    public static String encrypt(@NonNull String password, String salt) {
        try {
            return encrypt(password, salt, MessageDigest.getInstance("SHA-512"));
        } catch (NoSuchAlgorithmException e) {
            throw new UnhandledException(e);
        }
    }

    /**
     * Encrypts a password with a salt, using a custom MessageDigest.
     * This appends the salt after the password.
     * @param password Password to encrypt
     * @param salt Salt to use. Pass {@code null} or an empty string if you don't want to use a salt.
     * @param messageDigest MessageDigest to use
     * @return The encrypted password
     */
    public static String encrypt(String password, String salt, MessageDigest messageDigest) {
        if(salt == null) {
            salt = "";
        }

        String plainText = password.concat(salt);
        byte[] hash = messageDigest.digest(plainText.getBytes());
        return NumberHelper.bytesToHex(hash);
    }

    /**
     * Checks whether a not encrypted password is equal to an encrypted password.
     * This is useful if you save passwords
     * @param unEncryptedPassword Raw password that was entered.
     * @param salt Salt to use. Pass {@code null} or an empty string if you don't want to use a salt.
     * @param encryptedPassword Already encrypted password to check against.
     * @return whether the passwords equal.
     */
    public static boolean passwordsEqual(String unEncryptedPassword, String salt, String encryptedPassword) {
        return encrypt(unEncryptedPassword, salt).equals(encryptedPassword);
    }

    /**
     * Generates a random salt using the provided MessageDigest.
     * @param messageDigest MessageDigest to use
     * @return Random salt
     */
    public static String generateSalt(MessageDigest messageDigest) {
        byte[] hash = messageDigest.digest(NumberHelper.randomInteger().toByteArray());
        return NumberHelper.bytesToHex(hash);
    }

    public static String generateSalt() {
        try {
            return generateSalt(MessageDigest.getInstance("SHA-512"));
        } catch (NoSuchAlgorithmException e) {
            throw new UnhandledException(e);
        }
    }
}
