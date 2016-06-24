/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.games.kits;

import li.l1t.common.games.kits.objective.ObjectiveResolver;
import li.l1t.common.util.FileHelper;
import li.l1t.common.util.inventory.InventoryHelper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class that is capable of loading kits.
 *
 * @author xxyy
 */
public class KitLoader {
    public static final String FILE_EXTENSION = ".kit.yml";
    private final KitManager manager;
    private final File kitDirectory;

    /**
     * Constructs a new kit loader which is ready to load kits from a directory upon request.
     *
     * @param manager      the manager associated with the new loader
     * @param kitDirectory the directory containing the kit files (need not exist yet)
     */
    public KitLoader(KitManager manager, File kitDirectory) {
        this.manager = manager;
        this.kitDirectory = kitDirectory;
    }

    /**
     * Deletes the file representing a kit.
     *
     * @throws IOException if an error occurs while deleting
     */
    public void delete(Kit kit) throws IOException {
        Files.delete(kit.getFile().toPath());
    }

    /**
     * @return the directory where kits are read from
     */
    public File getKitDirectory() {
        return kitDirectory;
    }

    /**
     * Gets the file for a kit by its name. Note that the file may not exist and may not
     * represent a valid kit.
     *
     * @param kitName the unique name of the kit
     * @return the file representing the kit of given name
     */
    public File getKitPath(String kitName) {
        return new File(getKitDirectory(), kitName + FILE_EXTENSION);
    }

    /**
     * Loads the list of kits and all kit data from the kit directory, creating it if it does not
     * exist.
     */
    public List<Kit> load() {
        FileHelper.mkdirsWithException(getKitDirectory());
        ArrayList<Kit> kits = new ArrayList<>();

        for (File kitFile : getKitDirectory().listFiles(
                file -> file.getName().endsWith(FILE_EXTENSION)
        )) {
            kits.add(loadKit(kitFile));
        }

        Collections.sort(kits); //maintain consistent display order - sorts by id,name
        return kits;
    }

    /**
     * Loads a single kit from its file.
     *
     * @param kitFile the kit file to load from
     * @return a new kit from that file
     */
    public Kit loadKit(File kitFile) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(kitFile);
        @SuppressWarnings("SuspiciousToArrayCall")
        ItemStack[] armor = config.getList(Kit.ARMOR_PATH)
                .toArray(new ItemStack[InventoryHelper.ARMOR_SIZE]);
        @SuppressWarnings("SuspiciousToArrayCall")
        ItemStack[] contents = config.getList(Kit.CONTENTS_PATH)
                .toArray(new ItemStack[InventoryHelper.PLAYER_INV_SIZE]);

        return new Kit(
                manager, config.getInt(Kit.ID_PATH, 0), kitFile,
                config.getItemStack(Kit.ICON_PATH),
                contents, armor, config.getString(Kit.OBJECTIVE_PATH),
                config.getString(Kit.AUTHOR_PATH), config
        );
    }

    /**
     * Saves a kit to its designated file.
     *
     * @param kit the kit to save in its current state
     * @throws IOException if an error occurs saving the file
     */
    public void saveKit(Kit kit) throws IOException {
        YamlConfiguration config = kit.getConfig();
        config.set(Kit.ID_PATH, kit.getId());
        config.set(Kit.ICON_PATH, kit.getIconStack());
        config.set(Kit.CONTENTS_PATH, kit.getContents());
        config.set(Kit.ARMOR_PATH, kit.getArmor());
        config.set(Kit.OBJECTIVE_PATH, kit.getObjective());
        config.set(Kit.AUTHOR_PATH, kit.getAuthorName());
        config.save(kit.getFile());
    }

    /**
     * Creates a new kit and saves it to the kit directory.
     *
     * @param name       the file name of the new kit
     * @param id         the {@link Kit#getId() sorting id} of the new kit
     * @param iconStack  the icon stack to represent the new kit in inventories
     * @param contents   the inventory contents for the new kit
     * @param armor      the armor contents for the new kit
     * @param objective  the {@link ObjectiveResolver
     *                   objective} required to access the new kit
     * @param authorName the name of the player who created the new kit
     * @return the created kit
     * @throws IOException if an error occurs creating the file or saving the kit
     */
    public Kit createKit(String name, int id, ItemStack iconStack, ItemStack[] contents,
                         ItemStack[] armor, String objective, String authorName) throws IOException {
        File file = getKitPath(name);
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }
        YamlConfiguration config = new YamlConfiguration();
        Kit kit = new Kit(manager, id, file, iconStack, contents, armor, objective, authorName, config);
        saveKit(kit);
        return kit;
    }
}
