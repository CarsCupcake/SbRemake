package me.carscupcake.sbremake.item.impl.armor.enderArmor;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.skill.Skill;
import net.minestom.server.color.Color;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class EnderBoots implements ISbItem, NpcSellable, ISbItem.StatProvider, ISoulbound {

    @Override
    public SoulboundType getSoulboundType() {
        return SoulboundType.Coop;
    }
    @Override
    public String getId() {
        return "ENDER_BOOTS";
    }

    @Override
    public String getName() {
        return "Ender Boots";
    }

    @Override
    public Material getMaterial() {
        return Material.CHAINMAIL_BOOTS;
    }

    @Override
    public ItemType getType() {
        return ItemType.Boots;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public int sellPrice() {
        return 10_000;
    }

    @Override
    public List<Requirement> requirements() {
        return List.of(new SkillRequirement(Skill.Combat, 21));
    }

    @Override
    public Lore getLore() {
        return new Lore("§7All stats of this armor are multiplied by §52x§7 while on the End Island!");
    }


    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 15, Stat.Defense, 25);
    }
}