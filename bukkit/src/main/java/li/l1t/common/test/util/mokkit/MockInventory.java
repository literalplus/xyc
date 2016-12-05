/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.test.util.mokkit;

import li.l1t.common.util.PredicateHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A mock of a custom inventory.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-14
 */
public class MockInventory implements Inventory {
    private int size;
    private int maxStackSize = 64;
    private String name;
    private ItemStack[] contents;
    private InventoryHolder holder;

    public MockInventory(int size, String name, InventoryHolder holder) {
        this.size = size;
        this.name = name;
        this.holder = holder;
        this.contents = new ItemStack[size];
    }

    @Override
    public ItemStack[] getStorageContents() {
        return contents;
    }

    @Override
    public void setStorageContents(ItemStack[] itemStacks) throws IllegalArgumentException {
        setContents(itemStacks);
    }

    @Override
    public Location getLocation() {
        throw new UnsupportedOperationException("MockInventory#getLocation()");
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }

    @Override
    public void setMaxStackSize(int size) {
        this.maxStackSize = size;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemStack getItem(int index) {
        if (index >= size) {
            throw new IllegalArgumentException("index must be < size; given: " + index + ", size=" + getSize());
        }
        return contents[index];
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (item.getAmount() > getMaxStackSize()) {
            throw new UnsupportedOperationException("can't put stacks > max size: " + item);
        }
        contents[index] = item;
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
        HashMap<Integer, ItemStack> unput = new HashMap<>(items.length);
        for (int i = 0; i < items.length; i++) {
            unput.put(i, items[i]);
        }
        List<ItemStack> toPut = new ArrayList<>(unput.values());
        for (int i = 0; i < getSize(); i++) {
            if (toPut.isEmpty()) {
                break;
            }
            ItemStack blockingItem = contents[i];
            if (blockingItem == null) {
                ItemStack firstStack = toPut.remove(0);
                setItem(i, firstStack);
            } else {
                Optional<ItemStack> first = toPut.stream()
                        .filter(item -> item.getType() == blockingItem.getType())
                        .findFirst();
                if (first.isPresent()) {
                    ItemStack newStack = first.get();
                    newStack.setAmount(newStack.getAmount() + blockingItem.getAmount());
                    setItem(i, newStack);
                }
            }
        }
        unput.values().removeIf(PredicateHelper.not(toPut::contains));
        return unput;
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
        HashMap<Integer, ItemStack> nonRemoved = new HashMap<>(items.length);
        for (int i = 0; i < items.length; i++) {
            nonRemoved.put(i, items[i]);
        }
        ArrayList<ItemStack> toRemove = new ArrayList<>(nonRemoved.values());
        for (int i = 0; i < getSize(); i++) {
            if (toRemove.isEmpty()) {
                break;
            }
            ItemStack currentItem = contents[i];
            if (currentItem == null) {
                continue;
            }
            Optional<ItemStack> first = toRemove.stream()
                    .filter(stack -> stack.getType() == currentItem.getType())
                    .findFirst();
            if (!first.isPresent()) {
                continue;
            }
            ItemStack removalCandidate = first.get();
            if (removalCandidate.getAmount() > currentItem.getAmount()) {
                clear(i);
                removalCandidate.setAmount(removalCandidate.getAmount() - currentItem.getAmount());
            } else if (removalCandidate.getAmount() == currentItem.getAmount()) {
                clear(i);
                toRemove.remove(removalCandidate);
            } else {
                toRemove.remove(removalCandidate);
                currentItem.setAmount(currentItem.getAmount() - removalCandidate.getAmount());
            }
        }
        nonRemoved.values().removeIf(PredicateHelper.not(toRemove::contains));
        return nonRemoved;
    }

    @Override
    public ItemStack[] getContents() {
        return contents;
    }

    @Override
    public void setContents(ItemStack[] items) throws IllegalArgumentException {
        this.contents = items;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean contains(int materialId) {
        return contains(materialId, 1);
    }

    @Override
    public boolean contains(Material material) throws IllegalArgumentException {
        return contains(material, 1);
    }

    @Override
    public boolean contains(ItemStack item) {
        return contains(item, 1);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean contains(int materialId, int amount) {
        return contentStream().anyMatch(itemStack ->
                itemStack.getTypeId() == materialId && itemStack.getAmount() >= amount
        );
    }

    @NotNull
    private Stream<ItemStack> contentStream() {
        return Arrays.stream(contents).filter(Objects::nonNull);
    }

    @Override
    public boolean contains(Material material, int amount) throws IllegalArgumentException {
        return contentStream().anyMatch(itemStack ->
                itemStack.getType() == material && itemStack.getAmount() >= amount
        );
    }

    @Override
    public boolean contains(ItemStack item, int amount) {
        return contentStream().filter(item::equals).count() >= amount;
    }

    @Override
    public boolean containsAtLeast(ItemStack item, int amount) {
        return contentStream()
                .filter(itemStack -> itemStack.getType() == item.getType())
                .mapToInt(ItemStack::getAmount)
                .sum() >= amount;
    }

    @Override
    @SuppressWarnings("deprecation")
    public HashMap<Integer, ? extends ItemStack> all(int materialId) {
        HashMap<Integer, ItemStack> result = new HashMap<>();
        for (int i = 0; i < getSize(); i++) {
            ItemStack content = contents[i];
            if (content != null && content.getTypeId() == materialId) {
                result.put(i, content);
            }
        }
        return result;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
        HashMap<Integer, ItemStack> result = new HashMap<>();
        for (int i = 0; i < getSize(); i++) {
            ItemStack content = contents[i];
            if (content != null && content.getType() == material) {
                result.put(i, content);
            }
        }
        return result;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(ItemStack item) {
        HashMap<Integer, ItemStack> result = new HashMap<>();
        for (int i = 0; i < getSize(); i++) {
            ItemStack content = contents[i];
            if (content != null && item.equals(content)) {
                result.put(i, content);
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int first(int materialId) {
        for (int i = 0; i < getSize(); i++) {
            ItemStack content = contents[i];
            if (content != null && content.getTypeId() == materialId) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int first(Material material) throws IllegalArgumentException {
        for (int i = 0; i < getSize(); i++) {
            ItemStack content = contents[i];
            if (content != null && content.getType() == material) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int first(ItemStack item) {
        for (int i = 0; i < getSize(); i++) {
            ItemStack content = contents[i];
            if (content != null && content.equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int firstEmpty() {
        for (int i = 0; i < getSize(); i++) {
            ItemStack content = contents[i];
            if (content == null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void remove(int materialId) {
        for (int i = 0; i < getSize(); i++) {
            ItemStack content = contents[i];
            if (content != null && content.getTypeId() == materialId) {
                clear(i);
            }
        }
    }

    @Override
    public void remove(Material material) throws IllegalArgumentException {
        for (int i = 0; i < getSize(); i++) {
            ItemStack content = contents[i];
            if (content != null && content.getType() == material) {
                clear(i);
            }
        }
    }

    @Override
    public void remove(ItemStack item) {
        for (int i = 0; i < getSize(); i++) {
            ItemStack content = contents[i];
            if (content != null && content.equals(item)) {
                clear(i);
            }
        }
    }

    @Override
    public void clear(int index) {
        setItem(index, null);
    }

    @Override
    public void clear() {
        contents = new ItemStack[getSize()];
    }

    @Override
    public List<HumanEntity> getViewers() {
        return Collections.emptyList();
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public InventoryHolder getHolder() {
        return holder;
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        return Arrays.asList(contents).listIterator();
    }

    @Override
    public ListIterator<ItemStack> iterator(int index) {
        return Arrays.asList(contents).listIterator(index);
    }
}
