package me.carscupcake.sbremake.rewards.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.rewards.Reward;

public record SkyblockXpReward(int xp) implements Reward {
    @Override
    public void reward(SkyblockPlayer player) {
        player.addSkyblockXp(xp);
    }

    @Override
    public Lore lore() {
        return new Lore("§8+ §b" + (xp) + " Skyblock XP");
    }
}
