package me.carscupcake.sbremake.util;

import me.carscupcake.sbremake.player.Essence;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import org.jetbrains.annotations.NotNull;

public record EssenceCost(Essence essence, int amount) implements Cost {
    @Override
    public boolean canPay(SkyblockPlayer player) {
        return player.getEssence().getOrDefault(essence, 0) >= amount;
    }

    @Override
    public void pay(SkyblockPlayer player) {
        player.getEssence().subtract(essence, amount);
    }

    @Override
    public ItemBuilder appendToLore(ItemBuilder builder) {
        return builder.addLoreRow(toString());
    }

    @Override
    public @NotNull String toString() {
        return "§d" + (StringUtils.toFormatedNumber(amount)) + " " + (essence.name()) + " Essence";
    }
}
