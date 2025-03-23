package me.carscupcake.sbremake.item.impl.armor;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.item.Material;

public record PerfectArmor(String id, String name, int level, ItemType type) implements ISbItem, GemstoneSlots {
    public PerfectArmor(int level, ItemType type) {
        this("PERFECT_" + (type.name().toUpperCase()) + "_" + (level) , "Perfect " + (type.name()) + " - Tier " + (StringUtils.toRoman(level)) , level, type);
        SbItemStack.initSbItem(this);
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Material getMaterial() {
        return switch (type) {
            case Helmet -> Material.DIAMOND_HELMET;
            case Chestplate -> Material.DIAMOND_CHESTPLATE;
            case Leggings -> Material.DIAMOND_LEGGINGS;
            case Boots -> Material.DIAMOND_BOOTS;
            default -> throw new IllegalStateException("Not allowed type");
        };
    }

    @Override
    public ItemType getType() {
        return type;
    }

    @Override
    public ItemRarity getRarity() {
        if (level > 7) return ItemRarity.LEGENDARY;
        if (level > 5) return ItemRarity.EPIC;
        return ItemRarity.RARE;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Defense) {
            return switch (type) {
                case Helmet -> 90;
                case Chestplate -> 140;
                case Leggings -> 120;
                case Boots -> 70;
                default -> throw new IllegalStateException("Not allowed type");
            } + (20 * level);
        }
        return ISbItem.super.getStat(stat);
    }
    public static void init() {
        for (int i = 1; i < 13; i++) {
            new PerfectArmor(i, ItemType.Helmet);
            new PerfectArmor(i, ItemType.Chestplate);
            new PerfectArmor(i, ItemType.Leggings);
            new PerfectArmor(i, ItemType.Boots);
        }
    }

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return new GemstoneSlotType[]{GemstoneSlotType.Amethyst, GemstoneSlotType.Amethyst, GemstoneSlotType.Amethyst};
    }

    @Override
    public boolean[] getUnlocked() {
        return new boolean[]{true, true, true};
    }

    @Override
    public Cost[][] getLockedSlotCost() {
        return new Cost[0][];
    }
}
