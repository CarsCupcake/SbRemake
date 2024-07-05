package me.carscupcake.sbremake.player.skill;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;

public interface SkillXpDropper {
    Skill type();
    double amount(SkyblockPlayer target);
    default void apply(SkyblockPlayer target) {
        target.getSkill(type()).addXp(amount(target));
    }
}
