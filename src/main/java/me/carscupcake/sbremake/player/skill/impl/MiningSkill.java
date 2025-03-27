package me.carscupcake.sbremake.player.skill.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.event.PlayerToEntityDamageEvent;
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

public class MiningSkill extends ISkill {
    public static final EventNode<Event> LISTENER = EventNode.all("skill.mining").addListener(PlayerStatEvent.class, event -> {
        if (event.stat() == Stat.MiningFortune) {
            event.modifiers().add(new PlayerStatEvent.BasicModifier("Mining Skill",
                    event.player().getSkill(Skill.Mining).getLevel() * 4, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Skills));
        }
        if (event.stat() == Stat.Defense) {
            int level = event.player().getSkill(Skill.Mining).getLevel();
            int value = (level < 15) ? level : (14 + 2 * (level - 14));
            event.modifiers().add(new PlayerStatEvent.BasicModifier("Mining Skill",
                    value, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Skills));
        }
    });

    private static void process(PlayerToEntityDamageEvent event) {
        int level = event.getPlayer().getSkill(Skill.Combat).getLevel();
        event.setAdditiveMultiplier(event.getAdditiveMultiplier() + ((level < 50) ? level * 0.04 : (0.04 * 50 + (level - 50) * 0.01)));
    }

    public MiningSkill(SkyblockPlayer player) {
        super(player, "mining", Material.STONE_PICKAXE, Material.IRON_BLOCK,
                new Lore(List.of("§7Dive into deep caves and find rare", "§7ores and valuable materials to earn", "§7Mining XP!", "§7 ",
                        "§eSpelunker %level%", " §fGrants §a%bonus%%  " + (Stat.MiningFortune) + "§f, which", " §fincreases your chance for", " §fmultiple ore drops"), Map.of(
                        "%level%", (_, _) -> StringUtils.toRoman(player.getSkill(Skill.Mining).getLevel()),
                        "%bonus%", (_, _) -> {
                            int level = player.getSkill(Skill.Mining).getLevel();
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
        return "Mining";
    }

    @Override
    public Stat getWisdomStat() {
        return Stat.MiningWisdom;
    }

    @Override
    public List<Reward> getRewards(int level) {
        return List.of(new SpelunkerReward(level), makeCoinReward(level), new SkyblockXpReward(xpFromLevel(level)));
    }

    public record SpelunkerReward(int level) implements Reward {

        @Override
        public void reward(SkyblockPlayer player) {
        }

        @Override
        public Lore lore() {
            return new Lore(List.of("§eSpelunker " + (StringUtils.toRoman(level)) , " §fGrants " + ((level == 0) ? "" : "§8" + (4 * (level - 1)) + "➜") + "§a" + (4 * level) + " " + (Stat.MiningFortune) + "§f, which", " §fincreases your chance for", " §fmultiple ore drops", "§8+ §a" + ((level < 15) ? 1 : 2) + " " + (Stat.Defense) ));
        }
    }
}
