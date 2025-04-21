package me.carscupcake.sbremake.item.impl.armor.crimsonIsle;

import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.DominusAbility;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.KuudraArmorType;
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

public abstract class KuudraBootsBaseline extends KuudraArmorCommons implements ColoredLeather {

    @Override
    public Material getMaterial() {
        return Material.LEATHER_BOOTS;
    }

    @Override
    public ItemType getType() {
        return ItemType.Boots;
    }
}
