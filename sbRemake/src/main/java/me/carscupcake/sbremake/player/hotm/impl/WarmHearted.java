package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;

public class WarmHearted extends HotmUpgrade {

    public WarmHearted(SkyblockPlayer player) {
        super(player);
    }

    @Override
    public String getName() {
        return "Warm Hearted";
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 1, 3.1);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GlacialPowder;
    }

    @Override
    public String getId() {
        return "WARM_HEARTED";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Grants §a+%b% " + (Stat.ColdResistance) + "§7.", Map.of("%b%", (_, _) -> StringUtils.cleanDouble(getBonus(level), 1)));
    }

    public double getBonus(int level) {
        return level * 0.2;
    }

    @Override
    public int levelRequirement() {
        return 8;
    }
}
