package me.carscupcake.sbremake.item.modifiers;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.pets.IPet;
import me.carscupcake.sbremake.item.impl.pets.Pet;
import me.carscupcake.sbremake.item.impl.pets.PetItem;
import me.carscupcake.sbremake.item.impl.rune.Rune;
import me.carscupcake.sbremake.item.modifiers.attributes.AbstractAttribute;
import me.carscupcake.sbremake.item.modifiers.attributes.Attribute;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlot;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.item.modifiers.potion.PotionInfo;
import me.carscupcake.sbremake.item.modifiers.reforges.Reforge;
import me.carscupcake.sbremake.player.potion.IPotion;
import me.carscupcake.sbremake.player.potion.PotionType;
import me.carscupcake.sbremake.util.Pair;
import net.kyori.adventure.nbt.*;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.item.component.HeadProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("unused")
public interface Modifier<T> {
    Modifier<UUID> ITEM_ID = new Modifier<>() {
        @Override
        public @Nullable UUID getFromNbt(SbItemStack item) {
            var nbt = item.item().get(DataComponents.CUSTOM_DATA);
            if (nbt == null) return null;
            var uuid = nbt.nbt().getString("uuid");
            return UUID.fromString(uuid);
        }

        @Override
        public SbItemStack toNbt(UUID i, SbItemStack itemStack) {
            return SbItemStack.from(itemStack.item().with(DataComponents.CUSTOM_DATA,
                                                          new CustomData(Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA)).nbt().putString("uuid", i.toString()))));
        }
    };
    Modifier<Reforge> REFORGE = new Modifier<>() {
        @Override
        public Reforge getFromNbt(SbItemStack item) {
            if (item == SbItemStack.AIR) return null;
            String id = Objects.requireNonNull(item.item().get(DataComponents.CUSTOM_DATA)).nbt().getString("reforge", "");
            if (id.isEmpty()) return null;
            return Reforge.reforges.get(id);
        }

        @Override
        public SbItemStack toNbt(Reforge reforge, SbItemStack itemStack) {
            ItemStack item = itemStack.item();
            CompoundBinaryTag tag = Objects.requireNonNull(item.get(DataComponents.CUSTOM_DATA)).nbt();
            return SbItemStack.from(item.with(DataComponents.CUSTOM_DATA, new CustomData(tag.putString("reforge", reforge.getId()))));
        }
    };
    Modifier<Map<SkyblockEnchantment, Integer>> ENCHANTMENTS = new Modifier<>() {
        @Override
        public Map<SkyblockEnchantment, Integer> getFromNbt(SbItemStack item) {
            if (item == SbItemStack.AIR) return new HashMap<>();
            CompoundBinaryTag enchantments = Objects.requireNonNull(item.item().get(DataComponents.CUSTOM_DATA)).nbt().getCompound("enchantments");
            Map<SkyblockEnchantment, Integer> enchantmentMap = new HashMap<>();
            for (String key : enchantments.keySet()) {
                enchantmentMap.put(SkyblockEnchantment.enchantments.get(key), enchantments.getInt(key));
            }
            return enchantmentMap;
        }

        @Override
        public SbItemStack toNbt(Map<SkyblockEnchantment, Integer> skyblockEnchantmentIntegerMap, SbItemStack itemStack) {
            CompoundBinaryTag extraAttributes = Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA)).nbt();
            CompoundBinaryTag enchantments = extraAttributes.getCompound("enchantments", CompoundBinaryTag.empty());
            for (Map.Entry<SkyblockEnchantment, Integer> entry : skyblockEnchantmentIntegerMap.entrySet()) {
                enchantments = enchantments.putInt(entry.getKey().getId(), entry.getValue());
            }
            extraAttributes = extraAttributes.put("enchantments", enchantments);
            return SbItemStack.from(itemStack.item().with(DataComponents.CUSTOM_DATA, new CustomData(extraAttributes)));
        }
    };
    Modifier<List<String>> UNLOCKED_GEMSTONE_SLOTS = new Modifier<>() {
        @Override
        public List<String> getFromNbt(SbItemStack item) {
            if (item == SbItemStack.AIR) return new ArrayList<>();
            if (!(item.sbItem() instanceof GemstoneSlots)) return new ArrayList<>();
            CompoundBinaryTag gems = Objects.requireNonNull(item.item().get(DataComponents.CUSTOM_DATA)).nbt().getCompound("gems");
            List<String> unlocked = new ArrayList<>();
            for (BinaryTag tag : gems.getList("unlocked_slots")) {
                unlocked.add(((StringBinaryTag) tag).value());
            }
            return unlocked;
        }

        @Override
        public SbItemStack toNbt(List<String> strings, SbItemStack itemStack) {
            CompoundBinaryTag extraAttributes = Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA)).nbt();
            CompoundBinaryTag gems = extraAttributes.getCompound("gems");
            List<BinaryTag> tags = new ArrayList<>();
            for (String s : strings) tags.add(StringBinaryTag.stringBinaryTag(s));
            ItemStack item = itemStack.item().with(DataComponents.CUSTOM_DATA, new CustomData(extraAttributes.put("gems", gems.put("unlocked_slots", ListBinaryTag.listBinaryTag(BinaryTagTypes.STRING, tags)))));
            return SbItemStack.from(item);
        }
    };
    Modifier<GemstoneSlot[]> GEMSTONE_SLOTS = new Modifier<>() {
        @Override
        public @Nullable GemstoneSlot[] getFromNbt(SbItemStack item) {
            if (item == SbItemStack.AIR) return new GemstoneSlot[0];
            if (!(item.sbItem() instanceof GemstoneSlots slots)) return new GemstoneSlot[0];
            GemstoneSlot[] gemstoneSlots = new GemstoneSlot[slots.getGemstoneSlots().length];
            CompoundBinaryTag gems = Objects.requireNonNull(item.item().get(DataComponents.CUSTOM_DATA)).nbt().getCompound("gems");
            List<String> unlocked = item.getModifier(UNLOCKED_GEMSTONE_SLOTS);
            int i = 0;
            boolean[] unlockedByDefault = slots.getUnlocked();
            for (GemstoneSlotType type : slots.getGemstoneSlots()) {
                String slotId = (type.name().toUpperCase()) + "_" + (i);
                boolean isUnlocked = unlocked.contains(slotId) || unlockedByDefault[i];
                String gem = gems.getString((slotId) + "_gem");
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
            CompoundBinaryTag extraAttributes = Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA)).nbt();
            CompoundBinaryTag gems = CompoundBinaryTag.empty();
            List<String> unlocked = new ArrayList<>();
            int i = 0;
            for (GemstoneSlot slot : gemstoneSlots) {
                String slotId = (slot.type().name().toUpperCase()) + "_" + (i);
                if (slot.unlocked() && !slots.getUnlocked()[i]) unlocked.add(slotId);
                if (slot.gemstone() != null) {
                    gems = gems.putString(slotId, slot.gemstone().quality().name()).putString((slotId) + "_gem", slot.gemstone().type().name());
                }
                i++;
            }
            SbItemStack item = SbItemStack.from(itemStack.item().with(DataComponents.CUSTOM_DATA, new CustomData(extraAttributes.put("gems", gems))));
            return item.withModifier(UNLOCKED_GEMSTONE_SLOTS, unlocked);
        }
    };
    Modifier<Pair<Attribute, Attribute>> ATTRIBUTES = new Modifier<>() {

        @Override
        public @Nullable Pair<Attribute, Attribute> getFromNbt(SbItemStack item) {
            if (item == SbItemStack.AIR) return null;
            var extraAttributes = Objects.requireNonNull(item.item().get(DataComponents.CUSTOM_DATA)).nbt();
            var attributes = extraAttributes.getCompound("attributes", CompoundBinaryTag.empty());
            if (CompoundBinaryTag.empty() == attributes) return null;
            if (attributes.size() != 2) return null;
            var items = attributes.iterator();
            var first = items.next();
            var second = items.next();
            return new Pair<>(new Attribute(AbstractAttribute.attributes.get(first.getKey()), ((IntBinaryTag) first.getValue()).value()), new Attribute(AbstractAttribute.attributes.get(second.getKey()), ((IntBinaryTag) second.getValue()).value()));
        }

        @Override
        public SbItemStack toNbt(Pair<Attribute, Attribute> abstractAttributeAbstractAttributePair, SbItemStack itemStack) {
            var extraAttributes = Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA)).nbt();
            var attributes = CompoundBinaryTag.builder().putInt(abstractAttributeAbstractAttributePair.getFirst().attribute().getId(), abstractAttributeAbstractAttributePair.getFirst().level()).putInt(abstractAttributeAbstractAttributePair.getSecond().attribute().getId(), abstractAttributeAbstractAttributePair.getSecond().level()).build();
            return SbItemStack.from(itemStack.item().with(DataComponents.CUSTOM_DATA, new CustomData(extraAttributes.put("attributes", attributes))));
        }
    };
    Modifier<Pet.PetInfo> PET_INFO = new Modifier<>() {
        @Override
        public Pet.PetInfo getFromNbt(SbItemStack item) {
            CompoundBinaryTag extraAttributes = Objects.requireNonNull(item.item().get(DataComponents.CUSTOM_DATA)).nbt();
            CompoundBinaryTag petInfoTag = extraAttributes.getCompound("petInfo");
            if (CompoundBinaryTag.empty() == petInfoTag)
                return new Pet.PetInfo(null, ItemRarity.SPECIAL, 0, null, 0);
            IPet pet = IPet.pets.get(petInfoTag.getString("type"));
            ISbItem petItemId = SbItemStack.raw(petInfoTag.getString("heldItem"));
            PetItem petItem = (petItemId == null) ? null : (PetItem) petItemId;
            return new Pet.PetInfo(pet, ItemRarity.valueOf(petInfoTag.getString("tier")), petInfoTag.getDouble("exp"), petItem, petInfoTag.getInt("candyUsed"));
        }

        @Override
        public SbItemStack toNbt(Pet.PetInfo petInfo, SbItemStack itemStack) {
            ItemStack item = itemStack.item().with(DataComponents.PROFILE, new HeadProfile(new PlayerSkin("", petInfo.pet().skullValue())));
            CompoundBinaryTag extraAttributes = Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA)).nbt();
            CompoundBinaryTag petInfoTag = extraAttributes.getCompound("petInfo");
            petInfoTag = petInfoTag.putString("type", petInfo.pet().getId()).putDouble("exp", petInfo.exp()).putInt("candyUsed", petInfo.petCandyUsed()).putString("tier", petInfo.rarity().name());
            if (petInfo.petItem() == null) petInfoTag = petInfoTag.remove("heldItem");
            else petInfoTag = petInfoTag.putString("heldItem", petInfo.petItem().getId());
            return SbItemStack.from(item.with(DataComponents.CUSTOM_DATA, new CustomData(extraAttributes.put("petInfo", petInfoTag))));
        }
    };
    Modifier<List<PotionInfo.PotionEffect>> POTION_EFFECTS = new Modifier<>() {
        @Override
        public List<PotionInfo.PotionEffect> getFromNbt(SbItemStack item) {
            List<PotionInfo.PotionEffect> effects = new ArrayList<>();
            for (BinaryTag binaryTag : Objects.requireNonNull(item.item().get(DataComponents.CUSTOM_DATA)).nbt().getList("effects")) {
                CompoundBinaryTag c = (CompoundBinaryTag) binaryTag;
                effects.add(new PotionInfo.PotionEffect(IPotion.potions.get(c.getString("effect")), c.getByte("level"), c.getLong("duration_ticks")));
            }
            return effects;
        }

        @Override
        public SbItemStack toNbt(List<PotionInfo.PotionEffect> effects, SbItemStack itemStack) {
            CompoundBinaryTag extraAttributes = Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA)).nbt();
            List<BinaryTag> tags = new ArrayList<>();
            for (PotionInfo.PotionEffect effect : effects) {
                tags.add(CompoundBinaryTag.empty().putString("effect", effect.potion().getId()).putByte("level", effect.level()).putLong("duration_ticks", effect.durationTicks()));
            }
            return SbItemStack.from(itemStack.item().with(DataComponents.CUSTOM_DATA, new CustomData(extraAttributes.put("effects", ListBinaryTag.listBinaryTag(BinaryTagTypes.COMPOUND, tags)))));
        }
    };
    Modifier<PotionInfo> POTION = new Modifier<>() {
        @Override
        public @Nullable PotionInfo getFromNbt(SbItemStack item) {
            CompoundBinaryTag extraAttributes = Objects.requireNonNull(item.item().get(DataComponents.CUSTOM_DATA)).nbt();
            String potionType = extraAttributes.getString("potion_type");
            if (potionType.isEmpty()) return null;
            String potion = extraAttributes.getString("potion");
            if (potion.isEmpty()) return null;
            List<PotionInfo.PotionEffect> effects = item.getModifier(POTION_EFFECTS);
            String customPotionName = extraAttributes.getString("potion_name");
            return new PotionInfo(IPotion.potions.get(potion), extraAttributes.getBoolean("enhanced"), extraAttributes.getBoolean("extended"), extraAttributes.getByte("potion_level"), PotionType.valueOf(potionType), customPotionName.isEmpty() ? null : customPotionName, effects);
        }

        @Override
        public SbItemStack toNbt(PotionInfo potionInfo, SbItemStack itemStack) {
            itemStack = itemStack.withModifier(POTION_EFFECTS, potionInfo.effects());
            CompoundBinaryTag extraAttributes = Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA)).nbt();
            if (potionInfo.customPotionName() != null)
                extraAttributes = extraAttributes.putString("potion_name", potionInfo.customPotionName());
            return SbItemStack.from(itemStack.item().with(DataComponents.CUSTOM_DATA,
                    new CustomData(extraAttributes.putString("potion", potionInfo.potion().getId()).putBoolean("enhanced", potionInfo.enhanced()).putBoolean("extended", potionInfo.extended()).putByte("potion_level", potionInfo.potionLevel()).putString("potion_type", potionInfo.potionType().name()))));
        }
    };
    Modifier<RuneModifier> RUNE = new Modifier<>() {
        @Override
        public @Nullable RuneModifier getFromNbt(SbItemStack item) {
            CompoundBinaryTag extraAttributes = Objects.requireNonNull(item.item().get(DataComponents.CUSTOM_DATA)).nbt();
            CompoundBinaryTag tag = extraAttributes.getCompound("runes");
            Iterator<Map.Entry<String, ? extends BinaryTag>> it = tag.iterator();
            if (!it.hasNext()) return null;
            Map.Entry<String, ? extends BinaryTag> entry = it.next();
            return new RuneModifier(Rune.runes.get(entry.getKey()), ((IntBinaryTag) entry.getValue()).value());
        }

        @Override
        public SbItemStack toNbt(RuneModifier iRune, SbItemStack itemStack) {
            return SbItemStack.from(itemStack.item().with(DataComponents.CUSTOM_DATA, new CustomData(Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA)).nbt().put("runes", iRune == null ?
                    CompoundBinaryTag.empty() : CompoundBinaryTag.empty().putInt(iRune.rune().getId(), iRune.level())))));
        }
    };
    Modifier<Integer> STARS = new Modifier<>() {
        @Override
        public Integer getFromNbt(SbItemStack item) {
            return (Objects.requireNonNull(item.item().get(DataComponents.CUSTOM_DATA)).nbt()).getInt("upgrade_level", 0);
        }

        @Override
        public SbItemStack toNbt(Integer integer, SbItemStack itemStack) {
            return SbItemStack.from(itemStack.item().with(DataComponents.CUSTOM_DATA, new CustomData((Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA)).nbt()).putInt("upgrade_level", integer))));
        }
    };

    Modifier<Integer> RarityUpgrades = new Modifier<>() {
        @Override
        public @NotNull Integer getFromNbt(SbItemStack item) {
            if (item == SbItemStack.AIR) return 0;
            return (Objects.requireNonNull(item.item().get(DataComponents.CUSTOM_DATA)).nbt()).getInt("rarity_upgrades", 0);
        }

        @Override
        public SbItemStack toNbt(Integer i, SbItemStack itemStack) {
            return SbItemStack.from(itemStack.item().with(DataComponents.CUSTOM_DATA, new CustomData(Objects.requireNonNull(itemStack.item().get(DataComponents.CUSTOM_DATA)).nbt().putInt("rarity_upgrades", i))));
        }
    };

    default T get(SbItemStack item) {
        Object o = item.modifierCache().get(this);
        if (o != null) return (T) o;
        T t = getFromNbt(item);
        item.modifierCache().put(this, t);
        return t;
    }

    @Nullable T getFromNbt(SbItemStack item);

    SbItemStack toNbt(T t, SbItemStack itemStack);
}
