package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class TitaniumInsanium extends HotmUpgrade {
    public TitaniumInsanium(SkyblockPlayer player) {
        super(player, MiningFortune.class, MiningSpeedBoost.class);
    }

    @Override
    public String getName() {
        return "Titanium Insanium";
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 2, 3.1d);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.MithrilPowder;
    }

    @Override
    public String getId() {
        return "TITANIUM_INSANIUM";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7When mining Mithril Ore, you have a §a%ch%% §7chance to convert the block into Titanium Ore.", Map.of("%ch%", (item, player) -> String.valueOf(reward(level))));
    }

    public double reward(int level) {
        return 2 + (level * 0.1);
    }

    @Override
    public int levelRequirement() {
        return 2;
    }
}
