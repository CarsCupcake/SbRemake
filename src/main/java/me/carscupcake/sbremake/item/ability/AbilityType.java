package me.carscupcake.sbremake.item.ability;

import me.carscupcake.sbremake.event.PlayerInteractEvent;
import net.minestom.server.event.trait.PlayerEvent;

public interface AbilityType<T extends PlayerEvent> {
    Class<T> type();

    default boolean canExecute(T o) {
        return o.getClass() == type();
    }

    boolean isSneak();

    String name();

    AbilityType<PlayerInteractEvent> RIGHT_CLICK = new AbilityType<>() {
        @Override
        public Class<PlayerInteractEvent> type() {
            return PlayerInteractEvent.class;
        }

        @Override
        public boolean isSneak() {
            return false;
        }

        @Override
        public String name() {
            return "Right Click";
        }
    };
    AbilityType<PlayerInteractEvent> SNEAK_RIGHT_CLICK = new AbilityType<>() {
        @Override
        public Class<PlayerInteractEvent> type() {
            return PlayerInteractEvent.class;
        }

        @Override
        public boolean isSneak() {
            return true;
        }

        @Override
        public boolean canExecute(PlayerInteractEvent o) {
            return AbilityType.super.canExecute(o) && o.player().isSneaking();
        }

        @Override
        public String name() {
            return "Sneak Right Click";
        }
    };
}
