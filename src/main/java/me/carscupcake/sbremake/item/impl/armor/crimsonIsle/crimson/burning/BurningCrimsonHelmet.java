package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.burning;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonHelmetBaseline;

import java.util.Map;

public class BurningCrimsonHelmet extends CrimsonHelmetBaseline {
    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Burning;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWIwMzRhNWQ5N2MyNGZlMGVjOTAyYmQwMmZlYzUyYTA5ZDkxNzM2NmJlMDYwYzUxZjlhMWEyNzZiMjg0YTlkNyJ9fX0=";
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 254, Stat.Defense, 79, Stat.Strength, 48, Stat.Intelligence, 24, Stat.CritDamage, 32);
    }
}
