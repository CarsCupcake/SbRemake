package me.carscupcake.sbremake.item.impl.other;

import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.requirements.CollectionRequirement;
import net.minestom.server.item.Material;

import java.util.List;

public class SculptorsAxe implements ISbItem, NpcSellable {
    @Override
    public String getId() {
        return "SCULPTORS_AXE";
    }

    @Override
    public String getName() {
        return "Sculptor's Axe";
    }

    @Override
    public Material getMaterial() {
        return Material.STONE_AXE;
    }

    @Override
    public ItemType getType() {
        return ItemType.Axe;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.COMMON;
    }

    @Override
    public int sellPrice() {
        return 64;
    }

    private final Lore lore = new Lore("Right-click on any log on your island to turn it into a 6 sided log!");
    @Override
    public Lore getLore() {
        return lore;
    }

    private final List<Requirement> requirements = List.of(new CollectionRequirement("BIRCH_WOOD", 4));
    @Override
    public List<Requirement> requirements() {
        return requirements;
    }
}
