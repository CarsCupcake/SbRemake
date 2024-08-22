package me.carscupcake.sbremake.item.modifiers;

import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import me.carscupcake.sbremake.item.modifiers.reforges.Reforge;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public interface Modifier<T> {
    default T get(SbItemStack item) {
        Object o = item.modifierCache().get(this);
        if (o != null) return (T) o;
        T t = getFromNbt(item);
        item.modifierCache().put(this, t);
        return t;
    }
    @Nullable T getFromNbt(SbItemStack item);
    SbItemStack toNbt(T t, SbItemStack itemStack);
    Modifier<Reforge> REFORGE = new Modifier<Reforge>() {
        @Override
        public Reforge getFromNbt(SbItemStack item) {
            String id = ((CompoundBinaryTag) item.item().getTag(Tag.NBT("ExtraAttributes"))).getString("reforge", "");
            if (id.isEmpty()) return null;
            return Reforge.reforges.get(id);
        }

        @Override
        public SbItemStack toNbt(Reforge reforge, SbItemStack itemStack) {
            ItemStack item = itemStack.item();
            CompoundBinaryTag tag = (CompoundBinaryTag) item.getTag(Tag.NBT("ExtraAttributes"));
            return SbItemStack.from(item.withTag(Tag.NBT("ExtraAttributes"), tag.putString("reforge", reforge.getId())));
        }
    };
    Modifier<Map<SkyblockEnchantment, Integer>> ENCHANTMENTS = new Modifier<>() {
        @Override
        public Map<SkyblockEnchantment, Integer> getFromNbt(SbItemStack item) {
            CompoundBinaryTag enchantments = ((CompoundBinaryTag) item.item().getTag(Tag.NBT("ExtraAttributes"))).getCompound("enchantments");
            Map<SkyblockEnchantment, Integer> enchantmentMap = new HashMap<>();
            for (String key : enchantments.keySet()) {
                enchantmentMap.put(SkyblockEnchantment.enchantments.get(key), enchantments.getInt(key));
            }
            return enchantmentMap;
        }

        @Override
        public SbItemStack toNbt(Map<SkyblockEnchantment, Integer> skyblockEnchantmentIntegerMap, SbItemStack itemStack) {
            CompoundBinaryTag extraAttributes = (CompoundBinaryTag) itemStack.item().getTag(Tag.NBT("ExtraAttributes"));
            CompoundBinaryTag enchantments = extraAttributes.getCompound("enchantments", CompoundBinaryTag.empty());
            for (Map.Entry<SkyblockEnchantment, Integer> entry : skyblockEnchantmentIntegerMap.entrySet()) {
                enchantments = enchantments.putInt(entry.getKey().getId(), entry.getValue());
            }
            extraAttributes = extraAttributes.put("enchantments", enchantments);
            return SbItemStack.from(itemStack.item().withTag(Tag.NBT("ExtraAttributes"), extraAttributes));
        }
    };
}
