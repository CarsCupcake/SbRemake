package me.carscupcake.sbremake.item.impl.armor.slayer.zombie;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class ZombiesHeart implements ISbItem, HeadWithValue, NpcSellable {
    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFkN2M4MTZmYzhjNjM2ZDdmNTBhOTNhMGJhN2FhZWZmMDZjOTZhNTYxNjQ1ZTllYjFiZWYzOTE2NTVjNTMxIn19fQ==";
    }

    @Override
    public String getId() {
        return "ZOMBIE_HEART";
    }

    @Override
    public String getName() {
        return "Zombie's Heart";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Helmet;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public int sellPrice() {
        return 123_000;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Health) return 50;
        return ISbItem.super.getStat(stat);
    }
}
