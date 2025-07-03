package me.carscupcake.sbremake.item.impl.other.foraging;

import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class Lushlilac implements ISbItem, HeadWithValue {
    @Override
    public String getId() {
        return "LUSHLILAC";
    }

    @Override
    public String getName() {
        return "Lushlilac";
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
        return ItemRarity.RARE;
    }

    @Override
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTcxNjQzNjE4NDQzMSwKICAicHJvZmlsZUlkIiA6ICJiNzQ4YWExODk3ZGU0Y2RiYjFhNTI1YjVjMjM3ZDc2MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaXhpOCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80OTMwZTE2MGI4MzQwNWMzYjNiNTgxYjBkYWJmODk1MGQ2NTM3ZjMxNzZhYTBhM2Q4OTA4YTI3YWFlMWY3ZmViIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
    }
}
