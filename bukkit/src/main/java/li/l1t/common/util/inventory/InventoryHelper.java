/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package li.l1t.common.util.inventory;

import com.google.common.base.Preconditions;
import li.l1t.common.inventory.SlotPosition;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Static utility class that helps when dealing with {@link org.bukkit.inventory.Inventory}s.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 30/01/14
 */
@SuppressWarnings("UnusedDeclaration")
public final class InventoryHelper {
    /**
     * Defines how many slots there are in each row in a fake inventory. (Not changeable, its a
     * constant by minecraft implemntation)
     *
     * @deprecated prefer {@link SlotPosition#SLOTS_PER_ROW} from the public XYC-API
     */
    @Deprecated
    public static final int SLOTS_PER_ROW = SlotPosition.SLOTS_PER_ROW;

    /**
     * The maximum amount of rows in an inventory.
     *
     * @deprecated prefer {@link SlotPosition#MAX_ROW_COUNT} from the public XYC-API
     */
    @Deprecated
    public static final int MAX_ROW_COUNT = SlotPosition.MAX_ROW_COUNT;

    /**
     * Default maximum fake inventory size. By default represents a double chest.
     */
    public static final int DEFAULT_MAX_INVENTORY_SIZE = SlotPosition.SLOTS_PER_ROW * SlotPosition.MAX_ROW_COUNT;
    /**
     * Maximum amount of items allowed in armor slots
     */
    public static final int ARMOR_SIZE = 4;
    /**
     * Default amount of slots in a player inventory
     *
     * @see InventoryType#PLAYER
     */
    public static final int PLAYER_INV_SIZE = InventoryType.PLAYER.getDefaultSize();

    public static final String ACTION_PREFIX_PLACE = "PLACE_";
    public static final String ACTION_PREFIX_HOTBAR = "HOTBAR_";
    public static final String ACTION_PREFIX_DROP = "DROP_";
    public static final String ACTION_PREFIX_PICKUP = "PICKUP_";

