package me.carscupcake.sbremake.item;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.nbt.BinaryTag;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.tag.Tag;

import java.util.*;

@Builder
@Getter
public class ItemStackModifiers {
    public static final ItemStackModifiers BASE_MODIFIERS = new ItemStackModifiersBuilder().build();
    @Builder.Default
    private boolean glint = false;
    @Builder.Default
    private List<Enchantment> baseEnchantments = new ArrayList<>();
    @Builder.Default
    private Map<String, BinaryTag> baseTags = new HashMap<>();
    public ItemStack.Builder apply(ItemStack.Builder builder) {
        if (glint || !baseEnchantments.isEmpty())
            builder.set(ItemComponent.ENCHANTMENTS, new EnchantmentList(Enchantment.PROTECTION, 1).withTooltip(false));
        for (Map.Entry<String, BinaryTag> tagEntry : baseTags.entrySet()) {
            builder.setTag(Tag.NBT("ExtraAttributes"), tagEntry.getValue());
        }
        //TODO add enchantments here (to the nbt)
        return builder;
    }
}
