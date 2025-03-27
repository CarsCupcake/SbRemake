package me.carscupcake.sbremake.util;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.GetItemStatEvent;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantment;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

import java.util.Set;

public class EnchantmentUtils {
    public static final EventNode<Event> LISTENER = EventNode.all("enchantments.listener")
            .addListener(GetItemStatEvent.class, event -> {
                if (event.getStat() == Stat.MiningSpeed) {
                    int level = event.getItemStack().getEnchantmentLevel(NormalEnchantment.Efficiency);
                    if (level > 0)
                        event.setValue(event.getValue() + 10 + 20 * level);
                }
                if (event.getStat() == Stat.MiningFortune) {
                    int level = event.getItemStack().getEnchantmentLevel(NormalEnchantment.Fortune);
                    if (level > 0)
                        event.setValue(event.getValue() + getFortuneBonus(level));
                }
                if (event.getStat() == Stat.Pristine) {
                    int level = event.getItemStack().getEnchantmentLevel(NormalEnchantment.Pristine);
                    if (level > 0)
                        event.setValue(event.getValue() + level);
                }
            });

    public static double getSharpnessBonus(int level) {
        return (level > 4 && level < 8) ? switch (level) {
            case 5 -> 0.3;
            case 6 -> 0.45;
            case 7 -> 0.65;
            default -> throw new IllegalStateException("Not Possible!");
        } : level * 5;
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

    public static final Set<EntityType> SMITE_TYPES = Set.of(EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIFIED_PIGLIN, EntityType.WITHER);

    public static int getFortuneBonus(int level) {
        return (level < 4) ? level * 10 : (5 + level * 10);
    }
}
