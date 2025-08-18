package me.carscupcake.sbremake.player.skill.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.*;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.ISkill;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.rewards.Reward;
import me.carscupcake.sbremake.rewards.impl.SkyblockXpReward;
import me.carscupcake.sbremake.event.eventBinding.PlayerDamageEntityBinding;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class CombatSkill extends ISkill {
    public static final EventNode<Event> LISTENER;

    static {
        LISTENER = EventNode.all("skill.combat").addListener(PlayerStatEvent.class, event -> {
            if (event.stat() != Stat.CritChance) return;
            event.modifiers().add(new PlayerStatEvent.BasicModifier("Combat Skill", event.player().getSkill(Skill.Combat).getLevel() * 0.5, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Skills));
        });
        LISTENER.register(new PlayerDamageEntityBinding(CombatSkill::process));
    }

    private static void process(PlayerToEntityDamageEvent event) {
        int level = event.getPlayer().getSkill(Skill.Combat).getLevel();
        event.setAdditiveMultiplier(event.getAdditiveMultiplier() + ((level < 50) ? level * 0.04 : (0.04 * 50 + (level - 50) * 0.01)));
    }

    public CombatSkill(SkyblockPlayer player) {
        super(player, "combat", Material.STONE_SWORD, Material.DIAMOND_HELMET, new Lore(List.of("§7Fight mobs and special bosses to", "§7earn Combat XP!", "§7 ", "§eWarrior %level%", " §fDeal §a%bonus%% §fmore damage to mobs."), Map.of(
                "%level%", (ignored, ignored2) -> String.valueOf(player.getSkill(Skill.Combat).getLevel()),
                "%bonus%", (ignored, ignored2) -> String.valueOf(player.getSkill(Skill.Combat).getLevel() * 4)
        )));
    }

    @Override
    public int getMaxLevel() {
        return 60;
    }

    @Override
    public String getName() {
        return "Combat";
    }

    @Override
    public Stat getWisdomStat() {
        return Stat.CombatWisdom;
    }

    @Override
    public List<Reward> getRewards(int level) {
        return List.of(new WarriorReward(level), makeCoinReward(level), new SkyblockXpReward(xpFromLevel(level)));
    }

    public record WarriorReward(int level) implements Reward {

        @Override
        public void reward(SkyblockPlayer player) {
        }

        @Override
        public Lore lore() {
            return new Lore(List.of("§eWarrior " + (StringUtils.toRoman(level)), " §fDeal " + ((level == 0) ? "" : "§8" + (4 * (level - 1)) + "%➜") + "§a" + (4 * level) + "% §fmore damage to mobs.", "§8+ §a0.5% " + (Stat.CritChance)));
        }
    }
}
