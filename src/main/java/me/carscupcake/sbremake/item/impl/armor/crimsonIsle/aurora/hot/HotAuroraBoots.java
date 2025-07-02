package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.hot;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraBootsBaseline;

import java.util.Map;

public class HotAuroraBoots extends AuroraBootsBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 164, Stat.Defense, 50, Stat.Intelligence, 158);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Hot;
    }
}
