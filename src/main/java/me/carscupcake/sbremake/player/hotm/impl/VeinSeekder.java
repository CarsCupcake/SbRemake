package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.PickaxeAbility;

public class VeinSeekder extends PickaxeAbility {
    public VeinSeekder(SkyblockPlayer player) {
        super(player, LonesomeMiner.class);
    }

    @Override
    public int cooldown() {
        return 60;
    }

    @Override
    public void onInteract() {

    }

    @Override
    public String getName() {
        return "Vein Seeker";
    }

    @Override
    public String getId() {
        return "VEIN_SEEKER_ABILITY";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Points the direction of the nearest vein and grants §a+3 §6Mining Spread §7for §a14s§7.");
    }

    @Override
    public int levelRequirement() {
        return 6;
    }
}
