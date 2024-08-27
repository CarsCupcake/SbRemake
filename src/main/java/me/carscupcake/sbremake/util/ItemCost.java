package me.carscupcake.sbremake.util;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;

@SuppressWarnings("preview")
public record ItemCost(ISbItem item, int amount) implements Cost {
    @Override
    public boolean canPay(SkyblockPlayer player) {
        int remaining = amount;
        for (ItemStack item : player.getInventory().getItemStacks()) {
            SbItemStack stack = SbItemStack.from(item);
            if (stack == null || stack.sbItem() != this.item) continue;
            if (remaining - stack.item().amount() <= 0) return true;
            remaining -= stack.item().amount();
        }
        return false;
    }

    @Override
    public void pay(SkyblockPlayer player) {
        int remaining = amount;
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            SbItemStack stack = SbItemStack.from(player.getInventory().getItemStack(i));
            if (stack == null || stack.sbItem() != this.item) continue;
            if (remaining - stack.item().amount() <= 0) {
                int delta = stack.item().amount() - remaining;
                stack = stack.withAmount(delta);
                if (stack == null) {
                    player.getInventory().setItemStack(i, ItemStack.AIR);
                } else {
                    player.getInventory().setItemStack(i, stack.item());
                }
                return;
            }
            remaining -= stack.item().amount();
            player.getInventory().setItemStack(i, ItemStack.AIR);
        }
    }

    @Override
    public ItemBuilder appendToLore(ItemBuilder builder) {
        return builder.addLoreRow(STR."\{item.getRarity().getPrefix()}\{item.getName()} §8x\{amount}");
    }

    @Override
    public String toString() {
        return STR."\{item.getRarity().getPrefix()}\{item.getName()} §8x\{amount}";
    }
}
