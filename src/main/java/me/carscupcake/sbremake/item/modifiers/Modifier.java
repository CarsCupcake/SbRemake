package me.carscupcake.sbremake.item.modifiers;

import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlot;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.item.modifiers.reforges.Reforge;
import net.kyori.adventure.nbt.*;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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

    Modifier<Reforge> REFORGE = new Modifier<>() {
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
    Modifier<List<String>> UNLOCKED_GEMSTONE_SLOTS = new Modifier<>() {
        @Override
        public List<String> getFromNbt(SbItemStack item) {
            if (!(item.sbItem() instanceof GemstoneSlots)) return new ArrayList<>();
            CompoundBinaryTag gems = ((CompoundBinaryTag) item.item().getTag(Tag.NBT("ExtraAttributes"))).getCompound("gems");
            List<String> unlocked = new ArrayList<>();
            for (BinaryTag tag : gems.getList("unlocked_slots")) {
                unlocked.add(((StringBinaryTag) tag).value());
            }
            return unlocked;
        }

        @Override
        public SbItemStack toNbt(List<String> strings, SbItemStack itemStack) {
            CompoundBinaryTag extraAttributes = (CompoundBinaryTag) itemStack.item().getTag(Tag.NBT("ExtraAttributes"));
            CompoundBinaryTag gems = extraAttributes.getCompound("gems");
            List<BinaryTag> tags = new ArrayList<>();
            for (String s : strings) tags.add(StringBinaryTag.stringBinaryTag(s));
            ItemStack item = itemStack.item().withTag(Tag.NBT("ExtraAttributes"), extraAttributes.put("gems", gems.put("unlocked_slots", ListBinaryTag.listBinaryTag(BinaryTagTypes.STRING, tags))));
            return SbItemStack.from(item);
        }
    };
    Modifier<GemstoneSlot[]> GEMSTONE_SLOTS = new Modifier<>() {
        @Override
        public @Nullable GemstoneSlot[] getFromNbt(SbItemStack item) {
            if (!(item.sbItem() instanceof GemstoneSlots slots)) return new GemstoneSlot[0];
            GemstoneSlot[] gemstoneSlots = new GemstoneSlot[slots.getGemstoneSlots().length];
            CompoundBinaryTag gems = ((CompoundBinaryTag) item.item().getTag(Tag.NBT("ExtraAttributes"))).getCompound("gems");
            List<String> unlocked = item.getModifier(UNLOCKED_GEMSTONE_SLOTS);
            int i = 0;
            boolean[] unlockedByDefault = slots.getUnlocked();
            for (GemstoneSlotType type : slots.getGemstoneSlots()) {
                String slotId = STR."\{type.name().toUpperCase()}_\{i}";
                boolean isUnlocked = unlocked.contains(slotId) || unlockedByDefault[i];
                String gem = gems.getString(STR."\{slotId}_gem");
                if (gem.isEmpty()) {
                    gemstoneSlots[i] = new GemstoneSlot(type, null, isUnlocked);
                    i++;
                    continue;
                }
                String quality = gems.getString(slotId);
                Gemstone gemstone = Gemstone.gemstones.get(Gemstone.Type.byId(gem)).get(Gemstone.Quality.byId(quality));
                gemstoneSlots[i] = new GemstoneSlot(type, gemstone, isUnlocked);
                i++;
            }
            return gemstoneSlots;
        }

        @Override
        public SbItemStack toNbt(GemstoneSlot[] gemstoneSlots, SbItemStack itemStack) {
            if (!(itemStack.sbItem() instanceof GemstoneSlots slots)) return itemStack;
            CompoundBinaryTag extraAttributes = (CompoundBinaryTag) itemStack.item().getTag(Tag.NBT("ExtraAttributes"));
            CompoundBinaryTag gems = CompoundBinaryTag.empty();
            List<String> unlocked = new ArrayList<>();
            int i = 0;
            for (GemstoneSlot slot : gemstoneSlots) {
                String slotId = STR."\{slot.type().name().toUpperCase()}_\{i}";
                if (slot.unlocked() && !slots.getUnlocked()[i])
                    unlocked.add(slotId);
                if (slot.gemstone() != null) {
                    gems = gems.putString(slotId, slot.gemstone().quality().name()).putString(STR."\{slotId}_gem", slot.gemstone().type().name());
                }
                i++;
            }
            SbItemStack item = SbItemStack.from(itemStack.item().withTag(Tag.NBT("ExtraAttributes"), extraAttributes.put("gems", gems)));
            return item.withModifier(UNLOCKED_GEMSTONE_SLOTS, unlocked);
        }
    };
}
