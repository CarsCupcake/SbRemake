package me.carscupcake.sbremake.item.impl.sword.slayer.enderman;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.event.EntityMeleeDamagePlayerEvent;
import me.carscupcake.sbremake.event.PlayerDamageEntityEvent;
import me.carscupcake.sbremake.event.PlayerMeleeDamageEntityEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.impl.sword.EndSword;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VoidwalkerKatana implements ISbItem, ISbItem.StatProvider, Listener {
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

    private final Map<Stat, Number> stats = Map.of(Stat.Damage, 105, Stat.Strength, 40, Stat.CritDamage, 15);

    @Override
    public Map<Stat, Number> stats() {
        return stats;
    }

    private final List<Requirement> requirements = List.of(new SlayerRequirement(Slayers.Enderman, 1));

    @Override
    public List<Requirement> requirements() {
        return requirements;
    }

    private final Lore lore = new Lore("§7Deals §a+150% §7damage to Endermen. Receive 3% less damage from Endermen when held.");

    @Override
    public Lore getLore() {
        return lore;
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("katanas").addListener(EntityMeleeDamagePlayerEvent.class, event -> {
            if (event.getPlayer().getSbItemInMainHand().sbItem() instanceof VoidwalkerKatana && event.getEntity().getEntityType() == EntityType.ENDERMAN)
                event.setMultiplier(event.getMultiplier() - 0.03);
        })
                .addListener(PlayerMeleeDamageEntityEvent.class, event -> {
                    if (event.getEntity().getEntityType() == EntityType.ENDERMAN) {
                        if (event.getPlayer().getSbItemInMainHand().sbItem() instanceof VoidwalkerKatana)
                            event.addAdditiveMultiplier(1.5);
                    }
                });
    }
}
