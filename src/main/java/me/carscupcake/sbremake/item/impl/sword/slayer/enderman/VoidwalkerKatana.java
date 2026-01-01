package me.carscupcake.sbremake.item.impl.sword.slayer.enderman;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.event.EntityMeleeDamagePlayerEvent;
import me.carscupcake.sbremake.event.PlayerMeleeDamageEntityEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class VoidwalkerKatana implements ISbItem, ISbItem.StatProvider, Listener {
    private final Map<Stat, Number> stats = Map.of(Stat.Damage, 105, Stat.Strength, 40, Stat.CritDamage, 15);
    private final List<Requirement> requirements = List.of(new SlayerRequirement(Slayers.Enderman, 1));
    private final Lore lore = new Lore("§7Deals §a+150% §7damage to Endermen. Receive §a3%§7 less damage from Endermen when held.");

    @Override
    public String getId() {
        return "VOIDWALKER_KATANA";
    }

    @Override
    public String getName() {
        return "Voidwalker Katana";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Sword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public Map<Stat, Number> stats() {
        return stats;
    }

    @Override
    public List<Requirement> requirements() {
        return requirements;
    }

    @Override
    public Lore getLore() {
        return lore;
    }

    @Override
    public EventNode<@NotNull Event> node() {
        return EventNode.all("katanas").addListener(EntityMeleeDamagePlayerEvent.class, event -> {
            if (event.getSource().getEntityType() != EntityType.ENDERMAN) return;
            if (event.getTarget().getSbItemInMainHand().sbItem() instanceof VoidwalkerKatana) event.setAdditiveMultiplier(event.getAdditiveMultiplier() - 0.03);
            if (event.getTarget().getSbItemInMainHand().sbItem() instanceof VoidedgeKatana) event.setAdditiveMultiplier(event.getAdditiveMultiplier() - 0.06);
            if (event.getTarget().getSbItemInMainHand().sbItem() instanceof VorpalKatana) event.setAdditiveMultiplier(event.getAdditiveMultiplier() - 0.09);
            if (event.getTarget().getSbItemInMainHand().sbItem() instanceof AtomsplitKatana) event.setAdditiveMultiplier(event.getAdditiveMultiplier() - 0.12);
        }).addListener(PlayerMeleeDamageEntityEvent.class, event -> {
            if (event.getTarget().getEntityType().equals(EntityType.ENDERMAN)) {
                if (event.getPlayer().getSbItemInMainHand().sbItem() instanceof VoidwalkerKatana) event.addAdditiveMultiplier(1.5);
                if (event.getPlayer().getSbItemInMainHand().sbItem() instanceof VoidedgeKatana) event.addAdditiveMultiplier(2);
                if (event.getPlayer().getSbItemInMainHand().sbItem() instanceof VorpalKatana) event.addAdditiveMultiplier(2.5);
                if (event.getPlayer().getSbItemInMainHand().sbItem() instanceof AtomsplitKatana) event.addAdditiveMultiplier(3);
            }
        });
    }
}
