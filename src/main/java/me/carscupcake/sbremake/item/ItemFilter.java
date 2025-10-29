package me.carscupcake.sbremake.item;

import lombok.Getter;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.gui.InputGui;
import me.carscupcake.sbremake.util.gui.InventoryBuilder;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.gui.PageGui;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum ItemFilter {
    All(_ -> true),
    Vanilla(item -> item instanceof BaseSbItem),
    Swords(ItemType.SWORDS),
    Bows(ItemType.Bow),
    Armor(ItemType.ARMOR),
    Tools(ItemType.Axe, ItemType.Pickaxe, ItemType.Drill, ItemType.Gauntlet, ItemType.Shovel),
    Accessories(ItemType.Accessory),
    Minions(ItemType.Minion),
    Admin(iSbItem -> iSbItem.getRarity() == ItemRarity.ADMIN);
    private static final InputGui searchInput = new InputGui("", "^^^^^^^^^^^^", "Enter a", "search query!");
    @Getter
    private final Predicate<ISbItem> predicate;

    ItemFilter(Predicate<ISbItem> predicate) {
        this.predicate = predicate;
    }

    ItemFilter(ItemType... types) {
        this.predicate = item -> {
            for (ItemType type : types) {
                if (item.getType() == type) return true;
            }
            return false;
        };
    }

    public static void openGui(SkyblockPlayer player) {
        openGui(player, All, null);
    }

    public static void openGui(SkyblockPlayer player, @NotNull ItemFilter filter, String search) {
        var items = filter.filterStream(SbItemStack.getRegisteredItems(), search).toList();
        var filterItemBuilder = new ItemBuilder(Material.HOPPER)
                .setName("§aFilter");
        for (ItemFilter itemFilter : ItemFilter.values()) {
            filterItemBuilder.addLoreRow((filter == itemFilter ? "§a" : "§7") + "‣ " + itemFilter.name());
        }
        var filterItem = filterItemBuilder.build();
        var searchItemBuilder = new ItemBuilder(Material.OAK_SIGN)
                .setName("§aSearch");
        if (search != null)
            searchItemBuilder.addLoreRow("§7Current search: " + search);
        var searchItem = searchItemBuilder.build();
        var resetItem = new ItemBuilder(Material.ANVIL)
                .setName("§aReset Filters")
                .setGlint(filter != All || search != null)
                .build();
        var totalPages = (int) Math.floor(items.size() / 45.0);
        var inventories = new ArrayList<Inventory>();
        InventoryBuilder current = null;
        int page = 0;
        int workingSlot = 0;
        for (var item : items) {
            if (item.getMaterial() == Material.AIR) continue;
            if (current == null) {
                current = new InventoryBuilder(6, "Items (" + (page + 1) + "/" + (totalPages + 1) + ")")
                        .setItem(52, filterItem)
                        .setItem(49, searchItem)
                        .setItem(51, resetItem);
            }
            current.setItem(workingSlot, SbItemStack.from(item).item());
            workingSlot++;
            if (workingSlot == 45) {
                inventories.add(current.build());
                current = null;
                page++;
                workingSlot = 0;
            }
        }
        if (current != null) inventories.add(current.build());
        if (current == null && inventories.isEmpty()) inventories.add(
                new InventoryBuilder(6, "Items (1/1)")
                        .setItem(52, filterItem)
                        .setItem(49, searchItem)
                        .setItem(51, resetItem)
                        .build());

        var gui = new PageGui(inventories, PageGui.ItemSlotPosition.BottomRight, PageGui.ItemSlotPosition.BottomLeft);
        gui.setCancelled(true);
        gui.getClickEvents().add(49, _ -> {
            searchInput.show(player, InputGui.STRING_FORMAT, s -> openGui(player, filter, s));
            return true;
        });
        gui.getClickEvents().add(51, _ -> {
            openGui(player, All, null);
            return true;
        });
        gui.getClickEvents().add(52, click -> {
            var newFilter = click instanceof Click.Left ? filter.next() : filter.previous();
            openGui(player, newFilter, search);
            return true;
        });
        gui.setGeneralClickEvent(event -> {
            if (event.getInventory() == event.getPlayer().getInventory()) return true;
            if (event.getSlot() >= 45) return true;
            var item = SbItemStack.from(event.getInventory().getItemStack(event.getSlot()));
            player.addItem(item.update(player));
            return true;
        });
        gui.showGui(player);
    }

    public ItemFilter next() {
        return values()[(ordinal() + 1) % values().length];
    }

    public ItemFilter previous() {
        return values()[(ordinal() + values().length - 1) % values().length];
    }

    public Stream<ISbItem> filterStream(@NotNull Collection<ISbItem> items, String name) {
        var stream = items.parallelStream().filter(predicate);
        return name == null ? stream : stream.filter(item -> StringUtils.stripeColorCodes(item.getName().toLowerCase()).contains(name.toLowerCase()) || item.getId().contains(name)).sorted((o1, o2) -> o1.getId().compareToIgnoreCase(o2.getId()));
    }
}
