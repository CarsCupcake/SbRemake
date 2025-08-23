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

public class FarmingSkill extends ISkill {
    public static final EventNode<Event> LISTENER = EventNode.all("skill.farming").addListener(PlayerStatEvent.class, event -> {
        if (event.stat() == Stat.FarmingFortune) {
            event.modifiers().add(new PlayerStatEvent.BasicModifier("Farming Skill",
                    event.player().getSkill(Skill.Farming).getLevel() * 4, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Skills));
        }
        if (event.stat() == Stat.Health) {
            event.modifiers().add(new PlayerStatEvent.BasicModifier("Farming Skill",
                    boost(event.player().getSkill(Skill.Farming).getLevel()), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Skills));
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

    public FarmingSkill(SkyblockPlayer player) {
        super(player, "farming", Material.GOLDEN_HOE, Material.HAY_BLOCK,
                new Lore(List.of("§7Harvest crops and shear sheep to", "§7earn Farming XP!", "§7 ",
                        "§eFarmhand %level%", " §fGrants §a%bonus%% " + (Stat.FarmingFortune) + "§f, which", " §fincreases your chance for", " §fmultiple crops"), Map.of(
                        "%level%", (ignored, ignored2) -> StringUtils.toRoman(player.getSkill(Skill.Farming).getLevel()),
                        "%bonus%", (ignored, ignored2) -> {
                            int level = player.getSkill(Skill.Farming).getLevel();
                            return String.valueOf(level * 4);
                        }
                )));
    }

    @Override
    public int getMaxLevel() {
        return 60;
    }

    @Override
    public String getName() {
        return "Farming";
    }

    @Override
    public Stat getWisdomStat() {
        return Stat.FarmingWisdom;
    }

    @Override
    public List<Reward> getRewards(int level) {
        return List.of(new FarmhandReward(level), makeCoinReward(level), new SkyblockXpReward(xpFromLevel(level)));
    }

    @Override
    public Skill getSkill() {
        return Skill.Farming;
    }

    public record FarmhandReward(int level) implements Reward {

        @Override
        public void reward(SkyblockPlayer player) {
        }

        @Override
        public Lore lore() {
            return new Lore(List.of("§eFarmhand " + (StringUtils.toRoman(level)), " §fGrants " + ((level == 0) ? "" : "§8" + (4 * (level - 1)) + "➜") + "§a" + (4 * level) + " " + (Stat.FarmingFortune) + "§f, which",
                    " §fincreases your chance for", " §fmultiple crops", "§8+ §a" + ((level < 15) ? 2 : (level < 20) ? 3 : (level <= 25) ? 4 : 5) + " " + (Stat.Health)));
        }
    }
}
