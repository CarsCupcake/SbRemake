package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora;

import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraHelmetBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraLeggingsBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.KuudraArmorType;
import net.minestom.server.color.Color;

import java.util.List;


public abstract class AuroraHelmetBaseline extends KuudraHelmetBaseline {
    @Override
    public KuudraArmorType armorType() {
        return KuudraArmorType.Aurora;
    }

    @Override
    public String getId() {
        return (armorTier().getId()) + "AURORA_HELMET";
    }

    @Override
    public String getName() {
        return (armorTier().getName()) + " Aurora Helmet";
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return AuroraBootsBaseline.ABILITIES;
    }
}
