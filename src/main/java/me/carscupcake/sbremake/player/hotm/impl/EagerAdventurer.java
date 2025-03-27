package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class EagerAdventurer extends HotmUpgrade {

    public EagerAdventurer(SkyblockPlayer player) {
        super(player);
    }

    @Override
    public String getName() {
        return "Eager Adventurer";
    }

    @Override
    public int getMaxLevel() {
        return 100;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(level + 1, 2.3);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GlacialPowder;
    }

    @Override
    public String getId() {
        return "EAGER_ADVENTURER";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Grants §a+%b% " + (Stat.MiningSpeed) + " §7when mining inside the §bGlacite Mineshaft§7.", Map.of("%b%", (_, _) -> String.valueOf(getBonus(level))));
    }

    public int getBonus(int level) {
        return level * 2;
    }

    @Override
    public int levelRequirement() {
        return 9;
    }
}
