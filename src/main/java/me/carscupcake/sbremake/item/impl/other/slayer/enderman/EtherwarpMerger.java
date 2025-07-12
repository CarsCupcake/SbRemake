package me.carscupcake.sbremake.item.impl.other.slayer.enderman;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class EtherwarpMerger implements ISbItem, HeadWithValue, NpcSellable {
    @Override
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTcyMDA0MjA3MTMyOCwKICAicHJvZmlsZUlkIiA6ICI2NWUxN2M2Njk5NDU0ODQxYTdhNjc4ZWY2YzBhNGY1ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJPbGRSZWRUViIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zZTUzMTRmNDkxOTY5MWNjYmY4MDc3NDNkYWU0N2FlNDVhYzJlM2ZmMDhmNzllZWNkZDQ1MmZlNjAyZWZmN2Y2IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
    }

    @Override
    public String getId() {
        return "ETHERWARP_MERGER";
    }

    @Override
    public String getName() {
        return "Etherwarp Merger";
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
    public int sellPrice() {
        return 100_000;
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }

    private final Lore lore = new Lore("§8Crafted by the Etherchemist and dropped by the Voidgloom Seraph.");

    @Override
    public Lore getLore() {
        return lore;
    }
}
