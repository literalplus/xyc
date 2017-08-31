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

package li.l1t.common.shared.uuid;

import li.l1t.common.lib.com.mojang.api.profiles.Profile;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * A repository containing name → UUID mappings.
 *
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
     * @throws UnknownKeyException    If no UUID was found for given key.
     * @throws InvalidResultException If profiles were found, but no unique UUID could be retrieved.
     * @see #forName(String)
     */
    @Nonnull
    UUID forNameChecked(String name) throws UnknownKeyException, InvalidResultException;

    /**
     * <p>Gets the first name associated with a given UUID. This lookup direction is much slower than {@link #forName(String)}
     * since it uses reverse lookup and should only be used in rare cases.
     * </p>
     * Some implementations may also only return names for UUIDs that were fetched using {@link #forName(String)} because
     * not all backends support reverse lookup.
     *
     * @param uuid UUID to match.
     * @return A name associated with given UUID or NULL if no name was found.
     */
    @Nullable
    String getName(UUID uuid);

    /**
     * Gets the parent repository of this object. The parent is queried when a request for a UUID doesn't return any results.
     *
     * @return The parent or {@link EmptyUUIDRepository#INSTANCE} if no parent is set.
     */
    @Nonnull
    UUIDRepository getParent();

    /**
     * Sets the parent repository of this repo. If NULL is passed, {@link EmptyUUIDRepository#INSTANCE} is used.
     *
     * @param newParent new parent of this repo.
     */
    void setParent(@Nullable UUIDRepository newParent);

    /**
     * @return the priority of this repository
     */
    ServicePriority getPriority();

    class UnknownKeyException extends Exception {

    }

    class InvalidResultException extends Exception {
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
    class EmptyUUIDRepository implements UUIDRepository {

        public static final EmptyUUIDRepository INSTANCE = new EmptyUUIDRepository();

        private EmptyUUIDRepository() {

        }

        @Nullable
        @Override
        public UUID forName(String name) {
            return null;
        }

        @Nonnull
        @Override
        public UUID forNameChecked(String name) throws UnknownKeyException, InvalidResultException {
            throw new UnknownKeyException();
        }

        @Nullable
        @Override
        public String getName(UUID uuid) {
            return null;
        }

        @Nonnull
        @Override
        public UUIDRepository getParent() {
            return this;
        }

        @Override
        public void setParent(UUIDRepository newParent) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ServicePriority getPriority() {
            return ServicePriority.Lowest;
        }
    }
}
