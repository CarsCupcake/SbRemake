package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;

public class NoStoneUnturned extends HotmUpgrade {

    public NoStoneUnturned(SkyblockPlayer player) {
        super(player);
    }

    @Override
    public String getName() {
        return "No Stone Unturned";
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(level + 1, 3.05);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GlacialPowder;
    }

    @Override
    public String getId() {
        return "NO_STONE_UNTURNED";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Increases your chance of finding a §9Suspicious Scrap §7when mining in a §bGlacial Mineshaft §7by §a%b%%", Map.of("%b%", (_, _) -> StringUtils.cleanDouble(getBonus(level), 1)));
    }

    public double getBonus(int level) {
        return level * 0.5;
    }

    @Override
    public int levelRequirement() {
        return 8;
    }
}
