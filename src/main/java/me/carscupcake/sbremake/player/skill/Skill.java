package me.carscupcake.sbremake.player.skill;

import lombok.Getter;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.impl.CombatSkill;
import me.carscupcake.sbremake.player.skill.impl.FarmingSkill;
import me.carscupcake.sbremake.player.skill.impl.ForagingSkill;
import me.carscupcake.sbremake.player.skill.impl.MiningSkill;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

import java.lang.reflect.Field;

@Getter
public enum Skill {
    Combat(CombatSkill.class),
    Mining(MiningSkill.class),
    Farming(FarmingSkill.class),
    Foraging(ForagingSkill.class);
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
