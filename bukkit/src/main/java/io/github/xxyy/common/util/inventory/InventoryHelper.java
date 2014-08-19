/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.util.inventory;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import io.github.xxyy.common.util.CommandHelper;

import java.util.Collection;

/**
 * Static utility class that helps when dealing with {@link org.bukkit.inventory.Inventory}s.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 30/01/14
 */
@SuppressWarnings("UnusedDeclaration")
public final class InventoryHelper {
    /**
     * Defines how many slots there are in each row in a fake inventory. (Not changeable, its a constant by minecraft implemntation)
     */
    public static final int SLOTS_PER_ROW = 9;

    /**
     * Default maximum fake inventory size.
     * Default is 4 rows of slots.
     */
    public static final int DEFAULT_MAX_INVENTORY_SIZE = SLOTS_PER_ROW * 4;
    public static final String ACTION_PREFIX_PLACE = "PLACE_";
    public static final String ACTION_PREFIX_HOTBAR = "HOTBAR_";
    public static final String ACTION_PREFIX_DROP = "DROP_";
    public static final String ACTION_PREFIX_PICKUP = "PICKUP_";

    private InventoryHelper() {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Makes sure that an inventory size is a multiple of {@link InventoryHelper#SLOTS_PER_ROW}
     * and will not fail Bukkit's inventory creation methods.
     *
     * @param minimumSize minimum size the inventory needs to be
     * @param maximumSize maximum size of the inventory. Must be a multiple of {@link InventoryHelper#SLOTS_PER_ROW}.
     * @return The multiple of {@link InventoryHelper#SLOTS_PER_ROW} that is closest to
     * {@code minimumSize} and is greater than or equal to {@code minimumSize}. Will never exceed {@code maximumSize}.
     * @throws IllegalArgumentException If {@code maximumSize} is not a multiple of {@link InventoryHelper#SLOTS_PER_ROW}
     * @see InventoryHelper#validateInventorySize(int, int)
     */
    public static int validateInventorySize(final int minimumSize, final int maximumSize) {
        Validate.isTrue((maximumSize % SLOTS_PER_ROW) == 0, "maximumSize must be a multiple of SLOTS_PER_ROW (=" + SLOTS_PER_ROW + ')');

        int outputSize = minimumSize; //Reassigning parameters is considered bad practice

        if (minimumSize > maximumSize) {
            outputSize = maximumSize;
        } else if (minimumSize % SLOTS_PER_ROW != 0) {
            outputSize = minimumSize + (SLOTS_PER_ROW - (minimumSize % SLOTS_PER_ROW));
        }

        return outputSize;
    }

    /**
     * Makes sure that an inventory size is a multiple of {@link InventoryHelper#SLOTS_PER_ROW}
     * and will not fail Bukkit's inventory creation methods.
     * This implementation uses {@link InventoryHelper#DEFAULT_MAX_INVENTORY_SIZE} as maximum size.
     *
     * @param minimumSize minimum size the inventory needs to be
     * @return The multiple of {@link InventoryHelper#SLOTS_PER_ROW} that is closest to
     * {@code minimumSize} and is greater than or equal to {@code minimumSize}. Will never exceed {@link InventoryHelper#DEFAULT_MAX_INVENTORY_SIZE}.
     * @see InventoryHelper#validateInventorySize(int, int)
     */
    public static int validateInventorySize(final int minimumSize) {
        return validateInventorySize(minimumSize, DEFAULT_MAX_INVENTORY_SIZE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Closes a {@link org.bukkit.entity.HumanEntity}'s open inventory (if any) later by running a task later with Bukkit's Scheduler API.
     *
     * @param humanEntity HumanEntity to target
     * @param delay       Time, in ticks, to wait before the inventory is closed
     * @param plugin      the plugin to register the task with
     * @see org.bukkit.entity.HumanEntity#closeInventory()
     * @see InventoryHelper#closeInventoryLater(org.bukkit.entity.HumanEntity, org.bukkit.plugin.Plugin)
     */
    public static void closeInventoryLater(final HumanEntity humanEntity, final long delay, final Plugin plugin) {
        Bukkit.getScheduler().runTaskLater(plugin, humanEntity::closeInventory, delay);
    }

    /**
     * Closes a {@link org.bukkit.entity.HumanEntity}'s open inventory (if any) later by running a task later with Bukkit's Scheduler API.
     * Convenience method, uses 1L as delay (in ticks), as this is used very often.
     *
     * @param humanEntity HumanEntity to target
     * @param plugin      the plugin to register the task with
     * @see org.bukkit.entity.HumanEntity#closeInventory()
     * @see InventoryHelper#closeInventoryLater(org.bukkit.entity.HumanEntity, long, org.bukkit.plugin.Plugin)
     */
    public static void closeInventoryLater(final HumanEntity humanEntity, final Plugin plugin) {
        closeInventoryLater(humanEntity, 1L, plugin);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Opens an Inventory to a  {@link org.bukkit.entity.HumanEntity} later by running a task later with Bukkit's Scheduler API.
     *
     * @param delay       Time, in ticks, to wait before the action is executed
     * @param humanEntity HumanEntity to target
     * @param inventory   the inventory to open
     * @param plugin      the plugin to register the task with
     * @see org.bukkit.entity.HumanEntity#openInventory(org.bukkit.inventory.Inventory)
     * @see InventoryHelper#openInventoryLater(org.bukkit.entity.HumanEntity, org.bukkit.inventory.Inventory, org.bukkit.plugin.Plugin)
     */
    public static void openInventoryLater(final HumanEntity humanEntity, final Inventory inventory, final long delay, final Plugin plugin) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> humanEntity.openInventory(inventory), delay);
    }

    /**
     * Opens an Inventory to a  {@link org.bukkit.entity.HumanEntity} later by running a task later with Bukkit's Scheduler API.
     * Convenience method, uses 1L as delay (in ticks), as this is used very often.
     *
     * @param humanEntity HumanEntity to target
     * @param inventory   the inventory to open
     * @param plugin      the plugin to register the task with
     * @see org.bukkit.entity.HumanEntity#openInventory(org.bukkit.inventory.Inventory)
     * @see InventoryHelper#openInventoryLater(org.bukkit.entity.HumanEntity, org.bukkit.inventory.Inventory, long, org.bukkit.plugin.Plugin)
     */
    public static void openInventoryLater(final HumanEntity humanEntity, final Inventory inventory, final Plugin plugin) {
        openInventoryLater(humanEntity, inventory, 1L, plugin);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks if a given {@link org.bukkit.event.inventory.InventoryAction} is representing some kind of pickup
     * (i.e. its enum constant name starts with {@value InventoryHelper#ACTION_PREFIX_PICKUP})
     *
     * @param action Action to check
     * @return Whether {@code action} 's name starts with {@value InventoryHelper#ACTION_PREFIX_PICKUP})
     */
    public static boolean isPickupAction(final InventoryAction action) {
        return action.name().startsWith(ACTION_PREFIX_PICKUP);
    }

    /**
     * Checks if a given {@link org.bukkit.event.inventory.InventoryAction} is representing some kind of drop
     * (i.e. its enum constant name starts with {@value InventoryHelper#ACTION_PREFIX_DROP}))
     *
     * @param action Action to check
     * @return Whether {@code action} 's name starts with {@value InventoryHelper#ACTION_PREFIX_DROP})
     */
    public static boolean isDropAction(final InventoryAction action) {
        return action.name().startsWith(ACTION_PREFIX_DROP);
    }

    /**
     * Checks if a given {@link org.bukkit.event.inventory.InventoryAction} is Hotbar-related
     * (i.e. its enum constant name starts with {@value InventoryHelper#ACTION_PREFIX_HOTBAR}))
     *
     * @param action Action to check
     * @return Whether {@code action} 's name starts with {@value InventoryHelper#ACTION_PREFIX_HOTBAR})
     */
    public static boolean isHotbarAction(final InventoryAction action) {
        return action.name().startsWith(ACTION_PREFIX_HOTBAR);
    }

    /**
     * Checks if a given {@link org.bukkit.event.inventory.InventoryAction} is representing some kind of place
     * (i.e. its enum constant name starts with {@value InventoryHelper#ACTION_PREFIX_PLACE}))
     *
     * @param action Action to check
     * @return Whether {@code action} 's name starts with {@value InventoryHelper#ACTION_PREFIX_PLACE})
     */
    public static boolean isPlaceAction(final InventoryAction action) {
        return action.name().startsWith(ACTION_PREFIX_PLACE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks if a Player's Inventory is currently empty (i.e. all slots, including armor, are either null or Material.AIR)
     *
     * @param plr Player to check
     * @return whether the Player's Inventory is currently empty.
     */
    public static boolean isInventoryEmpty(Player plr) {
        for (ItemStack item : plr.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                return false;
            }
        }
        for (ItemStack item : plr.getInventory().getArmorContents()) {
            if (item != null && item.getType() != Material.AIR) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clears the Inventory of a provided {@link org.bukkit.entity.Player}, including armor slots.
     *
     * @param plr whose Inventory to clear
     */
    public static void clearInventory(Player plr) {
        plr.getInventory().clear();
        plr.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
    }

    /**
     * Clears a list of player inventories, including armor slots.
     *
     * @param plrs target players
     * @see CommandHelper#clearInv(org.bukkit.entity.Player)
     */
    public static void clearInventories(final Collection<Player> plrs) {
        plrs.forEach(InventoryHelper::clearInventory);
    }
}
