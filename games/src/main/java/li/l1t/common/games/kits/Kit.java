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
import li.l1t.common.util.inventory.InventoryHelper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;

/**
 * Represents a kit that may be applied to a player's inventory and armor and is backed by a YAML
 * file.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class Kit implements Comparable<Kit> {
    public static final String ID_PATH = "id";
    public static final String ICON_PATH = "icon";
    public static final String ARMOR_PATH = "armor";
    public static final String CONTENTS_PATH = "contents";
    public static final String OBJECTIVE_PATH = "objective";
    public static final String AUTHOR_PATH = "author";

    private final KitManager manager;
    private final String name;
    private final File file;
    private ItemStack iconStack;
    private ItemStack[] contents = new ItemStack[InventoryHelper.PLAYER_INV_SIZE];
    private ItemStack[] armor = new ItemStack[InventoryHelper.ARMOR_SIZE];
    private String objective;
    private int id; //To make sure kits are displayed in order

    private String authorName;
    private YamlConfiguration config;

    /**
     * Creates a new kit.
     *
     * @param manager    the manager associated with this kit
     * @param id         the sort id of this kit (higher numbers displayed last)
     * @param file       the file where this kit is stored
     * @param iconStack  the icon stack used to represent this kit in inventories
     * @param contents   the inventory contents for this kit
     * @param armor      the armor contents for this kit
     * @param objective  the objective string for this kit, null for open kits
     * @param authorName the name of the player who created this kit
     * @param config     the configuration object for this kit
     */
    Kit(KitManager manager, int id, File file, ItemStack iconStack,
        ItemStack[] contents, ItemStack[] armor, String objective, String authorName,
        YamlConfiguration config) {
        this.manager = manager;
        this.id = id;
        this.file = file;
        this.name = file.getName().replace(KitLoader.FILE_EXTENSION, "");
        this.iconStack = iconStack;
        this.contents = contents;
        this.armor = armor;
        this.objective = objective;
        this.authorName = authorName;
        this.config = config;
    }

    /**
     * @return the kit manager associated with this kit
     */
    public KitManager getManager() {
        return manager;
    }

    /**
     * Returns the sort id of this kit. This is used so that kits are always displayed in the
     * same order, which is useful if there are objective-locked kits that follow some kind of
     * pattern. Higher numbers are displayed last. If two kits have the same sort id, they are
     * sorted by their names.
     *
     * @return the sort id of this kit (higher numbers displayed last)
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the sort id for this kit. ({@link #getId() explanation})
     *
     * @param id the sort id of this kit (higher numbers displayed last)
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the file where this kit is stored
     */
    public File getFile() {
        return file;
    }

    /**
     * @return the objective string for this kit, null for open kits (e.g. no objective required)
     * @see ObjectiveResolver
     */
    public String getObjective() {
        return objective;
    }

    /**
     * Sets the objective string for this kit. To make this kit open, e.g. make it accessible
     * without condition, set objective to null.
     *
     * @param objective the objective string for this kit
     * @see ObjectiveResolver
     */
    public void setObjective(String objective) {
        this.objective = objective;
    }

    /**
     * @return whether access to this kit is bound to an objective
     */
    public boolean hasObjective() {
        return objective != null;
    }

    /**
     * @return the armor contents for this kit
     */
    public ItemStack[] getArmor() {
        return this.armor;
    }

    /**
     * Sets the armor contents for this kit
     *
     * @param armor the armor contents for this kit
     */
    public void setArmor(ItemStack[] armor) {
        this.armor = armor;
    }

    /**
     * @return the name of the player who created this kit
     */
    public String getAuthorName() {
        return this.authorName;
    }

    /**
     * Sets the name of the player who created this kit.
     *
     * @param authorName the name of the player who created this kit
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * @return the configuration backing this kit
     */
    public YamlConfiguration getConfig() {
        return this.config;
    }

    /**
     * @return the inventory contents for this kit
     */
    public ItemStack[] getContents() {
        return this.contents;
    }

    /**
     * Sets the inventory contents for this kit
     *
     * @param contents the new inventory contents for this kit
     */
    public void setContents(ItemStack[] contents) {
        this.contents = contents;
    }

    /**
     * @return the icon that represents this kit in inventories
     */
    public ItemStack getIconStack() {
        return this.iconStack;
    }

    /**
     * Sets the icon that represents this kit in inventories
     *
     * @param iconStack the new icon that represents this kit in inventories
     */
    public void setIconStack(ItemStack iconStack) {
        this.iconStack = iconStack;
    }

    /**
     * @return the file name of this kit, excluding {@link KitLoader#FILE_EXTENSION file extension}
     */
    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public int compareTo(Kit other) {
        if (this.equals(other)) {
            return 0;
        }
        if (this.id == other.id) {
            if (this.name.equalsIgnoreCase(other.name)) {
                return 0;
            }
            return this.name.compareTo(other.name);
        }
        if (this.id < other.id) {
            return -1;
        }
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Kit)) {
            return false;
        }
        Kit other = (Kit) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
