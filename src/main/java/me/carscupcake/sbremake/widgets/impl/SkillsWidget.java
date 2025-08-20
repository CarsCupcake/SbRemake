package me.carscupcake.sbremake.widgets.impl;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.ISkill;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.Span;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.widgets.IWidget;
import me.carscupcake.sbremake.widgets.TabListItem;
import me.carscupcake.sbremake.widgets.WidgetTypes;
import net.kyori.adventure.text.Component;

import java.util.*;

public class SkillsWidget implements IWidget {
    private final TreeMap<Skill, Double> cachedSkillValues = new TreeMap<>(Enum::compareTo);
    private final List<ISkill> skills;
    private Span<TabListItem> allocated;

    public SkillsWidget(SkyblockPlayer player, List<Skill> skills) {
        this.skills = skills.stream().map(player::getSkill).sorted(Comparator.comparing(ISkill::getSkill)).toList();
    }

    @Override
    public void update() {
        int i = 0;
        for (var skill : skills) {
            if (skill.getXp() != cachedSkillValues.getOrDefault(skill.getSkill(), -1d)) {
                cachedSkillValues.put(skill.getSkill(), skill.getXp());
                allocated.get(i + 1).updateName(Component.text(" " + skill.getName() + ": " + (skill.getLevel() == skill.getMaxLevel() ? "§cMAX" : ("§a" + StringUtils.toShortNumber(
                        skill.getPercentages())))));
            }
            i++;
        }
    }

    @Override
    public void render(Span<TabListItem> allocated) {
        this.allocated = allocated;
        for (var skill : skills) {
            cachedSkillValues.put(skill.getSkill(), -1d);
        }
        allocated.getFirst().updateName(Component.text("§e§lSkills"));
        update();
    }

    @Override
    public WidgetTypes type() {
        return null;
    }

    @Override
    public int requiredSpace() {
        return skills.size() + 1;
    }
}
