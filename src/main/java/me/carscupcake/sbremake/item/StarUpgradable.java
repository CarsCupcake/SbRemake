package me.carscupcake.sbremake.item;

import com.google.common.base.Strings;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.gui.Gui;
import me.carscupcake.sbremake.util.gui.InventoryBuilder;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.concurrent.atomic.AtomicBoolean;

public interface StarUpgradable {
    Lore emptyUpgradeLore = new Lore("""
            §7Upgrades the level of items using materials and §dEssence§7, improving their stats.\
             Place an item above to upgrade its level.
             §7
            View the §dEssence Guide §7below for a list of upgradeable items and §dEssence§7!""");
    ItemStack emptyAnvil = new ItemBuilder(Material.ANVIL)
            .setLore(emptyUpgradeLore)
            .setName("§cUpgrade Item")
            .build();
    InventoryBuilder upgradeInventory = new InventoryBuilder(6, "Upgrade Item")
            .fill(TemplateItems.EmptySlot.getItem())
            .setItem(new ItemBuilder(Material.BARRIER)
                    .setName("§cClose").build(), 49)
            .setItem(ItemStack.of(Material.AIR), 13)
            .setItem(emptyAnvil, 22)
            .setItem(new ItemBuilder(Material.BARRIER)
                    .setLore(emptyUpgradeLore)
                    .setName("§cUpgrade Item")
                    .build(), 31)
            .verticalFill(0, 6, new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                    .setName("§7").build())
            .verticalFill(8, 6, new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                    .setName("§7").build());

    static void showStarUpgradingGui(SkyblockPlayer player) {
        var crafted = new AtomicBoolean(false);
        var gui = new Gui(upgradeInventory.build());
        gui.getClickEvents().add(49, ignored -> {
            player.closeGui();
            return true;
        });
        gui.setGeneralClickEvent(inventoryPreClickEvent -> {
            if (inventoryPreClickEvent.getInventory() == inventoryPreClickEvent.getPlayer().getInventory()) return false;
            if (inventoryPreClickEvent.getSlot() == 31 && crafted.get()) {
                return false;
            }
            return inventoryPreClickEvent.getSlot() != 13;
        });
        gui.setCloseEvent(() -> {
            if (gui.getInventory().getItemStack(13) != ItemStack.AIR) {
                player.addItem(gui.getInventory().getItemStack(13));
            }
            return false;
        });
        gui.getClickEvents().add(22, ignored -> {
            var sbItem = SbItemStack.from(gui.getInventory().getItemStack(13));
            if (!(sbItem.sbItem() instanceof StarUpgradable upgradable) || upgradable.getMaxStars() <= sbItem.getModifier(Modifier.STARS)) {
                player.sendMessage("§cYou cannot upgrade this item!");
                return true;
            }
            var stars = sbItem.getModifier(Modifier.STARS);
            var costs = upgradable.upgradeCost(sbItem, stars);
            for (var cost : costs) {
                if (!cost.canPay(player)) {
                    player.sendMessage("§cYou cannot afford this!");
                    return true;
                }
            }
            for (var cost : costs) {
                cost.pay(player);
            }
            player.playSound(SoundType.BLOCK_ANVIL_USE.create(1));
            gui.getInventory().setItemStack(13, ItemStack.AIR);
            crafted.set(true);
            gui.getInventory().setItemStack(31, sbItem.withModifier(Modifier.STARS, sbItem.getModifier(Modifier.STARS) + 1).update(player).item());
            return true;
        });
        gui.setPostClickEvent(event -> {
            if (event.getInventory() == event.getPlayer().getInventory()) return;
            if (event.getSlot() == 31 && crafted.get()) {
                gui.getInventory().setItemStack(31, new ItemBuilder(Material.BARRIER).setLore(emptyUpgradeLore).setName("§cUpgrade Item").build());
                gui.getInventory().setItemStack(22, emptyAnvil);
                crafted.set(false);
                return;
            }
            if (event.getSlot() != 13) return;
            if (crafted.get()) {
                player.addItem(gui.getInventory().getItemStack(31));
                crafted.set(false);
            }
            var selectionSlot = event.getInventory().getItemStack(event.getSlot());
            if (selectionSlot == ItemStack.AIR) {
                gui.getInventory().setItemStack(31, new ItemBuilder(Material.BARRIER).setLore(emptyUpgradeLore).setName("§cUpgrade Item").build());
                gui.getInventory().setItemStack(22, emptyAnvil);
                return;
            }
            SbItemStack itemStack = SbItemStack.from(selectionSlot);
            if (!(itemStack.sbItem() instanceof StarUpgradable starUpgradable)) {
                gui.getInventory().setItemStack(31, new ItemBuilder(Material.BARRIER).addAllLore("§7This item cannot be upgraded!").setName("§cError!").build());
                gui.getInventory().setItemStack(22, emptyAnvil);
                return;
            }
            var stars = itemStack.getModifier(Modifier.STARS);
            if (stars >= starUpgradable.getMaxStars()) {
                gui.getInventory().setItemStack(31, new ItemBuilder(Material.BARRIER).addAllLore("§7This item cannot be upgraded any", "§7further!").setName("§cError!").build());
                gui.getInventory().setItemStack(22, emptyAnvil);
                return;
            }
            var upgradeCost = starUpgradable.upgradeCost(itemStack, stars);
            var upgradedItem = itemStack.withModifier(Modifier.STARS, stars + 1).update(player);
            StringBuilder builder = new StringBuilder();
            for (var cost : upgradeCost) {
                builder.append(cost).append("\n");
            }
            builder.append("\n§eClick to upgrade!");
            gui.getInventory().setItemStack(22, new ItemBuilder(Material.ANVIL)
                            .setName("§aUpgrade Item")
                            .setLore(new Lore("§7Upgrades the item to " + upgradedItem.getRarity().getPrefix() + upgradedItem.displayName() + "§7!\n §7\nCost\n" + builder))
                    .build());
            gui.getInventory().setItemStack(31, new ItemBuilder(upgradedItem.item()).addLore("§7Cost\n" + builder).build());
        });
        gui.showGui(player);
    }

    int getMaxStars();

    Cost[] upgradeCost(SbItemStack item, int star);

    default String getStarDisplay(int stars) {
        if (stars == 0) return "";
        String s = "";
        if (!(stars <= 0)) {
            if (stars <= 5) {
                s = "§6" + (Strings.repeat("✪", stars));
            } else if (stars <= 10) {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < 5; i++)
                    str.append((i + 6 > stars) ? "§6" : "§d").append("✪");
                s = str.toString();
            } else if (stars <= 15) {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < 5; i++)
                    str.append((i + 11 > stars) ? "§d" : "§b").append("✪");
                s = str.toString();
            }
        }
        return s;
    }

    default double getBonus(SkyblockPlayer player, int stars) {
        return stars * 0.02d;
    }
}
