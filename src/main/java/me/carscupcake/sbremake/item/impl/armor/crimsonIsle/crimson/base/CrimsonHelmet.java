package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.base;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonHelmetBaseline;

import java.util.Map;

public class CrimsonHelmet extends CrimsonHelmetBaseline {
    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Base;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTA1MWM4M2Q5ZWJmNjkwMTNmMWVjOGM5ZWZjOTc5ZWMyZDkyNWE5MjFjYzg3N2ZmNjRhYmUwOWFhZGQyZjZjYyJ9fX0=";
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 160, Stat.Defense, 50, Stat.Strength, 30, Stat.Intelligence, 15, Stat.CritDamage, 20);
    }
}
