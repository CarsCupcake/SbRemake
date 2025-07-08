package me.carscupcake.sbremake.item.impl.reforgeStone;

import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class HazmatEnderman implements ISbItem, HeadWithValue {

    @Override
    public String getId() {
        return "HAZMAT_ENDERMAN";
    }

    @Override
    public String getName() {
        return "Hazmat Enderman";
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
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTY0OTQxNzMzNTczMiwKICAicHJvZmlsZUlkIiA6ICJlM2I0NDVjODQ3ZjU0OGZiOGM4ZmEzZjFmN2VmYmE4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJNaW5pRGlnZ2VyVGVzdCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83MWU3Mjg5MGE3OWU1MDBjZjEzYTk3ZDEzNzRjNWFjOGE0ZjE1YTllMGQ2ODg1OTk3ZmM2ZjJlM2MxMTI1NGMiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
    }
}
