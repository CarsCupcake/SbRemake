package me.carscupcake.sbremake.item.ability;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.event.trait.PlayerEvent;

import java.util.function.Consumer;

public record ItemAbility<TEvent extends PlayerEvent>(String name, AbilityType<TEvent> abilityType,
                                                      Consumer<TEvent> executor, Lore lore,
                                                      Requirement<TEvent>... requirements) implements Ability {
    @SafeVarargs
    public ItemAbility(String name, AbilityType<TEvent> abilityType, Consumer<TEvent> executor, Lore lore, Requirement<TEvent>... requirements) {
        this.requirements = (requirements == null) ? new Requirement[0] : requirements;
        this.name = name;
        this.abilityType = abilityType;
        this.executor = executor;
        this.lore = lore;
    }

    @SafeVarargs
    public ItemAbility(String name, AbilityType<TEvent> abilityType, Consumer<TEvent> executor, Requirement<TEvent>... requirement) {
        this(name, abilityType, executor, Lore.EMPTY, (requirement == null) ? new Requirement[0] : requirement);
    }

    @Override
    public String headline(SbItemStack item, SkyblockPlayer player) {
        return STR."§6Ability: \{name} §e§l\{abilityType.name()}";
    }

    @Override
    public Lore lore() {
        return lore;
    }

    public boolean canExecute(TEvent event) {
        if (!abilityType.canExecute(event))
            return false;
        for (Requirement<TEvent> requirement : requirements)
            if (!requirement.requirement(event)) {
                if (requirement instanceof ManaRequirement<TEvent>) {
                    ((SkyblockPlayer) event.getPlayer()).setNotEnoughMana(true);
                    event.getPlayer().playSound(Sound.sound(Key.key("minecraft", "entity.enderman.teleport"), Sound.Source.PLAYER, 1, 0.1f));
                }
                return false;
            }
        return true;
    }

    public void execute(PlayerEvent o) throws ClassCastException {
        TEvent event = (TEvent) o;
        if (!canExecute(event)) return;
        ManaRequirement<TEvent> manaRequirement = null;
        for (Requirement<TEvent> requirement : requirements) {
            requirement.execute(event);
            if (requirement instanceof ManaRequirement<TEvent> manaRequirement1)
                manaRequirement = manaRequirement1;
        }
        executor.accept(event);
        if (manaRequirement != null)
            ((SkyblockPlayer) o.getPlayer()).setLastAbility(STR."§b-\{manaRequirement.getManaCost()} Mana (§6\{name}§b)");
    }
}
