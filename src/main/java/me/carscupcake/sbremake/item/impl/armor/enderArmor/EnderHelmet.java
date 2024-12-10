package me.carscupcake.sbremake.item.impl.armor.enderArmor;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.skill.Skill;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class EnderHelmet implements ISbItem, NpcSellable, ISbItem.StatProvider, HeadWithValue, ISoulbound {
    @Override
    public SoulboundType getSoulboundType() {
        return SoulboundType.Coop;
    }
    @Override
    public String getId() {
        return "ENDER_HELMET";
    }

    @Override
    public String getName() {
        return "Ender Helmet";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Helmet;
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
        return Map.of(Stat.Health, 20, Stat.Defense, 35);
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjhjNzVkYzU4M2ZhY2MzNDU1ZGYzNWMyZDVkNTU1YmI0YTkwZTEwNDliMGEyZTcxOTNlYmJkZDQ0NTY4ODM4NyJ9fX0=";
    }
}
