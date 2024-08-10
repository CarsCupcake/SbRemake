package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;

public class GiftsFromTheDeparted extends HotmUpgrade {

    public GiftsFromTheDeparted(SkyblockPlayer player) {
        super(player, Surveyor.class, FrozenSolid.class);
    }

    @Override
    public String getName() {
        return "Gifts from the Departed";
    }

    @Override
    public int getMaxLevel() {
        return 100;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(level + 1, 2.45);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GlacialPowder;
    }

    @Override
    public String getId() {
        return "GIFTS_FROM_THE_DEPARTED";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Gain a §a%b%% §7chance to get an extra item when looting a §bFrozen Corpse", Map.of("%b%", (_, _) -> StringUtils.cleanDouble(getBonus(level), 1)));
    }

    public double getBonus(int level) {
        return level * 0.2d;
    }

    @Override
    public int levelRequirement() {
        return 10;
    }
}
