package io.github.xxyy.common.games.kits;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

/**
 * A class that provides an inventory used to select kits.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class KitSelector implements Listener {
    private String name;
    private int size;
    private OptionClickEventHandler handler;
    private Plugin plugin;
    private FulfillChecker checker;
    private KitInfo[] optionKits;
    private KitLoader loader;
    private boolean[] optionsClickable;

    /**
     * Duplicated a {@link KitSelector}.
     *
     * @param original {@link KitSelector} to duplicate.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public KitSelector(KitSelector original) {
        this(original.name, original.loader, original.handler, original.checker, original.plugin);
    }

    /**
     * Constructs a {@link KitSelector}.
     *
     * @param name    Title of the {@link Inventory} used to show the menu.
     * @param ldr     {@link KitLoader} used to get and apply kits.
     * @param handler Class that handles {@link OptionClickEvent}s invoked by this instance.
     * @param checker Class that checks for fulfillment of objectives.
     * @param plugin  Plugin owning this instance.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public KitSelector(String name, KitLoader ldr, OptionClickEventHandler handler, FulfillChecker checker, Plugin plugin) {
        this.name = name;
        this.handler = handler;
        this.checker = checker;
        this.plugin = plugin;
        this.loader = ldr;

        this.size = this.loader.getKits().size();
        if ((this.size % 9) != 0) {
            this.size += 9 - (this.size % 9);//getting it up to a multiple of 9
        }
        this.optionKits = new KitInfo[this.size];
        this.optionsClickable = new boolean[this.size];

        int i = 0;
        for (KitInfo kit : this.loader.getKits()) {
            this.optionKits[i] = kit;
            i++;
        }

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Destroys this instance and unregisters all {@link EventHandler}s.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public void destroy() {
        HandlerList.unregisterAll(this);
        this.handler = null;
        this.plugin = null;
        this.optionKits = null;
    }

    /**
     * Opens this {@link KitSelector} to a {@link Player}.
     *
     * @param plr {@link Player} to open to.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public void open(Player plr) {
        Inventory inventory = Bukkit.createInventory(plr, this.size, this.name);
        for (int i = 0; i < this.optionKits.length; i++) {
            if (this.optionKits[i] != null && this.optionKits[i].getIcon() != null) {
                String objNeeded = this.optionKits[i].getObjectiveNeeded();
                if (objNeeded != null) {
                    String objAmount = this.optionKits[i].getObjectiveNeededAmount();
                    if (!this.checker.fulfillsObjective(plr, objNeeded, objAmount)) {
                        this.optionsClickable[i] = false;
                        inventory.setItem(i, this.optionKits[i].getUnavailIcon());
                        continue;
                    }
                }
                this.optionsClickable[i] = true;
                inventory.setItem(i, this.optionKits[i].getIcon());
            }
        }
        plr.openInventory(inventory);
    }

    /**
     * @param handler the handler to set
     */
    public void setHandler(OptionClickEventHandler handler) {
        this.handler = handler;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(this.name)) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < this.size && this.optionKits[slot] != null) {
                if (!this.optionsClickable[slot]) {
                    return;
                }
                Plugin plugin = this.plugin;
                OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, this.optionKits[slot]);
                this.handler.onOptionClick(e);
                if (e.willClose()) {
                    final Player p = (Player) event.getWhoClicked();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            p.closeInventory();
                        }
                    }, 1);
                }
                if (!e.isCancelled()) {
                    this.loader.apply(this.optionKits[slot], (Player) event.getWhoClicked());
                }
            }
        }
    }

    /**
     * Classes that implement this can check if a {@link Player} fulfills an objective.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public interface FulfillChecker {

        /**
         * @param plr       Player that tries to use a kit
         * @param objective Objective category.
         * @param amount    Specified by the objective, for example for a "point" objective this would be the amount of points needed. For
         *                  "permission" it would be the required permission. (that means it's up to YOU)
         *
         * @return Whether the {@link Player} is allowed to use a kit "guarded" by this objective.
         * @author <a href="http://xxyy.github.io/">xxyy</a>
         */
        public boolean fulfillsObjective(Player plr, String objective, String amount);
    }

    /**
     * This event gets called when an option is clicked.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public class OptionClickEvent implements Cancellable {

        private Player player;
        private int position;
        private KitInfo kit;
        private boolean close;
        private boolean cancelled;

        /**
         * Construsts a new {@link OptionClickEvent}.
         *
         * @param player   Player who clicked.
         * @param position slot id that was clicked.
         * @param kit      Kit located in this slot.
         *
         * @author <a href="http://xxyy.github.io/">xxyy</a>
         */
        public OptionClickEvent(Player player, int position, KitInfo kit) {
            this.player = player;
            this.position = position;
            this.kit = kit;
            this.close = true;
            this.cancelled = false;
        }

        /**
         * @return The kit located in the clicked slot.
         * @author <a href="http://xxyy.github.io/">xxyy</a>
         */
        public KitInfo getKit() {
            return this.kit;
        }

        /**
         * @return Who clicked
         * @author <a href="http://xxyy.github.io/">xxyy</a>
         */
        public Player getPlayer() {
            return this.player;
        }

        /**
         * @return Slot which was clicked (id)
         * @author <a href="http://xxyy.github.io/">xxyy</a>
         */
        public int getPosition() {
            return this.position;
        }

        @Override
        public boolean isCancelled() {
            return this.cancelled;
        }

        @Override
        public void setCancelled(boolean cancel) {
            this.cancelled = cancel;
        }

        /**
         * @param close Whether the inventory will close.
         *
         * @author <a href="http://xxyy.github.io/">xxyy</a>
         */
        public void setWillClose(boolean close) {
            this.close = close;
        }

        /**
         * @param load Whether the kit will be applied to the {@link Player}.
         *
         * @deprecated Use {@link OptionClickEvent#setCancelled(boolean)} instead.
         * @author <a href="http://xxyy.github.io/">xxyy</a>
         */
        @Deprecated
        public void setWillLoad(boolean load) {
            this.cancelled = load;
        }

        /**
         * @return Whether the inventory will close.
         * @author <a href="http://xxyy.github.io/">xxyy</a>
         */
        public boolean willClose() {
            return this.close;
        }

        /**
         * @return Whether the kit will be applied to the {@link Player}.
         * @deprecated Use {@link OptionClickEvent#isCancelled()} instead.
         * @author <a href="http://xxyy.github.io/">xxyy</a>
         */
        @Deprecated
        public boolean willLoad() {
            return this.cancelled;
        }
    }

    /**
     * A class that can handle {@link OptionClickEvent}s.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public interface OptionClickEventHandler {

        /**
         * This method gets invoked if an event is called.
         *
         * @param event Event that was called.
         *
         * @author <a href="http://xxyy.github.io/">xxyy</a>
         */
        public void onOptionClick(OptionClickEvent event);
    }
}
