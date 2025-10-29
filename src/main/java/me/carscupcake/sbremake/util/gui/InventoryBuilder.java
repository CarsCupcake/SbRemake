package me.carscupcake.sbremake.util.gui;

import me.carscupcake.sbremake.item.ISbItem;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class InventoryBuilder {
    private final int rows;
    private final List<ItemStack> itemStacks;
    private final String name;

    private InventoryBuilder(int rows, List<ItemStack> itemStacks, String name) {
        this.rows = rows;
        this.itemStacks = itemStacks;
        this.name = name;
    }

    public InventoryBuilder(@Range(from = 1, to = 6) int rows, String name) {
        this.rows = rows;
        itemStacks = new LinkedList<>();
        this.name = name;
        init();
    }

    public InventoryBuilder(Inventory inventory, String name) {
        itemStacks = new ArrayList<>(Arrays.asList(inventory.getItemStacks()));
        this.name = name;
        rows = inventory.getSize() / 9;
    }

    private void init() {
        if (itemStacks.isEmpty()) {
            for (int i = 0; i < rows * 9; i++)
                itemStacks.add(ItemStack.of(Material.AIR));
        }
    }

    public Inventory build() {
        InventoryType type = switch (rows) {
            case 1 -> InventoryType.CHEST_1_ROW;
            case 2 -> InventoryType.CHEST_2_ROW;
            case 3 -> InventoryType.CHEST_3_ROW;
            case 4 -> InventoryType.CHEST_4_ROW;
            case 5 -> InventoryType.CHEST_5_ROW;
            case 6 -> InventoryType.CHEST_6_ROW;
            default -> throw new IllegalStateException((rows) + " rows are illigal");
        };
        Inventory inventory = new Inventory(type, name);
        int i = 0;
        for (ItemStack it : itemStacks) {
            inventory.setItemStack(i, it);
            i++;
        }
        return inventory;
    }

    public InventoryBuilder fill(ItemStack item, int v1, int v2) {
        for (int i = v1; i < v2 + 1; i++)
            itemStacks.set(i, item);
        return this;
    }

    public InventoryBuilder fill(ItemStack item) {
        return fill(item, 0, rows * 9 - 1);

    }

    public InventoryBuilder fill(ISbItem manager, int v1, int v2) {
        return fill(manager.create().item(), v1, v2);
    }

    public InventoryBuilder fill(ISbItem manager) {
        return fill(manager.create().item());
    }

    public InventoryBuilder setItem(ItemStack item, int i) {
        itemStacks.set(i, item);
        return this;
    }

    public InventoryBuilder setItem(int i, ItemStack item) {
        itemStacks.set(i, item);
        return this;
    }

    public InventoryBuilder setItem(ISbItem item, int i) {
        return setItem(item.create().item(), i);
    }

    public InventoryBuilder setItems(ItemStack[] item, int i) {
        for (ItemStack stack : item) {
            itemStacks.set(i, stack);
            i++;
        }
        return this;
    }

    public InventoryBuilder setItems(ItemStack item, int... i) {
        for (int t : i)
            setItem(item, t);
        return this;
    }

    public InventoryBuilder setItems(ISbItem[] item, int i) {
        ArrayList<ItemStack> items = new ArrayList<>(item.length);
        for (ISbItem itemManager : item)
            items.add(itemManager.create().item());

        return setItems(items.toArray(new ItemStack[0]), i);
    }

    public InventoryBuilder verticalFill(int start, int rows, ItemStack stack) {
        for (int i = start; i < (rows * 9) + start; i += 9) {
            setItem(stack, i);
        }
        return this;
    }

    public InventoryBuilder copy() {
        return new InventoryBuilder(rows, new ArrayList<>(itemStacks), name);
    }
}
