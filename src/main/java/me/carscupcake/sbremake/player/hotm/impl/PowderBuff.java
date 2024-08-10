package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class PowderBuff extends HotmUpgrade {
    public PowderBuff(SkyblockPlayer player) {
        super(player, Mole.class);
    }

    @Override
    public String getName() {
        return "Powder Buff";
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(level + 1, 3.2d);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GemstonePowder;
    }

    @Override
    public String getId() {
        return "POWDER_BUFF";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Gain §a%p%% §7more Mithril Powder and Gemstone Powder.", Map.of("%p%", (_, _) -> String.valueOf(getBuff(level))));
    }

    public int getBuff(int level) {
        return level;
    }

    @Override
    public int levelRequirement() {
        return 7;
    }
}
