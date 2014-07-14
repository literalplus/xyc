package io.github.xxyy.common.util.uuid;

import com.google.common.base.Charsets;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 *Helps dealing with UUIDs.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 8.7.14
 */
public final class UUIDHelper {

    /**
     * A Pattern that matches valid Java UUIDs.
     */
    public static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");

    private static UUIDRepository uuidRepository = new MojangUUIDRepository();

    private UUIDHelper() {

    }

    /**
     * Performs a match using {@link #UUID_PATTERN} to check whether {@code input} is a valid UUID as accepted by the
     * Java {@link java.util.UUID} impl.
     * @param input Input string to check
     * @return Whether the input string is a valid Java UUID.
     */
    public static boolean isValidUUID(String input) {
        return UUID_PATTERN.matcher(input).matches();
    }

    /**
     * Creates an "offline" UUID as Minecraft would use for "cracked" players.
     * @param offlineName The offline player's name, case-sensitive.
     * @return the offline UUID for given name.
     */
    public static UUID getOfflineUUID(String offlineName) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + offlineName).getBytes(Charsets.UTF_8));
    }

    /**
     * Gets the UUID corresponding to given name from the registered {@link io.github.xxyy.common.util.uuid.UUIDRepository} chain.
     *
     * @param userName Name to search for
     * @return UUID corresponding to given name or NULL if none.
     * @see io.github.xxyy.common.util.uuid.UUIDRepository#forName(String)
     */
    public static UUID getUUID(String userName) {
        return uuidRepository.forName(userName);
    }

    /**
     * Gets the UUID corresponding to the given name from the registered {@link io.github.xxyy.common.util.uuid.UUIDRepository} chain,
     * if present.
     *
     * @param userName Name to search for
     * @return UUID corresponding to given name.
     * @throws UUIDRepository.UnknownKeyException If no UUID was found for given name.
     * @throws UUIDRepository.InvalidResultException If the result is invalid for some reason.
     */
    public static UUID getUUIDChecked(String userName) throws UUIDRepository.UnknownKeyException, UUIDRepository.InvalidResultException {
        return uuidRepository.forNameChecked(userName);
    }

    /**
     * Adds a new repository to the repository chain. New repositories are checked before delegating the call to existing ones.
     * A {@link io.github.xxyy.common.util.uuid.MojangUUIDRepository} is at top of the chain by default.
     * @param newRepo New repository to add to the chain
     */
    public static void addRepository(UUIDRepository newRepo) {
        newRepo.setParent(uuidRepository);
        uuidRepository = newRepo;
    }
}
