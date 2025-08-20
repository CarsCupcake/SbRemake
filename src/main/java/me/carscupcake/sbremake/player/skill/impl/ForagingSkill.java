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
import java.util.Map;

public class ForagingSkill extends ISkill {
    public static final EventNode<Event> LISTENER = EventNode.all("skill.foraging").addListener(PlayerStatEvent.class, event -> {
        if (event.stat() == Stat.ForagingFortune) {
            event.modifiers().add(new PlayerStatEvent.BasicModifier("Foraging Skill",
                    event.player().getSkill(Skill.Foraging).getLevel() * 4, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Skills));
        }
        if (event.stat() == Stat.Strength) {
            int level = event.player().getSkill(Skill.Foraging).getLevel();
            int value = (level < 15) ? level : (14 + 2 * (level - 14));
            event.modifiers().add(new PlayerStatEvent.BasicModifier("Foraging Skill",
                    value, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Skills));
        }
    });

    public static int boost(int level) {
        if (level < 15)
            return level * 2;
        int value = 28;
        if (level < 20)
            return value + (level - 14) * 3;
        value += 15;
        if (level <= 25)
            return value + (level - 19) * 4;
        value += 24;
        return value + (level - 25) * 5;
    }

    public ForagingSkill(SkyblockPlayer player) {
        super(player, "foraging", Material.JUNGLE_SAPLING, Material.OAK_LOG,
                new Lore(List.of("§7Cut trees and forage for other", "§7plants to earn Foraging XP!", "§7 ",
                        "§eLogger %level%", " §fGrants §a%bonus%%  " + (Stat.ForagingFortune) + "§f, which", " §fincreases your chance for", " §fmultiple logs"), Map.of(
                        "%level%", (ignored, ignored2) -> StringUtils.toRoman(player.getSkill(Skill.Foraging).getLevel()),
                        "%bonus%", (ignored, ignored2) -> {
                            int level = player.getSkill(Skill.Foraging).getLevel();
                            return String.valueOf(level * 4);
                        }
                )));
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public String getName() {
        return "Foraging";
    }

    @Override
    public Stat getWisdomStat() {
        return Stat.ForagingWisdom;
    }

    @Override
    public List<Reward> getRewards(int level) {
        return List.of(new LoggerReward(level), makeCoinReward(level), new SkyblockXpReward(xpFromLevel(level)));
    }

    @Override
    public Skill getSkill() {
        return Skill.Foraging;
    }

    public record LoggerReward(int level) implements Reward {

        @Override
        public void reward(SkyblockPlayer player) {
        }

        @Override
        public Lore lore() {
            return new Lore(List.of("§eLogger " + (StringUtils.toRoman(level)), " §fGrants " + ((level == 0) ? "" : "§8" + (4 * (level - 1)) + "➜") + "§a" + (4 * level) + " " + (Stat.ForagingFortune) + "§f, which", " §fincreases your chance for", " §fmultiple logs.", "§8+ §a" + ((level < 15) ? 1 : 2) + " " + (Stat.Strength)));
        }
    }
}
