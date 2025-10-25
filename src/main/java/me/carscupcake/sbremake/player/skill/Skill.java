package me.carscupcake.sbremake.player.skill;

import lombok.Getter;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.impl.*;

@Getter
public enum Skill {
    Combat(CombatSkill.class),
    Mining(MiningSkill.class),
    Farming(FarmingSkill.class),
    Foraging(ForagingSkill.class),
    Fishing(FishingSkill.class),
    Alchemy(AlchemySkill.class),
    Dungeneering(Dungeoneering.class);
    private final Class<? extends ISkill> clazz;

    Skill(Class<? extends ISkill> clazz) {
        this.clazz = clazz;
    }

    public ISkill instantiate(SkyblockPlayer player, ConfigSection configSection) {
        try {
            var instance = clazz.getConstructor(SkyblockPlayer.class).newInstance(player);
            instance.load(configSection);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
