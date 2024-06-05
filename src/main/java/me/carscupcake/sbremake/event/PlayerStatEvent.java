package me.carscupcake.sbremake.event;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public record PlayerStatEvent(SkyblockPlayer player, List<PlayerStatModifier> modifiers, Stat stat) implements PlayerEvent {
    @Override
    public @NotNull Player getPlayer() {
        return player;
    }
    public record BasicModifier(String name, double value, Type type, Material showItem, StatsCategory category) implements PlayerStatModifier {
        public BasicModifier(String name, double value, Type type, StatsCategory category) {
            this(name, value, type, null, category);
        }
    }

    public double calculate() {
        double value = 0;
        double addativeMult = 1;
        double multiplicativeMult = 1;
        for (PlayerStatModifier modifier : modifiers) {
            switch (modifier.type()) {
                case Value -> value += modifier.value();
                case AddativeMultiplier -> addativeMult += modifier.value();
                case MultiplicativeMultiplier -> multiplicativeMult *= modifier.value();
            }
        }
        return value * addativeMult * multiplicativeMult;
    }

    interface PlayerStatModifier {
        Type type();
        double value();
        String name();
        @Nullable Material showItem();
        StatsCategory category();
    }
    public enum Type {
        Value,
        AddativeMultiplier,
        MultiplicativeMultiplier;
    }
    public enum StatsCategory {
        Innate("Innate"),
        Armor("Armor"),
        ItemHeld("Item Held"),
        Skills("Skills"),
        PetStats("Pet Stats"),
        Ability("Ability"),
        Uncategorized("Uncategorized");
        StatsCategory(String item) {

        }
    }
}
