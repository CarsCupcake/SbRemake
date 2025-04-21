package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.infernal;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraHelmetBaseline;

import java.util.Map;

public class InfernalAuroraHelmet extends AuroraHelmetBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 403, Stat.Defense, 126, Stat.Intelligence, 479, Stat.AbilityDamage, 40);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Infernal;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmNjM2VhNTM0NWMyODhjMjRmMWFhN2RkOGZjNjMyOTU1OTdkM2ZiMzc0ZTMxNGY2MDU2NDBiOGRlYTA2ZmUzZiJ9fX0=";
    }
}
