package me.carscupcake.sbremake.util.gui;

import lombok.Getter;
import me.carscupcake.sbremake.util.TemplateItems;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;

import java.util.List;

@Getter
public class PageGui extends Gui {
    private final List<Inventory> inventories;
    private int page;
    private final ItemSlotPosition nextItemSlot;
    private final ItemSlotPosition previousItemSlot;
    public PageGui(List<Inventory> inventory, ItemSlotPosition nextItemSlot, ItemSlotPosition previousItemSlot) {
        this(inventory, nextItemSlot, previousItemSlot, 0);
    }
    public PageGui(List<Inventory> inventory, ItemSlotPosition nextItemSlot, ItemSlotPosition previousItemSlot, int page) {
        super(new Inventory(InventoryType.CHEST_1_ROW, ""));
        if (inventory.isEmpty()) throw new IllegalArgumentException("Inventories cant be empty!");
        inventories = prepareInventories(inventory, nextItemSlot, previousItemSlot);
        setInventory(inventories.getFirst());
        this.nextItemSlot = nextItemSlot;
        this.previousItemSlot = previousItemSlot;
        this.page = page;
    }

    private static List<Inventory> prepareInventories(List<Inventory> inventories, ItemSlotPosition nextItemSlot, ItemSlotPosition previousItemSlot) {
        if (inventories.size() == 1) return inventories;
        for (int i = 0; i < inventories.size(); i++) {
            Inventory inventory = inventories.get(i);
            if (i != inventories.size() - 1) {
                inventory.setItemStack(nextItemSlot.getSlot(inventory.getSize() / 9), TemplateItems.NextArrow.getItem());
            }
            if (i != 0) {
                inventory.setItemStack(previousItemSlot.getSlot(inventory.getSize() / 9), TemplateItems.BackArrow.getItem());
            }
        }
        return inventories;
    }

    @Override
    protected boolean click(int slot, Click click, AbstractInventory inventory) {
        for (var player : players)
            if (player.getPlayerInventory() == inventory) return super.click(slot, click, inventory);
        if (page != 0 && previousItemSlot.getSlot(getInventory().getSize() / 9) == slot) {
            setInventory(inventories.get(--page));
            return true;
        }
        if (page != inventories.size() - 1 && nextItemSlot.getSlot(getInventory().getSize() / 9) == slot) {
            setInventory(inventories.get(++page));
            return true;
        }
        return super.click(slot, click, inventory);
    }

    public enum ItemSlotPosition {
        BottomLeft {
            @Override
            public int getSlot(int rows) {
                return (rows - 1) * 9;
            }
        },
        BottomRight {
            @Override
            public int getSlot(int rows) {
                return (rows * 9) - 1;
            }
        };
        public abstract int getSlot(int rows);
    }
}
