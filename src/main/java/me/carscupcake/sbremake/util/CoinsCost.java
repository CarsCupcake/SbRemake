package me.carscupcake.sbremake.util;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.item.ItemBuilder;

@SuppressWarnings("preview")
public record CoinsCost(int coins) implements Cost{
    @Override
    public boolean canPay(SkyblockPlayer player) {
        return player.getCoins() >= coins;
    }

    @Override
    public void pay(SkyblockPlayer player) {
        player.removeCoins(coins);
    }

    @Override
    public ItemBuilder appendToLore(ItemBuilder builder) {
        return builder.addLoreRow(STR."§6\{StringUtils.toFormatedNumber(coins)} Coins");
    }

    @Override
    public String toString() {
        return STR."§6\{StringUtils.toFormatedNumber(coins)} Coins";
    }
}
