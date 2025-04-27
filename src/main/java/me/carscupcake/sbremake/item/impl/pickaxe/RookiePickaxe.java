package me.carscupcake.sbremake.item.impl.pickaxe;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantments;
import net.minestom.server.item.Material;

import java.util.Map;

public class RookiePickaxe implements ISbItem, ISbItem.StatProvider, NpcSellable, IVanillaPickaxe {
    @Override
    public String getId() {
        return "ROOKIE_PICKAXE";
    }

    @Override
    public String getName() {
        return "Rookie Pickaxe";
    }

    @Override
    public Material getMaterial() {
        return Material.STONE_PICKAXE;
    }

    @Override
    public ItemType getType() {
        return ItemType.Pickaxe;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.COMMON;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.MiningSpeed, 150, Stat.Damage, 15, Stat.BreakingPower, 2);
    }

    @Override
    public ItemStackModifiers modifierBuilder() {
        return ItemStackModifiers.builder().enchantments(Map.of(NormalEnchantments.Efficiency, 1)).build();
    }

    @Override
    public int sellPrice() {
        return 2;
    }

    @Override
    public VanillaPickaxeTier getTier() {
        return VanillaPickaxeTier.Stone;
    }
}
