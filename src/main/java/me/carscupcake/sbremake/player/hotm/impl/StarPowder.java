package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

public class StarPowder extends HotmUpgrade {

    public StarPowder(SkyblockPlayer player) {
        super(player, GreatExplorer.class, FrontLoaded.class);
    }

    @Override
    public String getName() {
        return "Star Powder";
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
        return Powder.MithrilPowder;
    }

    @Override
    public String getId() {
        return "STAR_POWDER";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Mining Mithril Ore near §5Fallen Crystals §7given §ax3 §7Mithril Powder.");
    }

    @Override
    public int levelRequirement() {
        return 5;
    }
}
