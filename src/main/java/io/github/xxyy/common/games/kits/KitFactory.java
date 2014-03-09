package io.github.xxyy.common.games.kits;

import io.github.xxyy.common.XycConstants;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * A factory that creates kits. Uses some static messages in German, needs to be refactored to be more reusable. Used by {@link CommandKitFactory}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class KitFactory {

    private ItemStack icon = null;
    private ItemStack unavailIcon = null;
    private ItemStack[] contents = null;
    private ItemStack[] armor;
    private String objectiveNeeded = null;
    private String objectiveNeededAmount = null;
    private String authorName = null;
    private KitLoader ldr = null;
    private KitInfo product = null;
    private String name;
    private int id;

    /**
     * Makes a new {@link KitFactory}.
     *
     * @param name       Name of the kit to be produced.
     * @param id         ID to use for ordering the kit. (always pass 0 to make it random; This is discouraged however.)
     * @param authorName Who created the kit. For use with {@link CommandSender#getName()}.
     * @param ldr        {@link KitLoader} to use.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public KitFactory(String name, int id, String authorName, KitLoader ldr) {
        this.authorName = authorName;
        this.id = id;
        this.ldr = ldr;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof KitFactory)) {
            return false;
        }
        KitFactory other = (KitFactory) obj;
        if (this.authorName == null) {
            if (other.authorName != null) {
                return false;
            }
        } else if (!this.authorName.equals(other.authorName)) {
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
     * @return A produced {@link KitInfo}. <code>null</code> if not produced.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public KitInfo fetchProduct() {
        return this.product;
    }

    /**
     * @return The armor for this kit.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public ItemStack[] getArmor() {
        return this.armor;
    }

    /**
     * @return Who authored this kit?
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public String getAuthorName() {
        return this.authorName;
    }

    /**
     * @return The inventory contents of this kit, excluding armor.
     * @see KitFactory#getArmor()
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public ItemStack[] getContents() {
        return this.contents;
    }

    /**
     * @return The icon that is used to represent tis KitFactory's kit in a {@link KitSelector}.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public ItemStack getIcon() {
        return this.icon;
    }

    /**
     * @return Gets the ID of the kit to be produced.
     * @see KitFactory#KitFactory(String, int, String, KitLoader)
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return Gets the {@link KitLoader} of the kit to be produced.
     * @see KitFactory#KitFactory(String, int, String, KitLoader)
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public KitLoader getLdr() {
        return this.ldr;
    }

    /**
     * @return Gets the name of the kit to be produced.
     * @see KitFactory#KitFactory(String, int, String, KitLoader)
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The name of the objective that needs to be fulfilled by an user in order to use this kit.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public String getObjectiveNeeded() {
        return this.objectiveNeeded;
    }

    /**
     * @return The amount of the objective that needs to be fulfilled by an user in order to use this kit. What this is is objective-implementation
     *         specific.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public String getObjectiveNeededAmount() {
        return this.objectiveNeededAmount;
    }

    /**
     * @return The icon that is used to represent tis KitFactory's kit in a {@link KitSelector} if the objective is not fulfilled by an user.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public ItemStack getUnavailIcon() {
        return this.unavailIcon;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.authorName == null) ? 0 : this.authorName.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    /**
     * @return Whether this kit is ready to be produced (i.e. steps 1 and 2 have been completed)
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public boolean isCompleted() {
        return this.contents != null && this.icon != null;
    }

    /**
     * @return If a product is available for retrieval.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public boolean isProduced() {
        return this.product != null;
    }

    /**
     * @return Whether step 3 is completed (needed for step 4)
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public boolean isStep3Complete() {
        return this.unavailIcon != null;
    }

    /**
     * Sends the completion state of this {@link KitFactory} to a {@link Player} as a ToDo List.
     *
     * @param plr Player to receive the list.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public void sendCompletionState(Player plr) {
        plr.sendMessage(new String[] {
            "§e§l======= §9KitFactory-ToDo-Liste: §1" + this.name + " §e§l=======",
            KitFactory.taskCompletionStateLine(this.contents, "Schritt 1: Inhalt und Rüstung setzen"),//armor and contents can only be set together
            KitFactory.taskCompletionStateLine(this.icon, "Schritt 2: Vorschaubild setzen"),
            KitFactory.optionalTaskCompletionStateLine(this.unavailIcon, "Schritt 3: Vorschaubild 2 setzen (Kit nicht verfügbar; optional)"),
            KitFactory.optionalTaskCompletionStateLine(this.objectiveNeeded, "Schritt 4: Aufgaben setzen (optional)")
        });
    }

    /**
     * Sets the armor of the kit.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public void setArmor(ItemStack[] armor) {
        this.armor = armor;
    }

    /**
     * Sets the contents of the kit, excluding armor.
     *
     * @see KitFactory#setArmor(ItemStack[])
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public void setContents(ItemStack[] contents) {
        this.contents = contents;
    }

    /**
     * Sets the icon to be displayed in a {@link KitLoader} to represent the kit to be produced.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    /**
     * Sets the name of the objective to be completed by an user in order to use this kit.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public void setObjectiveNeeded(String objectiveNeeded) {
        this.objectiveNeeded = objectiveNeeded;
    }

    /**
     * Sets the amount of the objective to be completed by an user in order to use this kit. What this is is objective-implementation-specific.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public void setObjectiveNeededAmount(String objectiveNeededAmount) {
        this.objectiveNeededAmount = objectiveNeededAmount;
    }

    /**
     * Sets the icon to be displayed in a {@link KitLoader} to represent the kit to be produced if the objective is not fulfilled by an user.
     *
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public void setUnavailIcon(ItemStack unavailIcon) {
        this.unavailIcon = unavailIcon;
    }

    /**
     * Tries to produce a new kit from the values stored in this {@link KitFactory} and sends a message if this is not possible.
     *
     * @param plr Player to receive any messages (in German).
     *
     * @return Whether the kit was produced successfully.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public boolean tryProduce(Player plr) {
        if (!this.isCompleted()) {
            plr.sendMessage("§7[Xyg] §cDieses Kit kann noch nicht in Massenproduktion gehen.");
            plr.sendMessage("§7[Xyg] §cErfülle zuerst alle Aufgaben:");
            this.sendCompletionState(plr);
            return false;
        }
        this.produce();
        plr.sendMessage("§7[Xyg] §cDein Kit geht jetzt in die Massenproduktion!!");
        return true;
    }

    private void produce() {
        this.product = this.ldr.register(this.name, this.icon, this.unavailIcon,
                this.contents, this.armor, this.objectiveNeeded,
                this.objectiveNeededAmount, this.authorName, this.id);
    }

    private static String optionalTaskCompletionStateLine(Object indicator, String label) {
        if (indicator == null) {
            return " §e" + XycConstants.BALLOT_X + " §6§o" + label;
        }
        return " §a" + XycConstants.CHECK_MARK + " §2" + label;
    }

    private static String taskCompletionStateLine(Object indicator, String label) {
        if (indicator == null) {
            return " §c" + XycConstants.BALLOT_X + " §4§l" + label;
        }
        return " §a" + XycConstants.CHECK_MARK + " §2" + label;
    }
}
