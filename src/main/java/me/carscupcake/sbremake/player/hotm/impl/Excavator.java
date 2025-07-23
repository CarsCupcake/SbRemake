package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;

public class Excavator extends HotmUpgrade {

    public Excavator(SkyblockPlayer player) {
        super(player, DeadMansChest.class, RagsToRiches.class);
    }

    @Override
    public String getName() {
        return "Excavator";
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(level + 1, 3);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GlacialPowder;
    }

    @Override
    public String getId() {
        return "EXCAVATOR";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§9Suspicious Scraps §7are §a%b%% §7more likely to contain a fossil.", Map.of("%b%", (ignored, ignored2) -> StringUtils.cleanDouble(getBonus(level), 1)));
    }

    public double getBonus(int level) {
        return level * 0.2;
    }

    @Override
    public int levelRequirement() {
        return 10;
    }
}
