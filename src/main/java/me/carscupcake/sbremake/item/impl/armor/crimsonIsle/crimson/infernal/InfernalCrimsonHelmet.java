package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.infernal;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonHelmetBaseline;

import java.util.Map;

public class InfernalCrimsonHelmet extends CrimsonHelmetBaseline {
    @Override
    protected KuudraArmorTier armorTier() {
        return KuudraArmorTier.Infernal;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTA3ZDZiZjc2MTExOTBlZDljNTgwZDhjODdjMjk2MDU5MjE2MjMwYzk1MDFlZWM2MzU5ZTBkNjBlYzg0NzU4ZSJ9fX0=";
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 403, Stat.Defense, 126, Stat.Strength, 76, Stat.Intelligence, 38, Stat.CritDamage, 50);
    }
}
