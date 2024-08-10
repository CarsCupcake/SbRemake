package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;

public class FrozenSolid extends HotmUpgrade {

    public FrozenSolid(SkyblockPlayer player) {
        super(player, GiftsFromTheDeparted.class, DeadMansChest.class);
    }

    @Override
    public String getName() {
        return "Frozen Solid";
    }

    @Override
    public int getMaxLevel() {
        return 1;
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
        return "FROZEN_SOLID";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Gain §a2x §bGlacite Powder §7from killing mobs in the §bGlacite Tunnels §7and §bGlacite Mineshafts§7.");
    }

    @Override
    public int levelRequirement() {
        return 10;
    }
}
