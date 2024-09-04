package me.carscupcake.sbremake.item.impl.other.farming;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class SuperEnchantedEgg implements ISbItem, NpcSellable {

    @Override
    public String getId() {
        return "SUPER_EGG";
    }

    @Override
    public String getName() {
        return "Super Enchanted Egg";
    }

    @Override
    public Material getMaterial() {
        return Material.GHAST_SPAWN_EGG;
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
    public ItemStackModifiers modifierBuilder() {
        return ItemStackModifiers.builder().glint(true).build();
    }


    @Override
    public int sellPrice() {
        return 432;
    }

    @Override
    public Lore getLore() {
        return new Lore("§7Used to craft epic and legendary pets.");
    }
}