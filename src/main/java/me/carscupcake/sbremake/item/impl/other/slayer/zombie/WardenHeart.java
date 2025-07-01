package me.carscupcake.sbremake.item.impl.other.slayer.zombie;

import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import net.minestom.server.item.Material;

import java.util.List;

public class WardenHeart implements ISbItem, HeadWithValue {
    @Override
    public String getId() {
        return "WARDEN_HEART";
    }

    @Override
    public String getName() {
        return "Warden Heart";
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
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ1ZjRkMTM5YzllODkyNjJlYzA2YjI3YWFhZDczZmE0ODhhYjQ5MjkwZDJjY2Q2ODVhMjU1NDcyNTM3M2M5YiJ9fX0=";
    }

    @Override
    public List<Requirement> requirements() {
        return List.of(new SlayerRequirement(Slayers.Zombie, 8));
    }
}
