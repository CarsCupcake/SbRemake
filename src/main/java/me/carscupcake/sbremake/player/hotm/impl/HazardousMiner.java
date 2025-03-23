package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.PickaxeAbility;

public class HazardousMiner extends PickaxeAbility {
    public HazardousMiner(SkyblockPlayer player) {
        super(player, RagsToRiches.class);
    }

    @Override
    public int cooldown() {
        return 140;
    }

    @Override
    public void onInteract() {

    }

    @Override
    public String getName() {
        return "Hazardous Miner";
    }

    @Override
    public String getId() {
        return "HAZARDOUS_MINER_ABILITY";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Grants §a+40 " + (Stat.MiningSpeed) + "§7for each enemy withing §a20 §7blocks.");
    }

    @Override
    public int levelRequirement() {
        return 10;
    }
}
