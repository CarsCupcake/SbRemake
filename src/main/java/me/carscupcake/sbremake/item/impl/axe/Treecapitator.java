package me.carscupcake.sbremake.item.impl.axe;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.ItemCost;
import net.minestom.server.item.Material;

import java.util.List;

public class Treecapitator implements ISbItem, GemstoneSlots {
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

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return new GemstoneSlotType[]{GemstoneSlotType.Citrine, GemstoneSlotType.Citrine};
    }

    @Override
    public boolean[] getUnlocked() {
        return new boolean[]{false, false};
    }

    @Override
    public Cost[][] getLockedSlotCost() {
        return new Cost[][]{{new CoinsCost(50_000), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Citrine).get(Gemstone.Quality.Fine).asItem(), 20)},
                {new CoinsCost(100_000), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Citrine).get(Gemstone.Quality.Fine).asItem(), 40)}};
    }
}
