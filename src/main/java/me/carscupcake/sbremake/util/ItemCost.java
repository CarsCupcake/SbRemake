package me.carscupcake.sbremake.util;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("preview")
public record ItemCost(ISbItem item, int amount) implements Cost {
    public ItemCost(Class<? extends ISbItem> clazz, int amount) {
        this(ISbItem.get(clazz), amount);
    }

    public ItemCost(Class<? extends ISbItem> clazz) {
        this(ISbItem.get(clazz), 1);
    }

    @Override
    public boolean canPay(SkyblockPlayer player) {
        int remaining = amount;
        for (ItemStack item : player.getPlayerInventory().getItemStacks()) {
            SbItemStack stack = SbItemStack.from(item);
            if (stack == SbItemStack.AIR || stack.sbItem() != this.item) continue;
            if (remaining - stack.item().amount() <= 0) return true;
            remaining -= stack.item().amount();
        }
        return false;
    }

    @Override
    public void pay(SkyblockPlayer player) {
        int remaining = amount;
        for (int i = 0; i < player.getPlayerInventory().getSize(); i++) {
            SbItemStack stack = player.getPlayerInventory().getSbItemStack(i);
            if (stack == null || stack.sbItem() != this.item) continue;
            if (remaining - stack.item().amount() <= 0) {
                int delta = stack.item().amount() - remaining;
                stack = stack.withAmount(delta);
                player.getPlayerInventory().setItemStack(i, Objects.requireNonNullElse(stack, SbItemStack.AIR));
                return;
            }
            remaining -= stack.item().amount();
            player.getPlayerInventory().setItemStack(i, SbItemStack.AIR);
        }
    }

    @Override
    public ItemBuilder appendToLore(ItemBuilder builder) {
        return builder.addLoreRow(toString());
    }

    @Override
    public @NotNull String toString() {
        return (item.getRarity().getPrefix()) + (item.getName()) + " §8x" + (amount);
    }
}
