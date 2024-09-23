package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.hot;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonHelmetBaseline;

import java.util.Map;

public class HotCrimsonHelmet extends CrimsonHelmetBaseline {
    @Override
    protected KuudraArmorTier armorTier() {
        return KuudraArmorTier.Hot;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmJlNmQ2Njc3MGE2MWJmNTZlNmQ0YjQ3NjkyMmIxYzNiM2RjOWY3OGEyNmU1NmIzNmNkOTY1YjdhYjIwYjQxNyJ9fX0=";
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 202, Stat.Defense, 63, Stat.Strength, 38, Stat.Intelligence, 19, Stat.CritDamage, 25);
    }
}
