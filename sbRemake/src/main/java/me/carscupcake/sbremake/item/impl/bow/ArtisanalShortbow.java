package me.carscupcake.sbremake.item.impl.bow;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.skill.Skill;
import net.minestom.server.item.Material;

import java.util.List;

public class ArtisanalShortbow implements ISbItem, Shortbow, NpcSellable {
    @Override
    public String getId() {
        return "ARTISANAL_SHORTBOW";
    }

    @Override
    public String getName() {
        return "Artisanal Shortbow";
    }

    @Override
    public Material getMaterial() {
        return Material.BOW;
    }

    @Override
    public ItemType getType() {
        return ItemType.Bow;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Damage) return 40;
        return ISbItem.super.getStat(stat);
    }

    @Override
    public int sellPrice() {
        return 200;
    }

    @Override
    public List<Requirement> requirements() {
        return List.of(new SkillRequirement(Skill.Combat, 4));
    }
}
