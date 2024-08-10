package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

public class KeenEye extends HotmUpgrade {

    public KeenEye(SkyblockPlayer player) {
        super(player);
    }

    @Override
    public String getName() {
        return "Keen Eye";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int nextLevelCost(int current) {
        return 0;
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GlacialPowder;
    }

    @Override
    public String getId() {
        return "KEEN_EYE";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Whenever you enter a §bGlacite Mineshaft §7one highlighted Hard Stone block will contain §a+250 §bGlacite Powder§7.");
    }

    @Override
    public int levelRequirement() {
        return 8;
    }
}
