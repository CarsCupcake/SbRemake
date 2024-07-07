package me.carscupcake.sbremake.item.modifiers.enchantment;

import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.tag.Tag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface SkyblockEnchantment {
    Map<String, SkyblockEnchantment> enchantments = new HashMap<>();

    int getMaxLevel();
    String getName();
    String getId();
    Lore description();
    ItemType[] types();

    default Set<SkyblockEnchantment> conflicts() {
        return new HashSet<>();
    }

    default boolean canApply(SkyblockEnchantment skyblockEnchantment) {
        return !this.conflicts().contains(skyblockEnchantment);
    }

    default ItemStack apply(ItemStack item, int level) {
        CompoundBinaryTag tag = (CompoundBinaryTag) item.getTag(Tag.NBT("ExtraAttributes"));
        CompoundBinaryTag enchantments = tag.getCompound("enchantments", CompoundBinaryTag.from(new HashMap<>())).putInt(getId(), level);;
        if (enchantments.size() == 1) {
            item = item.with(ItemComponent.ENCHANTMENTS, new EnchantmentList(Enchantment.PROTECTION, 1).withTooltip(false));
        }
        return item.withTag(Tag.NBT("ExtraAttributes"), tag.put("enchantments", enchantments));
    }
}
