package me.carscupcake.sbremake.item.modifiers.enchantment;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Lazy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface UltimateEnchantment extends SkyblockEnchantment {
    Set<UltimateEnchantment> ULTIMATE_ENCHANTMENTS = new HashSet<>();

    @Override
    default Set<? extends SkyblockEnchantment> conflicts() {
        return ULTIMATE_ENCHANTMENTS;
    }
}
