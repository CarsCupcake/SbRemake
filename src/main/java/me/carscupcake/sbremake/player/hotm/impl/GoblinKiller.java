package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

public class GoblinKiller extends HotmUpgrade {
    public GoblinKiller(SkyblockPlayer player) {
        super(player, MiningMadness.class, LonesomeMiner.class);
    }

    @Override
    public String getName() {
        return "Goblin Killer";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 2, 3d);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.MithrilPowder;
    }

    @Override
    public String getId() {
        return "GOBLIN_KILLER";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Killing a §6Golden Goblin §7gives §2200 §7extra §2Mithril Powder§7, while killing other Goblins gives some based on their wits");
    }

    @Override
    public int levelRequirement() {
        return 5;
    }
}
