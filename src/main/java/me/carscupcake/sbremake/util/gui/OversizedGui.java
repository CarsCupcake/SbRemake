package me.carscupcake.sbremake.util.gui;

import lombok.Getter;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.*;

@Getter
public class OversizedGui extends Gui {
    private final StartingPosition startingPosition;
    private final List<List<ItemStack>> items;
    private int index;
    private final int up;
    private final int down;
    private int close = -1;
    private final Map<Integer, ItemStack> staticItems = new HashMap<>();

    private OversizedGui(StartingPosition startingPosition, String name, List<List<ItemStack>> items, int up, int down, int index) {
        super(buildInventory(items, index, startingPosition == StartingPosition.Bottom || startingPosition == StartingPosition.Top, new InventoryBuilder(Math.min(items.size(), 6), name).build(), true, up, down, -1, new HashMap<>()));
        this.startingPosition = startingPosition;
        this.items = items;
        this.index = index;
        final boolean vertical = startingPosition == StartingPosition.Bottom || startingPosition == StartingPosition.Top;
        getClickEvents().add(up, ignored -> {
            if (this.index == 0) return true;
            buildInventory(items, --this.index, vertical, getInventory(), false, up, down, close, staticItems);
            return true;
        });
        getClickEvents().add(down, ignored -> {
            if (this.index >= items.size() - (vertical ? 6 : 9)) return true;
            buildInventory(items, ++this.index, vertical, getInventory(), false, up, down, close, staticItems);
            return true;
        });
        this.up = up;
        this.down = down;
    }

    public static OversizedGui createGui(StartingPosition startingPosition, String name, List<List<ItemStack>> items, int up, int down) {
        int index = startingPosition == StartingPosition.Top || startingPosition == StartingPosition.Left ? 0 :
                (startingPosition == StartingPosition.Bottom) ? (items.size() - 6) : (items.size() - 9);
        return new OversizedGui(startingPosition, name, items, up, down, index);
    }

    public static OversizedGui createGui(List<ItemStack> items, StartingPosition startingPosition, String name, int up, int down) {
        List<List<ItemStack>> itemStacks = new ArrayList<>();
        List<ItemStack> itemList = new ArrayList<>();
        for (ItemStack item : items) {
            itemList.add(item);
            if (itemList.size() == 9) {
                itemStacks.add(itemList);
                itemList = new ArrayList<>();
            }
        }
        if (!itemList.isEmpty())
            itemStacks.add(itemList);
        return createGui(startingPosition, name, itemStacks, up, down);
    }

    public void setClose(int index) {
        this.close = index;
        getClickEvents().add(index, ignored -> {
            new HashSet<>(players).forEach(SkyblockPlayer::closeGui);
            return true;
        });
        getInventory().setItemStack(index, new ItemBuilder(Material.BARRIER).setName("§cClose").build());
    }

    public void addStatic(int slot, ItemStack item) {
        staticItems.put(slot, item);
        getInventory().setItemStack(slot, item);
    }

    private static Inventory buildInventory(List<List<ItemStack>> items, int index, boolean vertical, Inventory inventory, boolean newInv, int up, int down, int close, Map<Integer, ItemStack> staticItems) {
        if (items.size() <= 6) {
            if (newInv) {
                int i = 0;
                for (List<ItemStack> list : items)
                    for (ItemStack item : list) {
                        inventory.setItemStack(i, item);
                        i++;
                    }
            }
        } else if (vertical) {
            int j = 0;
            for (int i = index; i < index + 6; i++) {
                for (ItemStack item : items.get(i)) {
                    inventory.setItemStack(j, item);
                    j++;
                }
            }
        } else {
            int j = 0;
            int k = 0;
            for (int i = index; i < index + 9; i++) {
                for (ItemStack item : items.get(i)) {
                    inventory.setItemStack(j + k * 9, item);
                    k++;
                }
                j++;
            }
        }
        if (index != 0)
            inventory.setItemStack(up, new ItemBuilder(Material.ARROW).setName("§aScroll Up").build());
        if (index != items.size() - (vertical ? 6 : 9))
            inventory.setItemStack(down, new ItemBuilder(Material.ARROW).setName("§aScroll Down").build());
        if (close >= 0) {
            inventory.setItemStack(close, new ItemBuilder(Material.BARRIER).setName("§cClose").build());
        }
        for (Map.Entry<Integer, ItemStack> entry : staticItems.entrySet())
            inventory.setItemStack(entry.getKey(), entry.getValue());
        return inventory;
    }

    public enum StartingPosition {
        Bottom,
        Top,
        Left,
        Right
    }
}
