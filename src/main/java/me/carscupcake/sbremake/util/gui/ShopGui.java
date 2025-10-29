package me.carscupcake.sbremake.util.gui;

import me.carscupcake.sbremake.item.NpcSellable;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.Pair;
import me.carscupcake.sbremake.util.TemplateItems;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Objects;

public class ShopGui extends Gui {

    private ShopGui(InventoryBuilder builder, SkyblockPlayer player, List<Pair<SbItemStack, Cost>> items) {
        super(builder.build());
        setGeneralClickEvent(event -> {
            if (event.getInventory() == event.getPlayer().getInventory()) {
                ItemStack item = event.getClickedItem();
                SbItemStack stack = SbItemStack.from(item);
                if (stack == SbItemStack.AIR) return true;
                if (stack.sbItem() instanceof NpcSellable npcSellable) {
                    int coins = npcSellable.sellPrice() * item.amount();
                    player.addCoins(coins);
                    player.getPlayerInventory().setItemStack(event.getSlot(), SbItemStack.AIR);
                    player.getSellHistory().push(new Pair<>(stack, coins));
                    player.sendMessage("§aYou sold " + (stack.displayName()) + " §8x" + (item.amount()) + " §afor §6" + (coins) + " Coins§a!");
                    updateSellHistory(player);
                } else {
                    player.sendMessage("§cThat item cannot be sold!");
                }
            } else {
                ItemStack item = event.getClickedItem();
                if (item.material() == Material.AIR) return true;
                if (event.getSlot() == 49) {
                    if (player.getSellHistory().isEmpty()) return true;
                    if (player.getCoins() < player.getSellHistory().peek().getSecond()) {
                        player.sendMessage("§cYou cant afford this!");
                        return true;
                    }
                    Pair<SbItemStack, Integer> i = player.getSellHistory().pop();
                    if (player.addItem(i.getFirst(), false)) {
                        player.removeCoins(i.getSecond());
                        player.sendMessage("§aYou bought back " + (i.getFirst().displayName()));
                        updateSellHistory(player);
                    } else {
                        player.getSellHistory().push(i);
                        player.sendMessage("§cYou have no Inventory Space!");
                    }
                } else {
                    //Yes
                    int row = (int) (event.getSlot() / 9d);
                    if (row < 1 || row > 5) return true;
                    int slotFromRow = event.getSlot() - (row * 9);
                    if (slotFromRow == 0 || slotFromRow == 8) return true;
                    int index = ((row - 1) * 7) - 1 + slotFromRow;
                    if (index >= items.size()) return true;
                    Pair<SbItemStack, Cost> pair = items.get(index);
                    if (!pair.getSecond().canPay(player)) {
                        player.sendMessage("§cYou don't have enough Coins!");
                        return true;
                    }
                    for (Requirement requirement : pair.getFirst().sbItem().requirements())
                        if (!requirement.canUse(player, pair.getFirst().item())) {
                            player.sendMessage("§cYou have not unlocked this item!");
                            return true;
                        }
                    if (player.addItem(pair.getFirst().update(player), false)) {
                        player.sendMessage("§aYou bought " + (pair.getFirst().displayName()) + (pair.getFirst().item().amount() > 1 ? "§8x" + (pair.getSecond()) + " " : "") + "§a for " + (pair.getSecond()) + "§a!");
                        pair.getSecond().pay(player);
                    }

                }
            }
            return true;
        });
    }

    public static ShopGui createShopGui(List<Pair<SbItemStack, Cost>> items, String name, SkyblockPlayer player) {
        InventoryBuilder builder = new InventoryBuilder(6, name).fill(TemplateItems.EmptySlot.getItem(), 0, 8).fill(TemplateItems.EmptySlot.getItem(), 45, 53).setItem(TemplateItems.EmptySlot.getItem(), 9).setItem(TemplateItems.EmptySlot.getItem(), 18).setItem(TemplateItems.EmptySlot.getItem(), 27).setItem(TemplateItems.EmptySlot.getItem(), 36).setItem(TemplateItems.EmptySlot.getItem(), 8).setItem(TemplateItems.EmptySlot.getItem(), 17).setItem(TemplateItems.EmptySlot.getItem(), 26).setItem(TemplateItems.EmptySlot.getItem(), 35).setItem(TemplateItems.EmptySlot.getItem(), 44).setItem(49, player.getSellHistory().isEmpty() ? new ItemBuilder(Material.HOPPER).setName("§aSell Item").addLore("§7Click items in your inventory to sell them to this Shop!").build() : new ItemBuilder(player.getSellHistory().peek().getFirst().item()).addAllLore(" ", "§7Cost", "§6" + (Objects.requireNonNull(player.getSellHistory().peek()).getSecond()) + " Coins").build());
        int slot = 10;
        for (Pair<SbItemStack, Cost> item : items) {
            builder.setItem(slot, item.getSecond().appendToLore(new ItemBuilder(item.getFirst().update(player).item()).addAllLore(" ", "§7Cost")).build());
            slot++;
            if ((slot + 1) % 9 == 0)
                slot += 2;
        }
        return new ShopGui(builder, player, items);
    }

    public void updateSellHistory(SkyblockPlayer player) {
        getInventory().setItemStack(49, player.getSellHistory().isEmpty() ? new ItemBuilder(Material.HOPPER).setName("§aSell Item").addLore("§7Click items in your inventory to sell them to this Shop!").build() : new ItemBuilder(player.getSellHistory().peek().getFirst().item()).addAllLore(" ", "§7Cost", "§6" + (Objects.requireNonNull(player.getSellHistory().peek()).getSecond()) + " Coins").build());
    }
}
