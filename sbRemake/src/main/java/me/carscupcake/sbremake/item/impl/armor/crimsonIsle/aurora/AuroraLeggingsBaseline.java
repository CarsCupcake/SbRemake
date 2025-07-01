package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora;

import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraBootsBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraLeggingsBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.DominusAbility;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.KuudraArmorType;
import net.minestom.server.color.Color;

import java.util.List;

public abstract class AuroraLeggingsBaseline extends KuudraLeggingsBaseline {
    @Override
    public KuudraArmorType armorType() {
        return KuudraArmorType.Aurora;
    }

    @Override
    public String getId() {
        return (armorTier().getId()) + "AURORA_LEGGINGS";
    }

    @Override
    public String getName() {
        return (armorTier().getName()) + " Aurora Leggings";
    }

    @Override
    public Color color() {
        return new Color(0x3f56fb);
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return AuroraBootsBaseline.ABILITIES;
    }
}
