/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.inventory;

import com.google.common.base.Preconditions;
import li.l1t.common.util.inventory.InventoryHelper;

/**
 * Represents the position of a slot in an inventory by its distance from the top left slot. The
 * x axis is horizontal and the y axis is vertical. (0, 0) is the top left slot.
 * <p>This implementation is immutable.</p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-22
 */
public class SlotPosition {
    private final int x;
    private final int y;

    /**
     * Creates a new slot position from its coordinates in the coordinate system.
     *
     * @param x the x coordinate, that is, the horizontal distance from the top left slot
     * @param y the y coordinate, that is, the vertical distance from the top left slot
     */
    private SlotPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new position object from a {@link #toSlotId() slot id}.
     *
     * @param slotId the slot id to create the position from
     * @return the created position
     * @throws IllegalArgumentException if slotId is negative
     */
    public static SlotPosition fromSlotId(int slotId) {
        Preconditions.checkArgument(slotId >= 0, "slotId must not be negative!");
        int y = Math.floorDiv(slotId, InventoryHelper.SLOTS_PER_ROW);
        int x = slotId % InventoryHelper.SLOTS_PER_ROW;
        return new SlotPosition(x, y);
    }

    /**
     * Creates a new position object from its coordinates.
     *
     * @param x the x coordinate, that is, the horizontal distance from the top left slot
     * @param y the y coordinate, that is, the vertical distance from the top left slot
     * @return the created position
     */
    public static SlotPosition ofXY(int x, int y) {
        return new SlotPosition(x, y);
    }

    /**
     * Creates a new position object from its coordinates, validating that it is a valid slot.
     *
     * @param x the x coordinate, that is, the horizontal distance from the top left slot
     * @param y the y coordinate, that is, the vertical distance from the top left slot
     * @return the created position
     * @throws IllegalArgumentException if the represented position is not a
     *                                  {@link #isValidSlot() valid slot}
     */
    public static SlotPosition ofXYValidated(int x, int y) {
        Preconditions.checkArgument(isValidSlot(x, y), "not a valid slot: (%s,%s)", x, y);
        return ofXY(x, y);
    }

    /**
     * Checks whether given coordinates represent a valid slot in a Minecraft double chest with
     * {@value InventoryHelper#SLOTS_PER_ROW} columns and {@value InventoryHelper#MAX_ROW_COUNT}
     * rows.
     *
     * @param x the x coordinate of the slot to check
     * @param y the y coordinate of the slot to check
     * @return whether the slot represented by given coordinates is within the bounds of a double
     * chest
     */
    public static boolean isValidSlot(int x, int y) {
        return x < InventoryHelper.SLOTS_PER_ROW && y < InventoryHelper.MAX_ROW_COUNT;
    }

    /**
     * @return the x coordinate of this position, that is, the horizontal distance from the top
     * left slot.
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y coordinate of this position, that is, the vertical distance from the top
     * left slot.
     */
    public int getY() {
        return y;
    }

    /**
     * Converts this position to a slot id. The top left slot has the id of zero, the one right to
     * that has the id of one, and so on. The slot below the top left slot has the id of nine.
     *
     * @return the slot id corresponding to this position
     */
    public int toSlotId() {
        return (y * InventoryHelper.SLOTS_PER_ROW) + x;
    }

    /**
     * Adds some coordinates to this position.
     *
     * @param xMod the modifier for the x value, may be negative
     * @param yMod the modifier for the y value, may be negative
     * @return the new position
     */
    public SlotPosition add(int xMod, int yMod) {
        return new SlotPosition(x + xMod, y + yMod);
    }

    /**
     * Adds some another position to this position by adding the x and y coordinates.
     *
     * @param position the position to add
     * @return the new position
     */
    public SlotPosition add(SlotPosition position) {
        return new SlotPosition(x + position.getX(), y + position.getY());
    }

    /**
     * Checks whether this position represents a valid slot in a Minecraft double chest.
     *
     * @return whether this position is a valid double chest slot
     * @see SlotPosition#isValidSlot(int, int)
     */
    public boolean isValidSlot() {
        return isValidSlot(x, y);
    }

    /**
     * Checks whether the result of the addition of this slot and given coordinates would result
     * in a {@link #isValidSlot(int, int) valid slot}.
     *
     * @param xMod the modifier for the x value, may be negative
     * @param yMod the modifier for the y value, may be negative
     * @return whether given addition is possible
     */
    public boolean isAdditionPossible(int xMod, int yMod) {
        return isValidSlot(x + xMod, y + yMod);
    }

    /**
     * Checks whether the result of the addition of this slot and given position would result
     * in a {@link #isValidSlot(int, int) valid slot}.
     *
     * @param position the position to add
     * @return whether given addition is possible
     */
    public boolean isAdditionPossible(SlotPosition position) {
        return isValidSlot(x + position.getX(), y + position.getY());
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SlotPosition)) return false;
        SlotPosition that = (SlotPosition) o;
        return x == that.x && y == that.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
