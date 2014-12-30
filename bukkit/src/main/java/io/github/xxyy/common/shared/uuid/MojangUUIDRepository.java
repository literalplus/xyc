/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.shared.uuid;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.github.xxyy.common.lib.com.mojang.api.profiles.HttpProfileRepository;
import io.github.xxyy.common.lib.com.mojang.api.profiles.Profile;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A {@link io.github.xxyy.common.shared.uuid.UUIDRepository} backed by the Mojang Name→UUID SOAP API.
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
            .<String, UUID>build(new CacheLoader<String, UUID>() {
                @Override
                @NotNull
                public UUID load(@NotNull String name) throws UnknownKeyException, InvalidResultException {
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

    @NotNull
    private UUIDRepository parent = EmptyUUIDRepository.INSTANCE;


    @Override
    public UUID forName(String name) {
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

    @NotNull
    @Override
    public UUID forNameChecked(String name) throws UnknownKeyException, InvalidResultException {
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
    @NotNull
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
