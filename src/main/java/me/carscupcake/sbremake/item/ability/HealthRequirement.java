package me.carscupcake.sbremake.item.ability;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.event.trait.PlayerEvent;

public record HealthRequirement<T extends PlayerEvent>(double maxHealthPercentage) implements Requirement<T> {

    @Override
    public boolean requirement(T t) {
        SkyblockPlayer player = (SkyblockPlayer) t.getPlayer();
        return player.getHealth() > maxHealthPercentage * player.getStat(Stat.Health);
    }

    @Override
    public void execute(T t) {
        SkyblockPlayer player = (SkyblockPlayer) t.getPlayer();
        player.forceDamage(maxHealthPercentage * player.getStat(Stat.Health));
    }
}
