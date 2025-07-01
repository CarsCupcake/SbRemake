package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.base;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerToEntityMageDamage;
import me.carscupcake.sbremake.item.Listener;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.ArcaneEnergy;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraBootsBaseline;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

import java.util.Map;

public class BaseAuroraBoots extends AuroraBootsBaseline implements Listener {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 130, Stat.Defense, 40, Stat.Intelligence, 125);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Base;
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("aurora.ability").addListener(PlayerToEntityMageDamage.class, event -> {
            var counter = ArcaneEnergy.ARCANE_ENERGY_MAP.get(event.getPlayer());
            if (counter != null) {
                counter.add();
            }
        });
    }
}
