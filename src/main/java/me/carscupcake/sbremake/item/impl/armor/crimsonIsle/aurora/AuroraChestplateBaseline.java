package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora;

import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraBootsBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraChestplateBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.KuudraArmorType;
import net.minestom.server.color.Color;

import java.util.List;

public abstract class AuroraChestplateBaseline extends KuudraChestplateBaseline {
    @Override
    public KuudraArmorType armorType() {
        return KuudraArmorType.Aurora;
    }

    @Override
    public String getId() {
        return (armorTier().getId()) + "AURORA_CHESTPLATE";
    }

    @Override
    public String getName() {
        return (armorTier().getName()) + " Aurora Chestplate";
    }

    @Override
    public Color color() {
        return new Color(0x2841f1);
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return AuroraBootsBaseline.ABILITIES;
    }
}
