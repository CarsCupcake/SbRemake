package me.carscupcake.sbremake.item.impl.armor.minersOutfit;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.Ability;
import net.minestom.server.color.Color;
import net.minestom.server.item.Material;

import java.util.List;

public class MinersOutfitLeggings implements ISbItem, ColoredLeather, NpcSellable {
    @Override
    public String getId() {
        return "MINER_OUTFIT_LEGGINGS";
    }

    @Override
    public String getName() {
        return "Miner's Outfit Leggings";
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER_LEGGINGS;
    }

    @Override
    public ItemType getType() {
        return ItemType.Leggings;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return List.of(HasteFullSetBonus.instance);
    }

    @Override
    public Color color() {
        return new Color(0x7a7964);
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Defense) return 30;
        return ISbItem.super.getStat(stat);
    }

    @Override
    public int sellPrice() {
        return 560;
    }
}
