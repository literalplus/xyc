package io.github.xxyy.common.util.uuid;

import io.github.xxyy.common.lib.com.mojang.api.profiles.Profile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * A repository containing name -> UUID mappings.
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 14.7.14
 */
public interface UUIDRepository {
    /**
     * Gets a UUID matching the given name from this repository.
     * If there are multiple UUIDs for that name, Mojang Premium Account ones are returned first.
     * If there is no Mojang UUID available and there are multiple cracked UUIDs, an {@link java.lang.IllegalStateException} is thrown.
     *
     * @param name Name to match
     * @return An UUID for given name or NULL if none were found.
     * @see #forNameChecked(String)
     */
    @Nullable
    UUID forName(String name);

    /**
     * Gets a UUID matching the given name from this repository.
     * If there are multiple UUIDs for that name, Mojang Premium Account ones are returned first.
     * If there is no Mojang UUID available and there are multiple cracked UUIDs, an {@link InvalidResultException} is thrown.
     *
     * @param name Name to match
     * @return An UUID for given name.
     * @throws UnknownKeyException If no UUID was found for given key.
     * @throws InvalidResultException If profiles were found, but no unique UUID could be retrieved.
     * @see #forName(String)
     */
    @NotNull
    UUID forNameChecked(String name) throws UnknownKeyException, InvalidResultException;

    /**
     * Gets the first name associated with a given UUID. This lookup direction is much slower than {@link #forName(String)}
     * since it uses reverse lookup and should only be used in rare cases.
     *
     * Some implementations may also only return names for UUIDs that were fetched using {@link #forName(String)} because
     * not all backends support reverse lookup.
     * @param uuid UUID to match.
     * @return A name associated with given UUID or NULL if no name was found.
     */
    @Nullable
    String getName(UUID uuid);

    /**
     * Gets the parent repository of this object. The parent is queried when a request for a UUID doesn't return any results.
     * @return The parent
     */
    @Nullable
    UUIDRepository getParent();

    /**
     * Sets the parent repository of this repo.
     * @param newParent new parent of this repo.
     */
    void setParent(UUIDRepository newParent);

    public static class UnknownKeyException extends Exception {

    }

    public static class InvalidResultException extends Exception {
        @Nullable
        private final Profile[] profiles;

        public InvalidResultException(@Nullable Profile[] profiles) {
            this.profiles = profiles;
        }

        @Nullable
        public Profile[] getProfiles() {
            return profiles;
        }
    }

    /**
     * An empty UUID repository. This just returns NULL or throws {@link UnknownKeyException}s when queried.
     * {@link #setParent(UUIDRepository)} throws a {@link java.lang.UnsupportedOperationException}.
     */
    public static class EmptyUUIDRepository implements UUIDRepository {

        public static final EmptyUUIDRepository INSTANCE = new EmptyUUIDRepository();

        @Nullable
        @Override
        public UUID forName(String name) {
            return null;
        }

        @NotNull
        @Override
        public UUID forNameChecked(String name) throws UnknownKeyException, InvalidResultException {
            throw new UnknownKeyException();
        }

        @Nullable
        @Override
        public String getName(UUID uuid) {
            return null;
        }

        @Nullable
        @Override
        public UUIDRepository getParent() {
            return null;
        }

        @Override
        public void setParent(UUIDRepository newParent) {
            throw new UnsupportedOperationException();
        }
    }
}
