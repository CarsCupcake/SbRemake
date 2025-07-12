package me.carscupcake.sbremake.item.impl.reforgeStone;

import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.modifiers.reforges.ArmorReforge;
import me.carscupcake.sbremake.item.modifiers.reforges.Reforge;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import net.minestom.server.item.Material;

import java.util.List;

public class PrecursorGear implements IReforgeStone, HeadWithValue {
    @Override
    public Reforge getReforge() {
        return ArmorReforge.Ancient;
    }

    private final List<Cost> common = List.of(new CoinsCost(10_000));
    private final List<Cost> uncommon = List.of(new CoinsCost(20_000));
    private final List<Cost> rare = List.of(new CoinsCost(30_000));
    private final List<Cost> epic = List.of(new CoinsCost(40_000));
    private final List<Cost> legendary = List.of(new CoinsCost(50_000));
    private final List<Cost> mythic = List.of(new CoinsCost(60_000));
    @Override
    public List<Cost> getCosts(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON ->  common;
            case UNCOMMON ->  uncommon;
            case RARE ->  rare;
            case EPIC ->  epic;
            case LEGENDARY ->  legendary;
            default -> mythic;
        };
    }

    @Override
    public String getId() {
        return "PRECURSOR_GEAR";
    }

    @Override
    public String getName() {
        return "Precursor Gear";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTU4OTMyNjI5NDg5NiwKICAicHJvZmlsZUlkIiA6ICJiZWNkZGIyOGEyYzg0OWI0YTliMDkyMmE1ODA1MTQyMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdFR2IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFmZjEwN2UyMGU5NWY1ZjZhYjYxYWU3M2RjNDI1MjNhMTEyZmM0NThjMzMzYTExNDgxMmIzMTNhMmI4ZGQ4YTciLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
    }
}
