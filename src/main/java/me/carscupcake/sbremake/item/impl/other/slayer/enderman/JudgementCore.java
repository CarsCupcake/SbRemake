package me.carscupcake.sbremake.item.impl.other.slayer.enderman;

import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class JudgementCore implements ISbItem, HeadWithValue {
    @Override
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTY1ODkwNDIzNTQ3MywKICAicHJvZmlsZUlkIiA6ICIxNDU1MDNhNDRjZmI0NzcwYmM3NWNjMTRjYjUwMDE4NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJMaWtlbHlFcmljIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk1ZjM3MjZmNmJmYmM4MWYwNzAyNWYxZDZjYmVjN2Y4ZjdmYTdhYmRmM2EwYTg2YjgxOTdlMDkyMjE0ODYwYmEiCiAgICB9CiAgfQp9";
    }

    @Override
    public String getId() {
        return "JUDGEMENT_CORE";
    }

    @Override
    public String getName() {
        return "Judgement Core";
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
        return ItemRarity.LEGENDARY;
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }
}
