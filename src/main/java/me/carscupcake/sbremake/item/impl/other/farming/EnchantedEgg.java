package me.carscupcake.sbremake.item.impl.other.farming;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class EnchantedEgg implements ISbItem, NpcSellable {

    @Override
    public String getId() {
        return "ENCHANTED_EGG";
    }

    @Override
    public String getName() {
        return "Enchanted Egg";
    }

    @Override
    public Material getMaterial() {
        return Material.EGG;
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
        return new Lore("§7This item can be used as a minion upgrade for chicken minions. Guarantees that each chicken will drop an egg after they spawn.\n \nThe Enchanted Egg can be used to craft low-rarity pets.");
    }
}