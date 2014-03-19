package io.github.xxyy.common.games.kits;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Represents a kit.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class KitInfo implements Comparable<KitInfo> {

    public static final int INVENTORY_SIZE = 36;
    public static final int ARMOR_SIZE = 4;
    private String name;
    private ItemStack icon;
    private ItemStack unavailIcon;
    private ItemStack[] contents = new ItemStack[INVENTORY_SIZE];
    private int id = 1;
    private ItemStack[] armor = new ItemStack[ARMOR_SIZE];
    private String objectiveNeeded = null;
    private String objectiveNeededAmount = null;
    private String authorName = null;
    private File fl = null;
    private YamlConfiguration cfg = null;

    /**
     * Constructs a KitInfo and fetches all information from a file.
     *
     * @param path Where the kit is located.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    protected KitInfo(String path) {
        this.fl = new File(path);
        this.cfg = YamlConfiguration.loadConfiguration(this.fl);
        this.name = this.cfg.getString("name");
        this.icon = this.cfg.getItemStack("icon");
        this.unavailIcon = this.cfg.getItemStack("unavailable-icon");
        this.armor = this.cfg.getList("armor").toArray(new ItemStack[ARMOR_SIZE]);
        this.contents = this.cfg
                .getList("contents")
                .toArray(new ItemStack[INVENTORY_SIZE]);
        this.objectiveNeeded = this.cfg.getString("objective.name", null);
        this.objectiveNeededAmount = this.cfg.getString("objective.amount", null);
        this.authorName = this.cfg.getString("author", "Anonymous");
        this.id = this.cfg.getInt("id", -1);
    }

    /**
     * Constructs a KitInfo and saves it to a file.
     */
    protected KitInfo(String name, ItemStack icon, ItemStack unavailIcon, ItemStack[] contents, ItemStack[] armor,
                      String objNeeded, String objAmount, String path, String authorName, int id) {
        Validate.notNull(path);
        Validate.notNull(icon);
        Validate.notNull(contents);
        Validate.notNull(armor);
        Validate.notNull(path);
        if (authorName == null || authorName.isEmpty()) {
            authorName = "Anonymous";
        }
        this.name = name;
        this.icon = icon;
        this.contents = Arrays.copyOf(contents, INVENTORY_SIZE);
        this.armor = Arrays.copyOf(armor, ARMOR_SIZE);
        this.authorName = authorName;
        this.objectiveNeeded = objNeeded;
        this.objectiveNeededAmount = objAmount;
        this.unavailIcon = unavailIcon;
        this.fl = new File(path);
        this.cfg = YamlConfiguration.loadConfiguration(this.fl);
        this.cfg.options().copyDefaults(true);
        this.cfg.options().copyHeader(true);
        this.cfg.options().header("TOWERDEFENSE KIT FILE - " + name);
        this.cfg.addDefault("name", name);
        this.cfg.addDefault("icon", icon);
        this.cfg.addDefault("unavailable-icon", unavailIcon);
        this.cfg.addDefault("contents", contents);
        this.cfg.addDefault("armor", armor);
        this.cfg.addDefault("objective.name", objNeeded);
        this.cfg.addDefault("objective.amount", objAmount);
        this.cfg.addDefault("author", authorName);
        this.cfg.addDefault("id", id);
        this.flush();
    }

    /**
     * For internal purposes
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    private KitInfo() {
    }

    @Override
    public int compareTo(KitInfo other) {
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
        if (!(obj instanceof KitInfo)) {
            return false;
        }
        KitInfo other = (KitInfo) obj;
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

    /**
     * Saves the current change of this {@link KitInfo} to disk.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public void flush() {
        if (this.fl == null || this.cfg == null) {
            throw new IllegalStateException("Dummy KitInfos can not be saved. doh.");
        }
        try {
            this.cfg.save(this.fl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return The armor contents for this {@link KitInfo}.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public ItemStack[] getArmor() {
        return this.armor;
    }

    /**
     * @return the authorName
     */
    public String getAuthorName() {
        return this.authorName;
    }

    /**
     * @return The {@link YamlConfiguration} backing this {@link KitInfo}.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public YamlConfiguration getCfg() {
        return this.cfg;
    }

    /**
     * @return The inventory contents for this {@link KitInfo}.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public ItemStack[] getContents() {
        return this.contents;
    }

    /**
     * @return The icon that represents this {@link KitInfo} in a {@link KitSelector}.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public ItemStack getIcon() {
        return this.icon;
    }

    /**
     * @return The name of this {@link KitInfo}.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the objectiveNeeded
     */
    public String getObjectiveNeeded() {
        return this.objectiveNeeded;
    }

    /**
     * @return the objectiveNeededAmount
     */
    public String getObjectiveNeededAmount() {
        return this.objectiveNeededAmount;
    }

    /**
     * @return the unavailIcon
     */
    public ItemStack getUnavailIcon() {
//        System.out.println(this.cfg.getItemStack("unavailable-icon"));
        return this.unavailIcon;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    /**
     * Constructs a dummy KitInfo for display - this can be used in special cases, i.e. when no KitInfo is available for loading. Dummy KitInfos can
     * NOT be saved.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    protected static KitInfo constructDummy(String name, ItemStack icon, ItemStack[] armor, ItemStack[] contents) {
        Validate.notNull(icon);
        Validate.notNull(contents);
        Validate.notNull(armor);
        if (contents.length != 36) {
            throw new IllegalArgumentException("contents must have exactly 36 entries.");
        }
        if (armor.length != 4) {
            throw new IllegalArgumentException("contents must have exactly 4 entries.");//definitely not spaghetti code
        }
        KitInfo ki = new KitInfo();//ki =/= "Künstliche Intelligenz" (=AI)
        ki.name = name;
        ki.icon = icon;
        ki.armor = armor;
        ki.contents = contents;
        return ki;
    }
}
