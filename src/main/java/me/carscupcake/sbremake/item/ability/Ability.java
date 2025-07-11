package me.carscupcake.sbremake.item.ability;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.Nullable;

import java.time.temporal.ChronoUnit;
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
        list.add(headline(itemStack, player) + "§7");
        list.addAll(lore().build(itemStack, player));
        long soulflowCost = -1;
        long manaCost = -1;
        double healthCost = -1;
        double cooldown = -1;
        if (this instanceof ItemAbility<?> itemAbility) {
            for (Requirement<?> requirement : itemAbility.requirements()) {
                if (requirement instanceof ManaRequirement<?> manaRequirement) {
                    manaCost = manaRequirement.getManaCost(itemStack);
                }
                if (requirement instanceof CooldownRequirement<?>(
                        long cooldown1, java.time.temporal.TemporalUnit timeUnit
                )) {
                    cooldown = (cooldown1 * (timeUnit == ChronoUnit.MILLIS ? .001 : timeUnit.getDuration().getSeconds()));
                }
                if (requirement instanceof HealthRequirement<?>(double maxHealthPercentage)) {
                    healthCost = player == null ? (maxHealthPercentage * 100) : (player.getStat(Stat.Health) * maxHealthPercentage);
                }
                if (requirement instanceof SoulflowRequirement<?>(var soulflow)) {
                    soulflowCost = soulflow;
                }
            }
        }
        if (soulflowCost > 0) list.add("§8Soulflow Cost: §3" + (soulflowCost));
        if (manaCost > 0) list.add("§8Mana Cost: §3" + (manaCost));
        if (healthCost > 0) list.add("§8Health Cost: §c" + StringUtils.cleanDouble(healthCost) + (player == null ? "%" : ""));
        if (cooldown > 0) list.add("§8Cooldown: §a" + StringUtils.toFormatedNumber(cooldown) + "s");
        return list;
    }

    EventNode<Event> ABILITY_NODE = EventNode.all("items.abilities").addListener(PlayerInteractEvent.class, Ability::runAbility);

    static void runAbility(PlayerEvent event) {
        SbItemStack item = ((SkyblockPlayer) event.getPlayer()).getSbItemInMainHand();
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
