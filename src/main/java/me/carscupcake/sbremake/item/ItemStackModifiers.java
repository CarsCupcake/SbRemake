package me.carscupcake.sbremake.item;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.NumberBinaryTag;
import net.minestom.server.component.DataComponent;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.tag.Tag;

import javax.swing.text.html.parser.Entity;
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
    @Builder.Default
    private Map<SkyblockEnchantment, Integer> enchantments = new HashMap<>();
    public ItemStack.Builder apply(ItemStack.Builder builder) {
        if (glint || !baseEnchantments.isEmpty())
            builder.set(ItemComponent.ENCHANTMENTS, new EnchantmentList(Enchantment.PROTECTION, 1).withTooltip(false));
        for (Map.Entry<String, BinaryTag> tagEntry : baseTags.entrySet()) {
            builder.setTag(Tag.NBT("ExtraAttributes"), tagEntry.getValue());
        }
        return builder;
    }

    public ItemStack apply(ItemStack item) {
        CompoundBinaryTag tag = (CompoundBinaryTag) item.getTag(Tag.NBT("ExtraAttributes"));
        CompoundBinaryTag enchantments = tag.getCompound("enchantments");
        for (Map.Entry<SkyblockEnchantment, Integer> entry : this.enchantments.entrySet())
            enchantments.putInt(entry.getKey().getId(), (entry.getValue()));
        tag.put("enchantments", enchantments);
        return item.withTag(Tag.NBT("ExtraAttributes"), tag);
    }
}
