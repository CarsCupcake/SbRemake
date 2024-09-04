package me.carscupcake.sbremake.item.ability;

import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public sealed interface Ability permits FullSetBonus, ItemAbility, PetAbility {
    String headline(SbItemStack item, @Nullable SkyblockPlayer player);

    Lore lore();

    default boolean showInLore() {
        return lore() != Lore.EMPTY;
    }

    default List<String> buildLore(SbItemStack itemStack, @Nullable SkyblockPlayer player) {
        List<String> list = new ArrayList<>();
        list.add(headline(itemStack, player));
        list.addAll(lore().build(itemStack, player));
        long manaCost = -1;
        long cooldown = -1;
        if (this instanceof ItemAbility<?> itemAbility) {
            for (Requirement<?> requirement : itemAbility.requirements()) {
                if (requirement instanceof ManaRequirement<?> manaRequirement) {
                    manaCost = manaRequirement.getManaCost();
                }
                if (requirement instanceof CooldownRequirement<?> cooldownRequirement) {
                    cooldown = cooldownRequirement.timeUnit().getDuration().getSeconds() * cooldownRequirement.cooldown();
                }
            }
        }
        if (manaCost > 0) list.add(STR."§8Mana Cost: §3\{manaCost}");
        if (cooldown > 0) list.add(STR."§8Cooldown: §a\{cooldown}s");
        return list;
    }

    EventNode<Event> ABILITY_NODE = EventNode.all("items.abilities").addListener(PlayerInteractEvent.class, Ability::runAbility);

    static void runAbility(PlayerEvent event) {
        SbItemStack item = SbItemStack.from(event.getPlayer().getItemInMainHand());
        if (item == null) return;
        List<ItemAbility<?>> abilities = new ArrayList<>();
        boolean hasSneak = false;
        for (Ability a : item.getAbilities((SkyblockPlayer) event.getPlayer())) {
            if (a instanceof ItemAbility<?> itemAbility && itemAbility.abilityType().type() == event.getClass()) {
                abilities.add(itemAbility);
                if (itemAbility.abilityType().isSneak()) hasSneak = true;
            }
        }
        if (abilities.isEmpty()) return;
        for (ItemAbility<?> ability : abilities) {
            if (hasSneak && !ability.abilityType().isSneak()) continue;
            ability.execute(event);
        }
    }
}
