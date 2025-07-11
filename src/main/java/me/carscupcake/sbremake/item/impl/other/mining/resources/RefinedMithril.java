package me.carscupcake.sbremake.item.impl.other.mining.resources;

import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class RefinedMithril implements ISbItem, HeadWithValue {
    @Override
    public String getId() {
        return "REFINED_MITHRIL";
    }

    @Override
    public String getName() {
        return "Refined Mithril";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTYxMDk5NjQ2NTYzMCwKICAicHJvZmlsZUlkIiA6ICI2ZmU4OTUxZDVhY2M0NDc3OWI2ZmYxMmU3YzFlOTQ2MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJlcGhlbXJhIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzM1YTc2NjMzMDA2MTliYjZhMTU2ZDc2MzUxYWMwNWY3YjNjYWZlYWMzMWUyZmYwNGM1NWNjOWYyMzYzMjc4MzIiCiAgICB9CiAgfQp9";
    }
}
