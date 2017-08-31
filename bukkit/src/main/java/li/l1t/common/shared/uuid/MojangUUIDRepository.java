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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import li.l1t.common.lib.com.mojang.api.profiles.HttpProfileRepository;
import li.l1t.common.lib.com.mojang.api.profiles.Profile;
import li.l1t.common.util.UUIDHelper;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A {@link UUIDRepository} backed by the Mojang Name→UUID SOAP API.
 * This impl only supports Name→UUID lookup, reverse lookup can only be provided in a limited manner, by
 * checking cached UUIDs.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 14.7.14
 */
public class MojangUUIDRepository implements UUIDRepository {
    private static final HttpProfileRepository HTTP_PROFILE_REPOSITORY = new HttpProfileRepository("minecraft");
    private final LoadingCache<String, UUID> uuidCache = CacheBuilder.newBuilder()
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .maximumSize(4_200L)
            .build(new CacheLoader<String, UUID>() {
                @Override
                @Nonnull
                public UUID load(@Nonnull String name) throws UnknownKeyException, InvalidResultException {
                    Profile[] profiles = HTTP_PROFILE_REPOSITORY.findProfilesByNames(name);

                    if (profiles.length == 1 && !profiles[0].isDemo()) {
                        return profiles[0].getUniqueId();
                    } else if (profiles.length == 0) {
                        throw new UnknownKeyException();
                    } else {
                        throw new InvalidResultException(profiles);
                    }
                }
            });

    @Nonnull
    private UUIDRepository parent = EmptyUUIDRepository.INSTANCE;


    @Override
    public UUID forName(String name) {
        if (UUIDHelper.isValidUUID(name)) {
            return UUIDHelper.getFromString(name);
        }
        try {
            return uuidCache.getUnchecked(name);
        } catch (UncheckedExecutionException e) {
            if (e.getCause() instanceof UnknownKeyException) {
                return parent.forName(name);
            } else {
                throw e;
            }
        }
    }

    @Nonnull
    @Override
    public UUID forNameChecked(String name) throws UnknownKeyException, InvalidResultException {
        if (UUIDHelper.isValidUUID(name)) {
            return UUIDHelper.getFromString(name);
        }
        try {
            return uuidCache.get(name);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof UnknownKeyException) {
                return getParent().forNameChecked(name);
            } else if (e.getCause() instanceof InvalidResultException) {
                throw (InvalidResultException) e.getCause();
            } else {
                throw new UncheckedExecutionException(e.getCause());
            }
        }
    }

    @Override
    public String getName(UUID uuid) {
        for (Map.Entry<String, UUID> entry : uuidCache.asMap().entrySet()) {
            if (entry.getValue().equals(uuid)) {
                return entry.getKey();
            }
        }

        return getParent().getName(uuid);
    }

    @Override
    @Nonnull
    public UUIDRepository getParent() {
        return parent;
    }

    @Override
    public void setParent(@Nullable UUIDRepository newParent) {
        if (newParent == null) {
            parent = EmptyUUIDRepository.INSTANCE;
        } else {
            parent = newParent;
        }
    }

    @Override
    public ServicePriority getPriority() {
        return ServicePriority.Lowest;
    }
}
