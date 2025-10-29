package me.carscupcake.sbremake.player.accessories;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.config.ConfigField;
import me.carscupcake.sbremake.config.DefaultConfigItem;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.AbstractAccessory;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.gui.InventoryBuilder;
import me.carscupcake.sbremake.util.gui.PageGui;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
public class AccessoryBag implements DefaultConfigItem {
    public static final EventNode<Event> LISTENER = EventNode.all("player.accessorybag").addListener(PlayerStatEvent.class, event -> {
        if (event.player().getAccessoryBag() == null) return;
        for (var tuning : event.player().getAccessoryBag().getTunings().entrySet()) {
            if (tuning.getKey().getStat() != event.stat()) continue;
            event.modifiers().add(new PlayerStatEvent.BasicModifier(tuning.getKey().name() + " Tuning", tuning.getKey().getPerPoint() * tuning.getValue(), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.TuningPoint));
        }
    });

    @ConfigField
    private List<SbItemStack> items;
    @Setter
    @ConfigField
    private int capacity;
    private int magicalPower;
    @Setter
    @ConfigField
    private Map<AccessoryStatTunings, Integer> tunings = new HashMap<>();

    private void computeMagicalPower() {
        AtomicInteger magicalPower = new AtomicInteger(0);
        items.parallelStream().collect(Collectors.groupingBy(stack -> ((AbstractAccessory) stack.sbItem()).getAccessoryFamily())).forEach((ignored, value) -> {
            if (value.size() == 1) {
                magicalPower.addAndGet(getPowerFromRarity(value.getFirst().getRarity()));
            } else {
                var mutable = new ArrayList<>(value);
                mutable.sort(Comparator.comparing(stack -> stack.getRarity().ordinal()));
                magicalPower.addAndGet(getPowerFromRarity(value.getLast().getRarity()));
            }
        });
        this.magicalPower = magicalPower.get();
    }

    public void openInventory(SkyblockPlayer player) {
        int invs = 1 + (capacity / 45);
        var list = 0;
        var inventories = new ArrayList<Inventory>(invs);
        for (int i = 0; i < invs; i++) {
            var invSlots = (capacity - list > 45) ? 54 : (9 * (1 + getNextHigher((capacity - list) / 9d)));
            var builder = new InventoryBuilder((int) Math.ceil(invSlots / 9d), "Accessory Bag (" + (i + 1) + "/" + invs + ")")
                    .fill(TemplateItems.EmptySlot.getItem());
            for (int j = 0; j < invSlots; j++) {
                if (list >= capacity) {
                    break;
                }
                if (items.size() <= list) {
                    builder.setItem(j, ItemStack.AIR);
                    list++;
                    continue;
                }
                builder.setItem(j, items.get(list).item());
                list++;
            }
            inventories.add(builder.build());
        }
        var gui = new PageGui(inventories, PageGui.ItemSlotPosition.BottomLeft, PageGui.ItemSlotPosition.BottomRight);
        gui.showGui(player);
        gui.setGeneralClickEvent(event -> {
            if (event.isCancelled()) return true;
            if (event.getSlot() < 0 ||  event.getSlot() > event.getInventory().getSize()) return true;
            var itemStack = event.getInventory().getItemStack(event.getSlot());
            var item = SbItemStack.from(itemStack);
            if (!itemStack.hasTag(Tag.NBT("id")) || !(item.sbItem() instanceof AbstractAccessory)) {
                if (item == SbItemStack.AIR) return false;
                player.sendMessage("§cThis is not an accessory!");
                return true;
            }
            return false;
        });
        gui.setCloseEvent(() -> {
            var items = new ArrayList<SbItemStack>();
            for (var inventory : gui.getInventories()) {
                for (int i = 0; i < inventory.getSize() - 9; i++) {
                    var item = inventory.getItemStack(i);
                    if (item == ItemStack.AIR) continue;
                    var sbItem = SbItemStack.from(item);
                    if (sbItem.sbItem() instanceof AbstractAccessory)
                        items.add(sbItem);
                }
            }
            AccessoryBag.this.items.clear();
            AccessoryBag.this.items.addAll(items);
            AccessoryBag.this.computeMagicalPower();
            return false;
        });
    }

    private int getNextHigher(double d) {
        int toInt = (int) d;
        if (toInt < d) return toInt + 1;
        return toInt;
    }

    public int getTuningPoints() {
        return magicalPower / 10;
    }

    private int getPowerFromRarity(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON, SPECIAL -> 3;
            case UNCOMMON, VERY_SPECIAL -> 5;
            case RARE -> 8;
            case EPIC -> 12;
            case LEGENDARY -> 16;
            default -> 22;
        };
    }
}
