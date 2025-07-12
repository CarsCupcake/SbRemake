package me.carscupcake.sbremake.item.impl.other.combat;

import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class SummoningEye implements ISbItem, HeadWithValue {
    @Override
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTYwMTg3NzExNDUyMSwKICAicHJvZmlsZUlkIiA6ICI0ZDcwNDg2ZjUwOTI0ZDMzODZiYmZjOWMxMmJhYjRhZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJzaXJGYWJpb3pzY2hlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2QzZDQ4MjI0YWZmODUzNTdjMzc0NTk1MWE0ZDg1ZTkyNGExMmMyNTgwZGMzYWQyODU0Y2JjZjNjMTJhZWE0ZDciCiAgICB9CiAgfQp9";
    }

    @Override
    public String getId() {
        return "SUMMONING_EYE";
    }

    @Override
    public String getName() {
        return "Summoning Eye";
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
    public boolean isUnstackable() {
        return true;
    }
}
