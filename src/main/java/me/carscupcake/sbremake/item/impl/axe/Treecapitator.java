package me.carscupcake.sbremake.item.impl.axe;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import net.minestom.server.item.Material;

import java.util.List;

public class Treecapitator implements ISbItem {
    @Override
    public String getId() {
        return "TREECAPITATOR_AXE";
    }

    @Override
    public String getName() {
        return "Treecapitator";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_AXE;
    }

    @Override
    public ItemType getType() {
        return ItemType.Axe;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public Lore getLore() {
        return new Lore(List.of("§7A forceful Gold Axe which can break", "§7a large amount of logs in a single hit!", "§8Cooldown: §a2s"));
    }

    @Override
    public LorePlace getLorePlacement() {
        return LorePlace.BelowAbility;
    }
}
