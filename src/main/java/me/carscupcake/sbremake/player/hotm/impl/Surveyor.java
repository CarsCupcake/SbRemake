package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;

public class Surveyor extends HotmUpgrade {

    public Surveyor(SkyblockPlayer player) {
        super(player);
    }

    @Override
    public String getName() {
        return "Surveyor";
    }

    @Override
    public int getMaxLevel() {
        return 20;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(level + 1, 4);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GlacialPowder;
    }

    @Override
    public String getId() {
        return "SURVEYOR";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Increases your chance of finding a §bGlacite Mineshaft §7when mining in the §bGlacite Tunnels §7by §a%b%%§7.", Map.of("%b%", (ignored, ignored2) -> StringUtils.cleanDouble(getBonus(level), 1)));
    }

    public double getBonus(int level) {
        return level * 0.75;
    }

    @Override
    public int levelRequirement() {
        return 9;
    }
}
