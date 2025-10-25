package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.config.KeyClass;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.AbilityType;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import me.carscupcake.sbremake.item.crafting.CraftingIngredient;
import me.carscupcake.sbremake.item.crafting.ShapedRecipe;
import me.carscupcake.sbremake.item.impl.armor.PerfectArmor;
import me.carscupcake.sbremake.item.impl.pets.IPet;
import me.carscupcake.sbremake.item.impl.pets.Pets;
import me.carscupcake.sbremake.item.minion.IMinionData;
import me.carscupcake.sbremake.item.minion.MinionItem;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import me.carscupcake.sbremake.item.modifiers.enchantment.UltimateEnchantment;
import me.carscupcake.sbremake.item.modifiers.enchantment.UltimateEnchantments;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneItem;
import me.carscupcake.sbremake.item.requirements.CollectionRequirement;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.worlds.impl.PrivateIsle;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.item.component.HeadProfile;
import net.minestom.server.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;

public interface ISbItem extends KeyClass {
    @Override
    default String key() {
        return getId();
    }
    String getId();

    String getName();

    Material getMaterial();

    ItemType getType();

    ItemRarity getRarity();

    default boolean isDungeonItem() {
        return false;
    }

    default ItemStack.Builder build(ItemStack.Builder builder) {
        return builder;
    }

    default ItemStackModifiers modifierBuilder() {
        return ItemStackModifiers.BASE_MODIFIERS;
    }

    default @Nullable Lore statsReplacement() {
        return null;
    }

    default boolean allowUpdates() {
        return true;
    }

    default StatProvider statProvider() {
        if (this instanceof StatProvider provider) return provider;
        return null;
    }

    default double getStat(Stat stat) {
        StatProvider provider = statProvider();
        if (provider != null) return provider.stats().getOrDefault(stat, 0d).doubleValue();
        return 0;
    }

    default Lore getLore() {
        return Lore.EMPTY;
    }

    default LorePlace getLorePlacement() {
        return LorePlace.AboveAbility;
    }

    default List<Ability> getDefaultAbilities() {
        return new ArrayList<>();
    }

    default List<Requirement> requirements() {
        return new ArrayList<>();
    }

    default int getMaxStackSize() {
        return isUnstackable() ? 1 : getMaterial().maxStackSize();
    }

    default boolean isUnstackable() {
        return getMaterial().maxStackSize() == 1;
    }

    default boolean hasRequirements(SkyblockPlayer player, ItemStack item) {
        for (Requirement requirement : requirements()) {
            if (!requirement.canUse(player, item)) {
                return false;
            }
        }
        return true;
    }

    Set<DataComponent<?>> HIDDEN_COMPONENTS = Set.of(DataComponents.ENCHANTABLE, DataComponents.ENCHANTMENTS,
            DataComponents.DAMAGE, DataComponents.USE_COOLDOWN, DataComponents.POTION_DURATION_SCALE, DataComponents.WEAPON, DataComponents.REPAIRABLE,
            DataComponents.DYED_COLOR, DataComponents.ATTRIBUTE_MODIFIERS, DataComponents.PROFILE, DataComponents.BANNER_PATTERNS);

