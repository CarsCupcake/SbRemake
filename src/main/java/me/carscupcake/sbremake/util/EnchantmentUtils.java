package me.carscupcake.sbremake.util;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.GetItemStatEvent;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantments;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

import java.util.Set;

public class EnchantmentUtils {
    public static final EventNode<Event> LISTENER = EventNode.all("enchantments.listener").addListener(GetItemStatEvent.class, event -> {
        if (event.getStat() == Stat.MiningSpeed) {
            int level = event.getItemStack().getEnchantmentLevel(NormalEnchantments.Efficiency);
            if (level > 0) event.setValue(event.getValue() + 10 + 20 * level);
        }
        if (event.getStat() == Stat.MiningFortune) {
            int level = event.getItemStack().getEnchantmentLevel(NormalEnchantments.Fortune);
            if (level > 0) event.setValue(event.getValue() + getFortuneBonus(level));
        }
        if (event.getStat() == Stat.Pristine) {
            int level = event.getItemStack().getEnchantmentLevel(NormalEnchantments.Pristine);
            if (level > 0) event.setValue(event.getValue() + level);
        }
    });
    public static final Set<EntityType> SMITE_TYPES = Set.of(EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIFIED_PIGLIN, EntityType.WITHER);
    public static final Set<EntityType> BASE_OF_ARTHROPODS_TYPES = Set.of(EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.SILVERFISH);
    public static final Set<EntityType> ENDER_SLAYER_TYPES = Set.of(EntityType.ENDER_DRAGON, EntityType.ENDERMAN, EntityType.ENDERMITE);
    public static final Set<EntityType> CUBISM_TYPES = Set.of(EntityType.SLIME, EntityType.MAGMA_CUBE, EntityType.CREEPER);

    public static double getSharpnessBonus(int level) {
        return (level > 4 && level < 8) ? switch (level) {
            case 5 -> 0.3;
            case 6 -> 0.45;
            case 7 -> 0.65;
            default -> throw new IllegalStateException("Not Possible!");
        } : level * 5;
    }

    public static double getBaneOfArthropodsBonus(int level) {
        return level <= 4 ? level * 0.1 : (0.4 + (level - 4) * 0.2);
    }

    public static int getCriticalBonus(int level) {
        return level <= 5 ? (level * 10) : (level == 6 ? 70 : ((level - 7) * 20 + 100));
    }

    public static double getCubismBonus(int level) {
        return level <= 4 ? level * 0.1 : (0.4 + (level - 4) * 0.2);
    }

    public static int getDivineGiftBonus(int level) {
        return level * 2;
    }

    public static double getDragonHunterBonus(int level) {
        return level * 0.08;
    }

    public static double getEnderSlayerBonus(int level) {
        return level <= 4 ? level * 0.15 : (level >= 6 ? (0.8 + (level - 5) * 0.2) : (level * 0.2 - 10));
    }

    public static double getExecuteBonus(int level) {
        return level <= 5 ? (level * 0.002) : ((level - 5) * 0.0025 + 0.01);
    }

    public static double getProsecuteBonus(int level) {
        return level <= 4 ? (level * 0.001) : ((level - 5) * 0.003 + 0.007);
    }

    public static double getSmiteBonus(int level) {
        if (level <= 4) return level * 0.1d;
        return switch (level) {
            case 5 -> 0.6;
            case 6 -> 0.8;
            default -> (level + 3) * 0.1;
        };
    }

    public static double getCleaveBonus(int level) {
        double d = level * 0.03;
        if (level > 5) d += 0.02;
        return d;
    }

    public static int getFortuneBonus(int level) {
        return (level < 4) ? level * 10 : (5 + level * 10);
    }

    public static double getUltimateWiseBonus(int level) {
        return Math.min(level, 10) * 0.1;
    }
}
