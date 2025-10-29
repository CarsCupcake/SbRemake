package me.carscupcake.sbremake.event;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record PlayerStatEvent(SkyblockPlayer player, List<PlayerStatModifier> modifiers,
                              Stat stat) implements PlayerEvent {
    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    public record BasicModifier(String name, double value, Type type, ItemStack showItem,
                                StatsCategory category) implements PlayerStatModifier {
        public BasicModifier(String name, double value, Type type, StatsCategory category) {
            this(name, value, type, category == null ? null : category.item, category);
        }

        public BasicModifier(String name, double value, Type type, Material showItem, StatsCategory category) {
            this(name, value, type, ItemStack.of(showItem), category);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PlayerStatModifier modifier) {
                return modifier.name().equals(name) && value == modifier.value() && modifier.type().equals(type) && modifier.category() == category;
            }
            return false;
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

    public interface PlayerStatModifier {
        Type type();

        double value();

        String name();

        @Nullable ItemStack showItem();

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
        Hotm("HOTM", new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODZmMDZlYWEzMDA0YWVlZDA5YjNkNWI0NWQ5NzZkZTU4NGU2OTFjMGU5Y2FkZTEzMzYzNWRlOTNkMjNiOWVkYiJ9fX0=").build()),
        Potion("Potion", ItemStack.of(Material.POTION)),
        TuningPoint("Tuning Point", ItemStack.of(Material.COMPARATOR)),
        Uncategorized("Uncategorized");
        private final String name;
        private final ItemStack item;

        StatsCategory(String item) {
            this(item, null);
        }

        StatsCategory(String name, ItemStack item) {
            this.name = name;
            this.item = item;
        }
    }
}