    default SbItemStack create() {
        ItemStack.Builder builder = ItemStack.builder(getMaterial()).set(DataComponents.ATTRIBUTE_MODIFIERS, new AttributeList(List.of()));
        if (getMaterial() == Material.PLAYER_HEAD && this instanceof HeadWithValue value) {
            builder.set(DataComponents.PROFILE, new HeadProfile(new PlayerSkin(value.value(), "")));
        }
        if (this instanceof ColoredLeather leather) {
            builder.set(DataComponents.DYED_COLOR, new Color(leather.color().asRGB()));
        }
        ItemStackModifiers modifiers = modifierBuilder();
        CompoundBinaryTag.Builder b = CompoundBinaryTag.builder().putString("id", getId());
        if (isUnstackable()) b = b.putString("uuid", UUID.randomUUID().toString());
        ItemStack item = build(modifiers.apply(builder.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false, HIDDEN_COMPONENTS)).set(DataComponents.CUSTOM_DATA, new CustomData(b.build())))).build();
        SbItemStack itemStack = SbItemStack.from(modifiers.apply(item));
        return itemStack.update();
    }

    enum LorePlace {
        Top, AboveAbility, BelowAbility
    }

    interface StatProvider {
        Map<Stat, Number> stats();
    }

    static void init() {
        UltimateEnchantment.ULTIMATE_ENCHANTMENTS.addAll(Arrays.asList(UltimateEnchantments.values()));
        for (Material material : Material.values()) {
            String name = StringUtils.connect(material.key().value().split("_"), true);
            SbItemStack.initSbItem(new BaseSbItem(material, name));
        }
        HashMap<ISbItem, EnchantedRecipe> recipes = new HashMap<>();
        GemstoneItem.init();
        var reflections = new Reflections("me.carscupcake.sbremake.item.impl");
        for (var clazz : reflections.getSubTypesOf(ISbItem.class)) {
            try {
                if (clazz.isInterface()) continue;
                if (clazz.isRecord()) continue;
                if (clazz.isEnum()) continue;
                if (Modifier.isAbstract(clazz.getModifiers())) continue;
                Constructor<? extends ISbItem> constructor = clazz.getConstructor();
                ISbItem instance = constructor.newInstance();
                SbItemStack.initSbItem(instance);
                if (instance instanceof Listener listener) {
                    MinecraftServer.getGlobalEventHandler().addChild(listener.node());
                }
                if (instance instanceof EnchantedRecipe r) recipes.put(instance, r);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
        reflections = new Reflections("me.carscupcake.sbremake.item.impl.minion");
        for (var clazz : reflections.getSubTypesOf(IMinionData.class)) {
            try {
                if (clazz.isInterface()) continue;
                if (clazz.isRecord()) continue;
                if (clazz.isEnum()) continue;
                if (Modifier.isAbstract(clazz.getModifiers())) continue;
                var constructor = clazz.getConstructor();
                var instance = constructor.newInstance();
                registerMinion(instance);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
        PerfectArmor.init();
        for (Pets pets : Pets.values())
            IPet.pets.put(pets.getId(), pets);
        for (Map.Entry<ISbItem, EnchantedRecipe> recipe : recipes.entrySet()) {
            Map<Character, CraftingIngredient> items = Map.of('#', new CraftingIngredient(32, recipe.getValue().base()));
            Requirement[] requirements = new Requirement[]{new CollectionRequirement(recipe.getValue().collection(), recipe.getValue().level())};
            Recipe.craftingRecipes.put(recipe.getKey().getId().toLowerCase(), ShapedRecipe.createShapedRecipe(recipe.getKey(), 1, -1, requirements, items, "###", "## "));
            Recipe.craftingRecipes.put((recipe.getKey().getId().toLowerCase()) + "_star", ShapedRecipe.createShapedRecipe(recipe.getKey(), 1, -1, requirements, items, " # ", "###", " # "));
        }
        SkyblockEnchantment.initListener();
        MinecraftServer.getGlobalEventHandler().addChild(SkyblockEnchantment.LISTENER);
    }

    static void registerMinion(IMinionData minion) {
        IMinionData.minions.put(minion.id(), minion);
        for (int i = 1; i <= minion.getLevels(); i++){
            int finalI = i;
            var item = new MinionItem(minion.name() + " " + StringUtils.toRoman(i), minion.id() + "_GENERATOR_" + i,
                                         minion.getHeadStrings()[i-1], List.of(new ItemAbility<>("Place", AbilityType.RIGHT_CLICK, event -> {
                                             if (event.block() == null) return;
                                             if (event.player().getWorldProvider() instanceof PrivateIsle privateIsle) {
                                                 if (privateIsle.addMinion(minion, finalI, new Pos(event.block().add(0.5, 1, 0.5)))) {
                                                     event.player().setItemInHand(PlayerHand.MAIN, SbItemStack.AIR);
                                                 }
                                             }
            })));
            SbItemStack.initSbItem(item);
        }
    }

    static ISbItem get(Class<? extends ISbItem> itemClass) {
        if (itemClass == BaseSbItem.class) throw new IllegalStateException("Not possible for base items!");
        try {
            return SbItemStack.raw((String) itemClass.getDeclaredMethod("getId").invoke(itemClass.getConstructor().newInstance()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static ISbItem get(Material material) {
        try {
            return SbItemStack.raw(material.key().value().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    static ISbItem fromKey(String key) {
        try {
            return SbItemStack.raw(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
