package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora;

import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraBootsBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.DominusAbility;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.KuudraArmorType;
import net.minestom.server.color.Color;

import java.util.List;

public abstract class AuroraBootsBaseline extends KuudraBootsBaseline {
    public static final List<Ability> ABILITIES = List.of(ArcaneEnergy.INSTANCE, HomingMissiles.INSTANCE);
    @Override
    public KuudraArmorType armorType() {
        return KuudraArmorType.Aurora;
    }

    @Override
    public String getId() {
        return (armorTier().getId()) + "AURORA_BOOTS";
    }

    @Override
    public String getName() {
        return (armorTier().getName()) + " Aurora Boots";
    }

    @Override
    public Color color() {
        return new Color(0x6184fc);
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return ABILITIES;
    }
}
