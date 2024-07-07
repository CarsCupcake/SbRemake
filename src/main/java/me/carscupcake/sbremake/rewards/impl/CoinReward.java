package me.carscupcake.sbremake.rewards.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.rewards.Reward;

public record CoinReward(int amount) implements Reward {
    @Override
    public void reward(SkyblockPlayer player) {
        player.setCoins(player.getCoins() + amount);
    }

    @Override
    public Lore lore() {
        return new Lore(STR."§8+ §6\{amount} §7Coins");
    }
}
