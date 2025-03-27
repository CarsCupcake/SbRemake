package me.carscupcake.sbremake.player.skill.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.ISkill;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.rewards.Reward;
import me.carscupcake.sbremake.rewards.impl.SkyblockXpReward;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;

import java.util.List;

public class AlchemySkill extends ISkill {
    public static final EventNode<Event> LISTENER = EventNode.all("skill.alchemy").addListener(PlayerStatEvent.class, event -> {
        if (event.stat() == Stat.Intelligence) {
            event.modifiers().add(new PlayerStatEvent.BasicModifier("Alchemy Skill",
                    boost(event.player().getSkill(Skill.Farming).getLevel()), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Skills));
        }
    });

    public static int boost(int level) {
        int i = 14;
        if (level <= 14) return level;
        return i + (level - 14) * 2;
    }

    public AlchemySkill(SkyblockPlayer player) {
        super(player, "alchemy", Material.BREWING_STAND, Material.BLAZE_ROD,
                new Lore(List.of("§7Brew potions to earn Alchemy XP!")));
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public String getName() {
        return "Alchemy";
    }

    @Override
    public Stat getWisdomStat() {
        return null;
    }

    @Override
    public List<Reward> getRewards(int level) {
        return List.of(new IntelligenceReward(level), new SkyblockXpReward(xpFromLevel(level)), new DungeonStatReward(level));
    }

    public record IntelligenceReward(int level) implements Reward {

        @Override
        public void reward(SkyblockPlayer player) {
        }

        @Override
        public Lore lore() {
            return new Lore(List.of("§8+ §a" + (level <= 14 ? 1 : 2) + " " + (Stat.Intelligence)));
        }
    }

    public record DungeonStatReward(int level) implements Reward {

        @Override
        public void reward(SkyblockPlayer player) {
        }

        @Override
        public Lore lore() {
            return new Lore("§fPotions that you brew have a §a" + (level == 1 ? "1" : "§8" + (level - 1) + "➜§a" + (level) + "%") + " §flonger duration.");
        }
    }
}
