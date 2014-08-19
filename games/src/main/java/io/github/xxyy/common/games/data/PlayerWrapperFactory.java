/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.games.data;

import org.bukkit.command.CommandSender;

import io.github.xxyy.lib.intellij_annotations.NotNull;
import io.github.xxyy.lib.intellij_annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

/**
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 15.4.14
 */
public interface PlayerWrapperFactory<T extends PlayerWrapper> {
    /**
     * Forces a full (re-)fetch for all products owned by this instance.
     *
     * @see io.github.xxyy.common.games.data.PlayerWrapper#forceFullFetch()
     */
    void forceFetchAllProducts();

    /**
     * Forces a full flush for all products owned by this instance.
     *
     * @see io.github.xxyy.common.games.data.PlayerWrapper#forceFullFlush()
     */
    void forceFlushAllProducts();

    /**
     * Frees all instances of a product by a specified UUID kept by this instance.
     *
     * @param wrpUniqueId UUID of the player whose wrappers shall be freed.
     */
    void free(UUID wrpUniqueId);

    /**
     * Wraps a {@link org.bukkit.command.CommandSender}.
     * <b>Note the dangers when using {@link org.bukkit.command.CommandSender} specified in {@link io.github.xxyy.common.games.data.PlayerWrapper#PlayerWrapper(org.bukkit.command.CommandSender, io.github.xxyy.common.sql.SafeSql)}!</b>
     *
     * @param sender {@link org.bukkit.command.CommandSender} to be wrapped.
     * @return a T wrapping sender.
     * @throws ClassCastException forwarded from {@link io.github.xxyy.common.games.data.PlayerWrapper#PlayerWrapper(org.bukkit.command.CommandSender, io.github.xxyy.common.sql.SafeSql)}
     * @see io.github.xxyy.common.games.data.PlayerWrapper#PlayerWrapper(org.bukkit.command.CommandSender, io.github.xxyy.common.sql.SafeSql)
     */
    T getWrapper(CommandSender sender);

    /**
     * Wraps a player by name and uuid.
     *
     * @param uuid    the unique id of the wrapped player
     * @param plrName name of the wrapped player.
     * @return a T wrapping plrName.
     * @see io.github.xxyy.common.games.data.PlayerWrapper#PlayerWrapper(java.util.UUID, String, io.github.xxyy.common.sql.SafeSql)
     */
    T getWrapper(@NotNull UUID uuid, @Nullable String plrName);

    /**
     * @return All products that strong references are kept for by this instance.
     */
    Collection<T> getWrappers();
}
