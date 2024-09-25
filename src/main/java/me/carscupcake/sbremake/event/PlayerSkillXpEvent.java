package me.carscupcake.sbremake.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import net.minestom.server.event.Event;

@Data
@AllArgsConstructor
public class PlayerSkillXpEvent implements Event {
    private final SkyblockPlayer player;
    private final Skill skill;
    private double xp;
    private double wisdom;
    private double multiplier;
}
