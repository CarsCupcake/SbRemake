package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.List;
import java.util.Map;

public class LuckOfTheCave extends HotmUpgrade {
    public LuckOfTheCave(SkyblockPlayer player) {
        super(player, MiningSpeedBoost.class, MiningMadness.class);
    }

    @Override
    public String getName() {
        return "Luck of the Cave";
    }

    @Override
    public int getMaxLevel() {
        return 45;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 2, 3.07d);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.MithrilPowder;
    }

    @Override
    public String getId() {
        return "LUCK_OF_THE_CAVE";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(List.of("§7Increases the chance for you to", "§7trigger rare occurrences in", "§2Dwarven Mines §7by §a%p%%§7.", " ", "§7Rare occurances include:",
                "§7 • §6Golden Goblins", "§7 • §5Fallen Stars", "§7 • §6Powder Ghasts"), Map.of("%p%", (ignored, ignored2) -> String.valueOf(bonus(level))));
    }

    public int bonus(int level) {
        return level + 5;
    }

    @Override
    public int levelRequirement() {
        return 3;
    }
}
