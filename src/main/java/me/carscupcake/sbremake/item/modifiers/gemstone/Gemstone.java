package me.carscupcake.sbremake.item.modifiers.gemstone;

import lombok.Getter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.gui.Gui;
import me.carscupcake.sbremake.util.gui.InventoryBuilder;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface Gemstone {
    double value(SbItemStack item);

    Type type();

    Quality quality();

    ISbItem asItem();

    int[][] slots = {{31}, {30, 32}, {30, 31, 32}, {29, 30, 32, 33}, {29, 30, 31, 32, 33}, {28, 29, 30, 32, 33, 34}, {28, 29, 30, 31, 32, 33, 34}};

    static void openGemstoneGrinder(SkyblockPlayer player) {
        Inventory builder = new InventoryBuilder(6, "Gemstone Grinder").fill(TemplateItems.EmptySlot.getItem()).setItem(13, ItemStack.AIR).build();
        showGemstoneSlots(builder);
        Gui gui = new Gui(builder);
        gui.setItemChangeEvent(event -> {
            if (event.getSlot() == 13 && event.getInventory() != event.getInventory().getViewers().stream().findFirst().orElseThrow().getInventory()) showGemstoneSlots(gui.getInventory());
        });
        gui.setGeneralClickEvent(event -> {
            if (event.getInventory() == event.getPlayer().getInventory()) {
                SbItemStack item = SbItemStack.from(event.getClickedItem());
                SbItemStack cursor = SbItemStack.from(event.getPlayer().getInventory().getCursorItem());
                if (item == SbItemStack.AIR && cursor != SbItemStack.AIR && cursor.sbItem() instanceof GemstoneSlots) return false;
                if (item == SbItemStack.AIR) return false;
                if ((!(item.sbItem() instanceof GemstoneSlots) && !(item.sbItem() instanceof Gemstone))) {
                    player.sendMessage("§cOnly items that can have Gemstones applied to them can be put in the Grinder!");
                    return true;
                }
                if (item.sbItem() instanceof GemstoneSlots) return false;
                if (cursor != SbItemStack.AIR) return true;
                Gemstone gemstone = (Gemstone) item.sbItem();
                SbItemStack selected = SbItemStack.from(gui.getInventory().getItemStack(13));
                if (selected == SbItemStack.AIR) return true;
                GemstoneSlot[] slots = selected.getModifier(Modifier.GEMSTONE_SLOTS);
                GemstoneSlot s = null;
                int index = 0;
                for (GemstoneSlot slot : slots) {
                    if (slot.unlocked() && slot.gemstone() == null) {
                        for (Type t : slot.type().getTypes()) {
                            if (t == gemstone.type()) {
                                s = slot;
                                break;
                            }
                        }
                        if (s != null) break;
                    }
                    index++;
                }
                if (s != null) {
                    slots[index] = new GemstoneSlot(s.type(), gemstone, true);
                    gui.getInventory().setItemStack(13, selected.withModifier(Modifier.GEMSTONE_SLOTS, slots).update(player).item());
                    item = item.withAmount(item.item().amount() - 1);
                    event.getPlayer().getInventory().setCursorItem((item == null || item == SbItemStack.AIR) ? ItemStack.AIR : Objects.requireNonNull(item).item());
                    event.getInventory().setItemStack(event.getSlot(), ItemStack.AIR);
                    return false;
                }

            } else {
                SbItemStack item = SbItemStack.from(gui.getInventory().getItemStack(13));
                if (item != SbItemStack.AIR) {
                    int i = 0;
                    for (int slot : slots[((GemstoneSlots) item.sbItem()).getGemstoneSlots().length - 1]) {
                        if (slot == event.getSlot()) {
                            GemstoneSlot[] gemstoneSlots = item.getModifier(Modifier.GEMSTONE_SLOTS);
                            GemstoneSlot gemstoneSlot = gemstoneSlots[i];
                            if (gemstoneSlot.unlocked()) {
                                if (gemstoneSlot.gemstone() != null) {
                                    if (player.getCoins() >= gemstoneSlot.gemstone().quality().cost) {
                                        if (player.addItem(gemstoneSlot.gemstone().asItem().create(), false)) {
                                            gemstoneSlots[i] = new GemstoneSlot(gemstoneSlot.type(), null, true);
                                            gui.getInventory().setItemStack(13, item.withModifier(Modifier.GEMSTONE_SLOTS, gemstoneSlots).item());
                                            player.removeCoins(gemstoneSlot.gemstone().quality().cost);
                                        } else player.sendMessage("§cNo Inventory Space available!");
                                    } else {
                                        player.sendMessage("§cNot enough Coins!");
                                    }
                                }
                            } else {
                                for (Cost cost : ((GemstoneSlots) item.sbItem()).getLockedSlotCost()[i])
                                    if (!cost.canPay(player)) {
                                        player.sendMessage("§cYou cant afford this!");
                                        return true;
                                    }
                                for (Cost cost : ((GemstoneSlots) item.sbItem()).getLockedSlotCost()[i]) {
                                    cost.pay(player);
                                }
                                player.playSound(SoundType.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                gemstoneSlots[i] = new GemstoneSlot(gemstoneSlot.type(), null, true);
                                gui.getInventory().setItemStack(13, item.withModifier(Modifier.GEMSTONE_SLOTS, gemstoneSlots).update(player).item());
                            }
                            return true;
                        }
                        i++;
                    }
                }
                return event.getSlot() != 13;
            }
            return true;
        });
        gui.setCloseEvent(() -> {
            SbItemStack item = SbItemStack.from(gui.getInventory().getItemStack(13));
            if (item != SbItemStack.AIR)
                player.addItem(item, false);
            return false;
        });
        gui.showGui(player);
    }

    static void showGemstoneSlots(Inventory inventory) {
        for (int i : slots[slots.length - 1])
            inventory.setItemStack(i, TemplateItems.EmptySlot.getItem());
        SbItemStack item = SbItemStack.from(inventory.getItemStack(13));
        if (item == SbItemStack.AIR || !(item.sbItem() instanceof GemstoneSlots gemstoneSlot)) {
            ItemStack gemstoneSlot = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§dGemstone Slot").addLore("§7Add an item above to apply Gemstones to it!").build();
            for (int i = 28; i < 35; i++) {
                inventory.setItemStack(i, gemstoneSlot);
            }
            return;
        }
        GemstoneSlot[] gemstoneSlots = item.getModifier(Modifier.GEMSTONE_SLOTS);
        int[] slots = Gemstone.slots[gemstoneSlots.length - 1];
        for (int i = 0; i < gemstoneSlots.length; i++) {
            GemstoneSlot slot = gemstoneSlots[i];
            if (slot.unlocked()) {
                if (slot.gemstone() == null) {
                    ItemBuilder builder = new ItemBuilder(slot.type().getGlassPane()).setName((slot.type().getPrefix()) + (slot.type().getPrefix()) + (slot.type().name()) + " Gemstone Slot").addLore("§7Click §aany Gemstone §7of any quality in your inventoryto apply this item!\n \nApplicable Gemstones");
                    for (Gemstone.Type type : slot.type().getTypes())
                        builder.addLoreRow((type.getPrefix()) + (type.name()) + " Gemstone");
                    inventory.setItemStack(slots[i], builder.build());
                } else {
                    ItemBuilder builder = new ItemBuilder(slot.gemstone().asItem().create().item());
                    inventory.setItemStack(slots[i], builder.addAllLore("§c  ", "§7Cost", "§6" + (StringUtils.toFormatedNumber(slot.gemstone().quality().cost)) + " Coins").build());
                }
            } else {
                ItemBuilder builder = new ItemBuilder(slot.type().getGlassPane()).setName((slot.type().getPrefix()) + (slot.type().getPrefix()) + (slot.type().name()) + " Gemstone Slot").addLore("§7This slot is locked! Purchasing this slot allows you to apply a " + (slot.type().getPrefix()) + (slot.type().getSymbol()) + " " + (slot.type().name()) + " Gemsotne §7 to it!\n \nApplicable Gemstones");
                for (Gemstone.Type type : slot.type().getTypes())
                    builder.addLoreRow((type.getPrefix()) + (type.name()) + " Gemstone");
                builder.addAllLore("§8 ", "§7Cost");
                for (Cost costs : gemstoneSlot.getLockedSlotCost()[i]) {
                    builder = costs.appendToLore(builder);
                }
                inventory.setItemStack(slots[i], builder.build());

            }
        }
    }

    Map<Type, Map<Quality, Gemstone>> gemstones = new HashMap<>();

    @Getter
    enum Type {
        Ruby(Stat.Health, "§c"), Amber(Stat.MiningSpeed, "§6"), Sapphire(Stat.Intelligence, "§b"), Jade(Stat.MiningFortune, "§a"), Amethyst(Stat.Defense, "§5"), Topaz(Stat.Pristine, "§e"), Jasper(Stat.Strength, "§d"), Opal(Stat.TrueDefense, "§f"), Aquamarine(Stat.SeaCreatureChance, "§3"), Citrine(Stat.ForagingFortune, "§4"), Onyx(Stat.CritDamage, "§9"), Peridot(Stat.FarmingFortune, "§2");

        private final Stat stat;
        private final String prefix;

        Type(Stat stat, String prefix) {
            this.stat = stat;
            this.prefix = prefix;
        }

        public static Type byId(String id) {
            for (Type t : Type.values())
                if (t.name().equalsIgnoreCase(id)) return t;
            return null;
        }
    }

    @Getter
    enum Quality {
        Rough(ItemRarity.COMMON, 100), Flawed(ItemRarity.UNCOMMON, 1000), Fine(ItemRarity.RARE, 10_000), Flawless(ItemRarity.EPIC, 100_000), Perfect(ItemRarity.LEGENDARY, 500_000);
        private final ItemRarity rarity;
        private final int cost;

        Quality(ItemRarity rarity, int cost) {
            this.rarity = rarity;
            this.cost = cost;
        }

        public static Quality byId(String id) {
            for (Quality t : Quality.values())
                if (t.name().equalsIgnoreCase(id)) return t;
            return null;
        }
    }
}