    private InventoryHelper() {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Makes sure that an inventory size is a multiple of {@link SlotPosition#SLOTS_PER_ROW} and
     * will not fail Bukkit's inventory creation methods.
     *
     * @param minimumSize minimum size the inventory needs to be
     * @param maximumSize maximum size of the inventory. Must be a multiple of {@link SlotPosition#SLOTS_PER_ROW}.
     * @return The multiple of {@link SlotPosition#SLOTS_PER_ROW} that is closest to {@code minimumSize} and is greater
     * than or equal to {@code minimumSize}. Will never exceed {@code maximumSize}.
     * @throws IllegalArgumentException If {@code maximumSize} is not a multiple of {@link SlotPosition#SLOTS_PER_ROW}
     * @see InventoryHelper#validateInventorySize(int, int)
     */
    public static int validateInventorySize(int minimumSize, int maximumSize) {
        Preconditions.checkArgument((maximumSize % SlotPosition.SLOTS_PER_ROW) == 0,
                "maximumSize must be a multiple of SLOTS_PER_ROW", maximumSize, SlotPosition.SLOTS_PER_ROW);

        int outputSize = minimumSize; //Reassigning parameters is considered bad practice

        if (minimumSize > maximumSize) {
            outputSize = maximumSize;
        } else if (minimumSize % SlotPosition.SLOTS_PER_ROW != 0) {
            outputSize = minimumSize + (SlotPosition.SLOTS_PER_ROW - (minimumSize % SlotPosition.SLOTS_PER_ROW));
        }

        return outputSize;
    }

    /**
     * Makes sure that an inventory size is a multiple of {@link SlotPosition#SLOTS_PER_ROW} and
     * will not fail Bukkit's inventory creation methods. This implementation uses {@link
     * InventoryHelper#DEFAULT_MAX_INVENTORY_SIZE} as maximum size.
     *
     * @param minimumSize minimum size the inventory needs to be
     * @return The multiple of {@link SlotPosition#SLOTS_PER_ROW} that is closest to {@code minimumSize} and is
     * greater than or equal to {@code minimumSize}. Will never exceed {@link InventoryHelper#DEFAULT_MAX_INVENTORY_SIZE}.
     * @see InventoryHelper#validateInventorySize(int, int)
     */
    public static int validateInventorySize(final int minimumSize) {
        return validateInventorySize(minimumSize, DEFAULT_MAX_INVENTORY_SIZE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Closes a {@link org.bukkit.entity.HumanEntity}'s open inventory (if any) later by running a
     * task later with Bukkit's Scheduler API.
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
     * Closes a {@link org.bukkit.entity.HumanEntity}'s open inventory (if any) later by running a
     * task later with Bukkit's Scheduler API. Convenience method, uses 1L as delay (in ticks), as
     * this is used very often.
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
     * Opens an Inventory to a  {@link org.bukkit.entity.HumanEntity} later by running a task later
     * with Bukkit's Scheduler API.
     *
     * @param delay       Time, in ticks, to wait before the action is executed
     * @param humanEntity HumanEntity to target
     * @param inventory   the inventory to open
     * @param plugin      the plugin to register the task with
     * @see org.bukkit.entity.HumanEntity#openInventory(org.bukkit.inventory.Inventory)
     * @see InventoryHelper#openInventoryLater(org.bukkit.entity.HumanEntity, org.bukkit.inventory.Inventory,
     * org.bukkit.plugin.Plugin)
     */
    public static void openInventoryLater(final HumanEntity humanEntity, final Inventory inventory, final long delay, final Plugin plugin) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> humanEntity.openInventory(inventory), delay);
    }

    /**
     * Opens an Inventory to a  {@link org.bukkit.entity.HumanEntity} later by running a task later
     * with Bukkit's Scheduler API. Convenience method, uses 1L as delay (in ticks), as this is used
     * very often.
     *
     * @param humanEntity HumanEntity to target
     * @param inventory   the inventory to open
     * @param plugin      the plugin to register the task with
     * @see org.bukkit.entity.HumanEntity#openInventory(org.bukkit.inventory.Inventory)
     * @see InventoryHelper#openInventoryLater(org.bukkit.entity.HumanEntity, org.bukkit.inventory.Inventory, long,
     * org.bukkit.plugin.Plugin)
     */
    public static void openInventoryLater(final HumanEntity humanEntity, final Inventory inventory, final Plugin plugin) {
        openInventoryLater(humanEntity, inventory, 1L, plugin);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks if a given {@link org.bukkit.event.inventory.InventoryAction} is representing some
     * kind of pickup (i.e. its enum constant name starts with {@value
     * InventoryHelper#ACTION_PREFIX_PICKUP})
     *
     * @param action Action to check
     * @return Whether {@code action} 's name starts with {@value InventoryHelper#ACTION_PREFIX_PICKUP})
     */
    public static boolean isPickupAction(final InventoryAction action) {
        return action.name().startsWith(ACTION_PREFIX_PICKUP);
    }

    /**
     * Checks if a given {@link org.bukkit.event.inventory.InventoryAction} is representing some
     * kind of drop (i.e. its enum constant name starts with {@value InventoryHelper#ACTION_PREFIX_DROP}))
     *
     * @param action Action to check
     * @return Whether {@code action} 's name starts with {@value InventoryHelper#ACTION_PREFIX_DROP})
     */
    public static boolean isDropAction(final InventoryAction action) {
        return action.name().startsWith(ACTION_PREFIX_DROP);
    }

    /**
     * Checks if a given {@link org.bukkit.event.inventory.InventoryAction} is Hotbar-related (i.e.
     * its enum constant name starts with {@value InventoryHelper#ACTION_PREFIX_HOTBAR}))
     *
     * @param action Action to check
     * @return Whether {@code action} 's name starts with {@value InventoryHelper#ACTION_PREFIX_HOTBAR})
     */
    public static boolean isHotbarAction(final InventoryAction action) {
        return action.name().startsWith(ACTION_PREFIX_HOTBAR);
    }

    /**
     * Checks if a given {@link org.bukkit.event.inventory.InventoryAction} is representing some
     * kind of place (i.e. its enum constant name starts with {@value InventoryHelper#ACTION_PREFIX_PLACE}))
     *
     * @param action Action to check
     * @return Whether {@code action} 's name starts with {@value InventoryHelper#ACTION_PREFIX_PLACE})
     */
    public static boolean isPlaceAction(final InventoryAction action) {
        return action.name().startsWith(ACTION_PREFIX_PLACE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks if a Player's Inventory is currently empty (i.e. all slots, including armor, are
     * either null or Material.AIR)
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
     */
    public static void clearInventories(final Collection<Player> plrs) {
        plrs.forEach(InventoryHelper::clearInventory);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Clones an ItemStack and sets its amount to 1 if it was 0 previously (this prevents some weird
     * glitches).
     *
     * @param input the stack to clone
     * @return a clone of the input, with non-zero amount
     */
    public static ItemStack cloneSafely(@Nullable ItemStack input) {
        if (input != null && input.getAmount() == 0) {
            input.setAmount(1);
        }
        return input == null ? null : input.clone();
    }

    /**
     * Clones all stacks in the input and sets their amount to 1 if it was 0. This is here because
     * Bukkit sometimes serializes ItemStacks with amount=0.
     *
     * @param input the array containing the stacks to copy
     * @return an array of clones of the input stacks
     */
    public static ItemStack[] cloneAll(ItemStack[] input) {
        ItemStack[] result = new ItemStack[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = cloneSafely(input[i]);
        }
        return result;
    }

    /**
     * Clones all stacks in the input and sets their amount to 1 if it was 0. This is here because
     * Bukkit sometimes serializes ItemStacks with amount=0.
     *
     * @param input a collection containing the stacks to copy
     * @return an array of clones of the input stacks
     */
    public static List<ItemStack> cloneAll(Collection<ItemStack> input) {
        return input.stream()
                .map(InventoryHelper::cloneSafely)
                .collect(Collectors.toList());
    }
}
