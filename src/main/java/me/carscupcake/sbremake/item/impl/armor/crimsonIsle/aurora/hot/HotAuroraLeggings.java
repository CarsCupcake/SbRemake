package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.hot;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraLeggingsBaseline;

import java.util.Map;

public class HotAuroraLeggings extends AuroraLeggingsBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 258, Stat.Defense, 69, Stat.Intelligence, 158);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Hot;
    }
}
