package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;

public class LonesomeMiner extends HotmUpgrade {

    public LonesomeMiner(SkyblockPlayer player) {
        super(player, GoblinKiller.class, MiningSpeed2.class, Professional.class);
    }

    @Override
    public String getName() {
        return "Lonesome Miner";
    }

    @Override
    public int getMaxLevel() {
        return 45;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 2, 3.07d);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GemstonePowder;
    }

    @Override
    public String getId() {
        return "LONESOME_MINER";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(STR."§7Increases \{Stat.Strength}§7, \{Stat.CritChance}§7, \{Stat.CritDamage}§7, \{Stat.Defense}§7 and \{Stat.Health} §7stats gain by §a%p%% §7while in the Crystal Hollows.", Map.of("%p%", (_, _) -> {
            return StringUtils.cleanDouble(getBonus(level), 1);
        }));
    }

    public double getBonus(int level) {
        return 5 + ((level - 1) * 0.5);
    }

    @Override
    public int levelRequirement() {
        return 6;
    }
}
