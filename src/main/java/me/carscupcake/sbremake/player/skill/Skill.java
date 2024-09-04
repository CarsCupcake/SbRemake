package me.carscupcake.sbremake.player.skill;

import lombok.Getter;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.impl.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

import java.lang.reflect.Field;

@Getter
public enum Skill {
    Combat(CombatSkill.class),
    Mining(MiningSkill.class),
    Farming(FarmingSkill.class),
    Foraging(ForagingSkill.class),
    Fishing(FishingSkill.class),
    Dungeneering(Dungeoneering.class);
    private final Class<? extends ISkill> clazz;
    Skill(Class<? extends ISkill> clazz) {
        this.clazz = clazz;
    }

    public ISkill instantiate(SkyblockPlayer player) {
        try {
            return clazz.getConstructor(SkyblockPlayer.class).newInstance(player);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
