package me.carscupcake.sbremake.event;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.event.Event;

public record EntityDeathEvent(SkyblockEntity entity, Type type) implements Event {
    public enum Type {
        Melee,
        Projectile,
        Magic,
        Undefined
    }
}
