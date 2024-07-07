package me.carscupcake.sbremake.util;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

public class EnchantmentUtils {
    public static final EventNode<Event> LISTENER = EventNode.all("enchantments.listener");
    public static double getSharpnessBonus(int level) {
        return (level > 4 && level < 8) ? switch (level) {
            case 5 -> 0.3;
            case 6 -> 0.45;
            case 7 -> 0.65;
            default -> throw new IllegalStateException("Not Possible!");
        } : level * 5;
    }
}
