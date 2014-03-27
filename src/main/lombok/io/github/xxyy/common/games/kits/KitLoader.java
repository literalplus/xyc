package io.github.xxyy.common.games.kits;

import com.google.common.collect.Lists;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class that is capable of loading kits.
 *
 * @author xxyy98
 */
public class KitLoader {
    String fileExtension = ".kit.yml";
    private String path;
    private boolean loaded;
    private KitInfo defaultKit;
    private Map<String, KitInfo> playerKits = new ConcurrentHashMap<>(24, 0.75F, 2);
    private List<KitInfo> kits = null;

    /**
     * @param orig A {@link KitLoader} to copy.
     */
    public KitLoader(KitLoader orig) {
        this(orig.path, orig.fileExtension);
        this.loaded = orig.loaded;
        if (orig.kits != null) {
            this.kits = new ArrayList<>(orig.kits);
            Collections.sort(this.kits);
        }
    }

    /**
     * Constructs a new {@link KitLoader} by a path where kits are located. Uses ".kit.yml" as file extension.
     *
     * @param path Path where the kits are.
     */
    public KitLoader(String path) {
        this.path = path;
        ItemStack icon = new ItemStack(Material.FLOWER_POT_ITEM);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("§4§lDummy Kit!");
        meta.setLore(Lists.newArrayList("§7§oThis means that no kits were found for loading.", "§e§oSee the server log for details."));
        icon.setItemMeta(meta);
        ItemStack[] armor = new ItemStack[]{new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.CHAINMAIL_LEGGINGS),
                new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_HELMET)};
        ItemStack[] contents = Lists.newArrayList(new ItemStack(Material.POISONOUS_POTATO), new ItemStack(Material.CLAY_BALL))
                .toArray(new ItemStack[36]);
        this.defaultKit = KitInfo.constructDummy("NoKitsLoaded", icon, armor, contents);
    }

    /**
     * Constructs a new {@link KitLoader} by a path where kits are located.
     *
     * @param path          Path where the kits are.
     * @param fileExtension file extension to use. (including '.')
     */
    public KitLoader(String path, String fileExtension) {
        this(path);
        this.fileExtension = fileExtension;
    }

    /**
     * Applies a {@link KitInfo} to a {@link Player}.
     *
     * @param kit KitInfo to apply.
     * @param plr Player that will receive the kit.
     */
    public void apply(KitInfo kit, Player plr) {
        Validate.notNull(kit);
        Validate.notNull(plr);
        plr.getInventory().setContents(kit.getContents());
        plr.getInventory().setArmorContents(kit.getArmor());
        this.playerKits.put(plr.getName(), kit);
    }

    /**
     * Allies a random kit to a {@link Player}.
     *
     * @param plr Player that will receive the kit.
     */
    public void applyRandomKit(Player plr) {
        if (!this.loaded) {
            this.load();
        }
        int rand = RandomUtils.nextInt(this.kits.size());
        this.apply(this.kits.get(rand), plr);
    }

    /**
     * Recursively deletes a kit by name.
     *
     * @param name Name of the kit.
     */
    public void delete(String name) {
        Validate.isTrue((new File(String.format(this.path, name))).delete(), "Could not delete KitInfo file for " + name);

        Iterator<KitInfo> iterator = kits.iterator();

        while (iterator.hasNext()) {
            if (iterator.next().getName().equalsIgnoreCase(name)) {
                iterator.remove();
            }
        }
    }

    /**
     * @return The default kit that is used if no kits are available.
     */
    public KitInfo getDefaultKit() {
        return this.defaultKit;
    }

    /**
     * @return The file extension that suffixes all kit files.
     */
    public String getFileExtension() {
        return this.fileExtension;
    }

    /**
     * @return All available kits.
     */
    public List<KitInfo> getKits() {
        if (!this.loaded) {
            this.load();
        }
        return this.kits;
    }

    /**
     * @return The path where the kits are located.
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Gets the kit applied to a {@link Player}.
     *
     * @param plrName Name of the player.
     * @return KitInfo or {@code null}.
     */
    public KitInfo getPlayerKit(String plrName) {
        return this.playerKits.get(plrName);
    }

    /**
     * @return Whether this KitLoader has loaded its kits.
     * @see KitLoader#load()
     */
    public boolean isLoaded() {
        return this.loaded;
    }

    /**
     * (Re-)loads the kits from the {@link KitLoader}'s folder.
     * @see KitLoader#isLoaded()
     */
    public void load() {
        this.loaded = true;
        File fl = new File(this.path);
        if (!fl.exists()) {
            assert fl.mkdirs();
        }
        if (!fl.isDirectory()) {
            throw new IllegalStateException("path is no directory");
        }
        this.kits = new ArrayList<>();
        for (String path : fl.list(new KitFilenameFilter())) {
            this.kits.add(new KitInfo(this.path + "/" + path));
        }
        Collections.sort(this.kits);
        if (this.kits.isEmpty()) {
            this.kits.add(this.defaultKit);
            System.out.println("Could not find ANY kits at " + this.path + " - Using default kit.");
        }
    }

    /**
     * @deprecated Use {@link KitLoader#register(String, ItemStack, ItemStack, ItemStack[], ItemStack[], String, String, String, int)}
     */
    @Deprecated
    public void put(KitInfo ki) {
        this.kits.add(ki);
        Collections.sort(this.kits);
    }

    protected KitInfo register(String name, ItemStack icon, ItemStack unavailIcon, ItemStack[] contents, ItemStack[] armor,
                               String objNeeded, String objAmount, String authorName, int id) {
        if (!this.loaded) {
            this.load();
        }
        KitInfo ki = new KitInfo(name, icon, unavailIcon, contents, armor, objNeeded, objAmount, this.path + "/" + name + this.fileExtension,
                authorName, id);
        this.kits.add(ki);
        Collections.sort(this.kits);
        return ki;
    }

    protected class KitFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(KitLoader.this.fileExtension);
        }
    }
}
