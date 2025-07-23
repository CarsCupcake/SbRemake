package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;

public class DeadMansChest extends HotmUpgrade {

    public DeadMansChest(SkyblockPlayer player) {
        super(player, FrozenSolid.class, DeadMansChest.class, SubzeroMining.class, Excavator.class);
    }

    @Override
    public String getName() {
        return "Dead Man's Chest";
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(level + 1, 3.1);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GlacialPowder;
    }

    @Override
    public String getId() {
        return "DEAD_MANS_CHEST";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Gain a §a%b%% §7chance to spawn §a1 §7additional §bFrozen Corpse §7when you enter a §bGlacite Mineshaft§7.", Map.of("%b%", (ignored, ignored2) -> StringUtils.cleanDouble(getBonus(level), 1)));
    }

    public double getBonus(int level) {
        return level;
    }

    @Override
    public int levelRequirement() {
        return 10;
    }
}
