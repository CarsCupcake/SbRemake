package me.carscupcake.sbremake.player.skill.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.ISkill;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.rewards.Reward;
import me.carscupcake.sbremake.rewards.impl.SkyblockXpReward;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;

import java.util.List;

public class Dungeoneering extends ISkill {
    public static final EventNode<Event> LISTENER = EventNode.all("skill.dungeon").addListener(PlayerStatEvent.class, event -> {
        if (event.stat() == Stat.Health) {
            event.modifiers().add(new PlayerStatEvent.BasicModifier("Dungeon Skill",
                    boost(event.player().getSkill(Skill.Farming).getLevel()), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Skills));
        }
    });

    public static int boost(int level) {
       return 2 * level;
    }

    public static int dungeonItemStatBonus(int level) {
        int i = 10;
        if (level <= 5) return i + level * 4;
        i += 5 * 4;
        if (level <= 10) return i + ((level - 5) * 5);
        i += 5 * 5;
        if (level <= 15) return i + ((level - 10) * 6);
        i += 5 * 6;
        if (level <= 20) return i + ((level - 15) * 7);
        i += 5 * 7;
        if (level <= 25) return i + ((level - 20) * 8);
        i += 5 * 8;
        if (level <= 30) return i + ((level - 25) * 9);
        i += 5 * 9;
        if (level <= 35) return i + ((level - 30) * 10);
        i += 5 * 10;
        if (level <= 40) return i + ((level - 35) * 12);
        i += 5 * 12;
        if (level <= 45) return i + ((level - 40) * 14);
        return switch (level) {
            case 46 -> 401;
            case 47 -> 418;
            case 48 -> 436;
            case 49 -> 455;
            default -> 475;
        };
    }

    public Dungeoneering(SkyblockPlayer player) {
        super(player, "dungeneering", Material.WITHER_SKELETON_SKULL, Material.WITHER_SKELETON_SKULL,
                new Lore(List.of("§7Complete Catacombs Dungeons to", "§7earn experience!")));
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public String getName() {
        return "Catacombs";
    }

    @Override
    public Stat getWisdomStat() {
        return null;
    }

    @Override
    public List<Reward> getRewards(int level) {
        return List.of(new HealthReward(level), new SkyblockXpReward(xpFromLevel(level)), new DungeonStatReward(level));
    }

    public record HealthReward(int level) implements Reward {

        @Override
        public void reward(SkyblockPlayer player) {
        }

        @Override
        public Lore lore() {
            return new Lore(List.of(STR."§8+ §a2 \{Stat.Health}"));
        }
    }
    public record DungeonStatReward(int level) implements Reward {

        @Override
        public void reward(SkyblockPlayer player) {
        }

        @Override
        public Lore lore() {
            return new Lore(STR."§7Increases the base stats of your dungeon items from §8\{dungeonItemStatBonus(level - 1)}➜§c\{dungeonItemStatBonus(level)}% §7while in §cThe Catacombs§7.");
        }
    }
}
