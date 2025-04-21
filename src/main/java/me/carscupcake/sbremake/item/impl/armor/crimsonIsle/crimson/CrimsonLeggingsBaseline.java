package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson;

import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmor;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraLeggingsBaseline;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneItem;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.ItemCost;
import net.minestom.server.color.Color;
import net.minestom.server.item.Material;

import java.util.List;

public abstract class CrimsonLeggingsBaseline extends KuudraLeggingsBaseline {
    @Override
    public KuudraArmorType armorType() {
        return KuudraArmorType.Crimson;
    }

    @Override
    public String getId() {
        return (armorTier().getId()) + "CRIMSON_LEGGINGS";
    }

    @Override
    public String getName() {
        return (armorTier().getName()) + " Crimson Leggings";
    }

    @Override
    public Color color() {
        return new Color(0xe66105);
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return List.of(DominusAbility.INSTANCE);
    }
}
