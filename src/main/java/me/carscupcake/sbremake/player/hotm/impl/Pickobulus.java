package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.PickaxeAbility;

public class Pickobulus extends PickaxeAbility {
    public Pickobulus(SkyblockPlayer player) {
        super(player, TitaniumInsanium.class);
    }

    @Override
    public int cooldown() {
        return 120;
    }

    @Override
    public void onInteract() {

    }

    @Override
    public String getName() {
        return "Pickobulus";
    }

    @Override
    public String getId() {
        return "PICKOBULUS_ABILITY";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Throw your pickaxe to create an explosion on impact, mining all ores within a 2 block radius.");
    }

    @Override
    public int levelRequirement() {
        return 2;
    }
}
