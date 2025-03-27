package me.carscupcake.sbremake.item.impl.armor.slayer.zombie;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import me.carscupcake.sbremake.util.Cost;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class RevivedHeart implements ISbItem, HeadWithValue, ISbItem.StatProvider, GemstoneSlots {
    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWJiZmVmZTk3ODY0ODY1YTE5YWFhZDBmN2RjNDdiNmMzMTE3OTFiNGMzY2E4MTE1ZWYwN2FkOTlhYjU1NjU3ZCJ9fX0=";
    }

    @Override
    public String getId() {
        return "REVIVED_HEART";
    }

    @Override
    public String getName() {
        return "Revived Heart";
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
        return ItemRarity.EPIC;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 100, Stat.Defense, 35, Stat.Intelligence, 100);
    }

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return new GemstoneSlotType[]{GemstoneSlotType.Ruby};
    }

    @Override
    public List<Requirement> requirements() {
        return List.of(new SlayerRequirement(Slayers.Zombie, 6));
    }

    @Override
    public boolean[] getUnlocked() {
        return new boolean[]{true};
    }

    @Override
    public Cost[][] getLockedSlotCost() {
        return new Cost[0][];
    }
}
