/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.common.games.data;

import li.l1t.common.sql.SafeSql;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Creates default {@link PlayerWrapper}s.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 16.4.14
 * @deprecated Part of the deprecated PlayerWrapper API. See {@link PlayerWrapper} for details.
 */
@Deprecated
public class DefaultPlayerWrapperFactory implements PlayerWrapperFactory<PlayerWrapper> {
    private static final ReentrantLock WRAPPER_CACHE_LOCK = new ReentrantLock(false);
    private final SafeSql sql;
    private Map<UUID, PlayerWrapper> wrappers = new HashMap<>();
    private Map<String, UUID> nameToUuidMap = new HashMap<>();

    @java.beans.ConstructorProperties({"sql"})
    public DefaultPlayerWrapperFactory(SafeSql sql) {
        this.sql = sql;
    }

    @Override
    public void forceFetchAllProducts() {
        WRAPPER_CACHE_LOCK.lock();
        try {
            for (PlayerWrapper wrp : wrappers.values()) {
                wrp.forceFullFetch();
            }
        } finally {
            WRAPPER_CACHE_LOCK.unlock();
        }
    }

    @Override
    public void forceFlushAllProducts() {
        WRAPPER_CACHE_LOCK.lock();
        try {
            for (PlayerWrapper wrp : wrappers.values()) {
                wrp.forceFullFlush();
            }
        } finally {
            WRAPPER_CACHE_LOCK.unlock();
        }
    }

    @Override
    public void free(@Nonnull UUID wrpUniqueId) {
        WRAPPER_CACHE_LOCK.lock();

        try {
            wrappers.remove(wrpUniqueId);

            Iterator<UUID> iterator = nameToUuidMap.values().iterator();
            while (iterator.hasNext()) {
                if (wrpUniqueId.equals(iterator.next())) {
                    iterator.remove();
                    break;
                }
            }
        } finally {
            WRAPPER_CACHE_LOCK.unlock();
        }
    }

    @Override
    public PlayerWrapper getWrapper(CommandSender sender) {
        WRAPPER_CACHE_LOCK.lock();

        try {
            PlayerWrapper wrp = new PlayerWrapper(sender, getSql());
            wrappers.put(wrp.getUniqueId(), wrp);
            nameToUuidMap.put(sender.getName(), wrp.getUniqueId());

            return wrp;
        } finally {
            WRAPPER_CACHE_LOCK.unlock();
        }
    }

    @Override
    public PlayerWrapper getWrapper(@Nonnull UUID uuid, @Nullable String plrName) {
        WRAPPER_CACHE_LOCK.lock();

        try {
            PlayerWrapper wrp = new PlayerWrapper(uuid, plrName, getSql());
            wrappers.put(wrp.getUniqueId(), wrp);
            nameToUuidMap.put(plrName, uuid);

            return wrp;
        } finally {
            WRAPPER_CACHE_LOCK.unlock();
        }
    }

    /**
     * Gets a wrapper by the player's name.
     * Unsafe, should only be used when nothing else is possible.
     *
     * @param plrName Name to match
     * @return A wrapper
     * @throws java.lang.NullPointerException If this player does not have a cached wrapper.
     */
    public PlayerWrapper getWrapper(@Nonnull String plrName) {
        return getWrapper(nameToUuidMap.get(plrName), plrName); //Can't do anything else
    }

    @Override
    public Collection<PlayerWrapper> getWrappers() {
        return wrappers.values();
    }

    public Map<UUID, PlayerWrapper> getWrapperMap() {
        return this.wrappers;
    }

    public SafeSql getSql() {
        return this.sql;
    }
}

