package me.carscupcake.sbremake.item.impl.other.slayer.zombie;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class RevenantViscera implements ISbItem, NpcSellable {
    @Override
    public String getId() {
        return "REVENANT_VISCERA";
    }

    @Override
    public String getName() {
        return "Revenant Viscera";
    }

    @Override
    public Material getMaterial() {
        return Material.COOKED_PORKCHOP;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public int sellPrice() {
        return 128;
    }

    @Override
    public ItemStackModifiers modifierBuilder() {
        return ItemStackModifiers.builder().glint(true).build();
    }
}
