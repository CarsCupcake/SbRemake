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

public class FishingSkill extends ISkill {
    public static final EventNode<Event> LISTENER = EventNode.all("skill.fishing").addListener(PlayerStatEvent.class, event -> {
        if (event.stat() == Stat.Health) {
            event.modifiers().add(new PlayerStatEvent.BasicModifier("Fishing Skill",
                    boost(event.player().getSkill(Skill.Fishing).getLevel()), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Skills));
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

    public FishingSkill(SkyblockPlayer player) {
        super(player, "fishing", Material.FISHING_ROD, Material.PRISMARINE,
                new Lore("§7Visit your local pond to fish and earn Fishing XP!\n \n§eTreasure Hunter %level%\n§f Increases the chance to find\n treasures when fishing by §a%bonus%%§f.", Map.of(
                        "%level%", (_, _) -> StringUtils.toRoman(player.getSkill(Skill.Fishing).getLevel()),
                        "%bonus%", (_, _) -> {
                            int level = player.getSkill(Skill.Fishing).getLevel();
                            return String.valueOf(level * 0.2);
                        }
                )));
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public String getName() {
        return "Fishing";
    }

    @Override
    public Stat getWisdomStat() {
        return Stat.FishingWisdom;
    }

    @Override
    public List<Reward> getRewards(int level) {
        return List.of(new TreasureHunterReward(level), makeCoinReward(level), new SkyblockXpReward(xpFromLevel(level)));
    }

    public record TreasureHunterReward(int level) implements Reward {

        @Override
        public void reward(SkyblockPlayer player) {
        }

        @Override
        public Lore lore() {
            return new Lore(List.of(STR."§eTreasure Hunter \{StringUtils.toRoman(level)}", "§f Increases the chance to find", STR."§f treasures when fishing by §a\{level * 0.2}%§f.", STR."§8+ §a\{(level < 15) ? 2 : (level < 20) ? 3 : (level <= 25) ? 4 : 5} \{Stat.Health}"));
        }
    }
}
