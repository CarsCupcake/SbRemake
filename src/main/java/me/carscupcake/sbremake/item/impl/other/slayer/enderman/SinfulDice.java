package me.carscupcake.sbremake.item.impl.other.slayer.enderman;

import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class SinfulDice implements ISbItem, HeadWithValue {
    @Override
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTYyNjQ2Mjg4MDI0MywKICAicHJvZmlsZUlkIiA6ICI0NWY3YTJlNjE3ODE0YjJjODAwODM5MmRmN2IzNWY0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJfSnVzdERvSXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzMyZGVkMWI1YmQ3ZTBhNTIwNTBhYTZlY2E0NjU4ZmQ4YmI5ZDQ4YzNiYzUwYTAwOTk0NjQzYjEyOGEyM2RlMyIKICAgIH0KICB9Cn0=";
    }

    @Override
    public String getId() {
        return "SINFUL_DICE";
    }

    @Override
    public String getName() {
        return "Sinful Dice";
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
}
