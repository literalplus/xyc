/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.games.kits.factory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.xxyy.common.XycConstants;
import io.github.xxyy.common.games.kits.Kit;
import io.github.xxyy.common.games.kits.KitManager;

import java.io.IOException;
import java.util.logging.Level;

/**
 * A factory that creates kits. Uses some static messages in German, needs to be refactored to be
 * more reusable. Used by {@link CommandKitFactory}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class KitFactory {
    private ItemStack icon;
    private ItemStack[] contents;
    private ItemStack[] armor;
    private String objective;
    private String authorName;
    private KitManager manager;
    private Kit product;
    private String name;
    private int id;

    /**
     * Constructs a new kit factory.
     *
     * @param name       the name of the new kit
     * @param id         the {@link Kit#getId() sorting id} of the new kit
     * @param authorName the name of the player who is creating the new kit
     * @param manager    the kit manager managing the new kit
     */
    public KitFactory(String name, int id, String authorName, KitManager manager) {
        this.authorName = authorName;
        this.id = id;
        this.manager = manager;
        this.name = name;
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

    /**
     * @return the produced {@link Kit}, or null if not yet produced
     */
    public Kit fetchProduct() {
        return this.product;
    }

    /**
     * @return the armor for the new kit.
     */
    public ItemStack[] getArmor() {
        return this.armor;
    }

    /**
     * @return the name of the player creating the new kit
     */
    public String getAuthorName() {
        return this.authorName;
    }

    /**
     * @return the inventory contents of this kit, excluding armor.
     */
    public ItemStack[] getContents() {
        return this.contents;
    }

    /**
     * @return the icon stack that is used to represent the new kit in inventories
     */
    public ItemStack getIcon() {
        return this.icon;
    }

    /**
     * @param icon the icon to represent the new kit in inventories
     */
    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    /**
     * @return the {@link Kit#getId() sorting id} of the new kit.
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return the manager managing the new kit
     */
    public KitManager getManager() {
        return manager;
    }

    /**
     * @return the objective string for the new kit
     * @see io.github.xxyy.common.games.kits.objective.ObjectiveResolver
     */
    public String getObjective() {
        return objective;
    }

    /**
     * @return the unique file name of the new kit
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return whether this kit can be produced (i.e. steps 1 and 2 have been completed)
     */
    public boolean isCompleted() {
        return this.contents != null && this.icon != null;
    }

    /**
     * @return whether a product is available for retrieval
     */
    public boolean isProduced() {
        return this.product != null;
    }

    /**
     * Sends the completion state of this factory to a player as a checklist
     *
     * @param receiver the receiver of the list
     */
    public void sendCompletionState(Player receiver) {
        receiver.sendMessage(new String[]{
                "§e§l======= §9Kitfabrik: §1" + this.name + " §e§l=======",
                taskCompletionStateLine(contents, "Schritt 1: Inhalt und Rüstung setzen"),
                taskCompletionStateLine(icon, "Schritt 2: Vorschaubild setzen"),
                optionalTaskCompletionStateLine(objective, "Schritt 3: Aufgaben setzen (optional)")
        });
    }

    /**
     * Sets the items for the new kit
     *
     * @param contents the new inventory contents
     * @param armor    the new armor contents
     */
    public void setItems(ItemStack[] contents, ItemStack[] armor) {
        this.contents = contents;
        this.armor = armor;
    }

    /**
     * Sets the objective string for the new kit. The format of the objective string depends on
     * the {@link io.github.xxyy.common.games.kits.objective.ObjectiveResolver}.
     *
     * @param objective the objective string for the new kit
     */
    public void setObjectiveNeeded(String objective) {
        this.objective = objective;
    }

    /**
     * Tries to produce a new kit from the values stored in this factory and sends a message to a
     * player if an error occurs.
     *
     * @param plr the player to receive error messages
     * @return whether the kit has been successfully produced
     */
    public boolean tryProduce(Player plr) {
        if (!this.isCompleted()) {
            plr.sendMessage("§7[Xyg] §cDieses Kit kann noch nicht in Massenproduktion gehen.");
            plr.sendMessage("§7[Xyg] §cErfülle zuerst alle Aufgaben:");
            this.sendCompletionState(plr);
            return false;
        }
        try {
            this.produce();
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, "Error saving a new kit: " + name, e);
            plr.sendMessage("§cDein Kit konnte nicht gespeichert werden: " + e.getMessage());
        }
        plr.sendMessage("§7[Xyg] §cDein Kit geht jetzt in die Massenproduktion! Hype!");
        return true;
    }

    private void produce() throws IOException {
        this.product = manager.getLoader().createKit(
                name, id, icon, contents, armor, objective, authorName
        );
        manager.register(this.product);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.authorName == null) ? 0 : this.authorName.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }
}
