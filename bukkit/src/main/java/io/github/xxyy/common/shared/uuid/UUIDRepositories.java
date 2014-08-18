/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without permission from the
 *  original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.shared.uuid;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import io.github.xxyy.common.internal.CommonPlugin;

import java.util.UUID;

/**
 * Helps dealing with UUID repositories.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 8.7.14
 */
public final class UUIDRepositories {

    public static final MojangUUIDRepository MOJANG_UUID_REPOSITORY = new MojangUUIDRepository();

    private UUIDRepositories() {

    }

    /**
     * Gets the UUID corresponding to given name from the registered {@link io.github.xxyy.common.shared.uuid.UUIDRepository} chain.
     *
     * @param userName Name to search for
     * @return UUID corresponding to given name or NULL if none.
     * @see io.github.xxyy.common.shared.uuid.UUIDRepository#forName(String)
     */
    public static UUID getUUID(String userName) {
        return getRepoOrFail().forName(userName);
    }

    /**
     * Gets the UUID corresponding to the given name from the registered {@link io.github.xxyy.common.shared.uuid.UUIDRepository} chain,
     * if present.
     *
     * @param userName Name to search for
     * @return UUID corresponding to given name.
     * @throws UUIDRepository.UnknownKeyException    If no UUID was found for given name.
     * @throws UUIDRepository.InvalidResultException If the result is invalid for some reason.
     */
    public static UUID getUUIDChecked(String userName) throws UUIDRepository.UnknownKeyException, UUIDRepository.InvalidResultException {
        return getRepoOrFail().forNameChecked(userName);
    }

    /**
     * Adds a new repository to the repository chain. New repositories are checked before delegating the call to existing ones.
     * A {@link io.github.xxyy.common.shared.uuid.MojangUUIDRepository} is at top of the chain by default.
     *
     * @param newRepo New repository to add to the chain
     * @param plugin  the plugin to use if xyc isn't running standalone
     */
    public static void addRepository(UUIDRepository newRepo, Plugin plugin) {
        if (!UUIDRepositories.class.getName().startsWith("io.github.xxyy.common.shared")) {
            plugin.getLogger().warning("[XYC] UUIDRepository registrations won't interface with other plugins when shaded to a different place!");
        }

        if (!(newRepo instanceof MojangUUIDRepository) && getRepository() == null) {
            addRepository(MOJANG_UUID_REPOSITORY, CommonPlugin.hasInstance() ? CommonPlugin.instance() : plugin);
            if (!CommonPlugin.hasInstance()) {
                plugin.getLogger().warning("[XYC] Registering UUIDRepository " + newRepo + " with non-XYC plugin - This may cause conflicts with plugin reloaders!");
            }
        }

        CommonPlugin.instance().getServer().getServicesManager()
                .register(UUIDRepository.class, newRepo, plugin, newRepo.getPriority());
    }

    /**
     * @return the registered UUIDRepository with the highest priority
     */
    public static UUIDRepository getRepository() {
        return Bukkit.getServicesManager().load(UUIDRepository.class);
    }

    private static UUIDRepository getRepoOrFail() {
        if (!UUIDRepositories.class.getName().startsWith("io.github.xxyy.common.shared")) {
            Bukkit.getLogger().warning("[XYC] UUIDRepository registrations won't interface with other plugins when shaded to a different place!");
        }

        UUIDRepository repository = getRepository();
        Validate.notNull(repository, "No repositories registered!");
        return repository;
    }
}
