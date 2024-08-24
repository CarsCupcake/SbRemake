package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.crafting.CraftingIngredient;
import me.carscupcake.sbremake.item.crafting.ShapedRecipe;
import me.carscupcake.sbremake.item.requirements.CollectionRequirement;
import me.carscupcake.sbremake.util.StringUtils;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.item.component.DyedItemColor;
import net.minestom.server.item.component.HeadProfile;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.Unit;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.*;

public interface ISbItem {
    String getId();

    String getName();

    Material getMaterial();

    ItemType getType();

    ItemRarity getRarity();

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

    default boolean isUnstackable() {
        return getMaterial().maxStackSize() == 1;
    }

    default SbItemStack create() {
        ItemStack.Builder builder = ItemStack.builder(getMaterial()).set(ItemComponent.ATTRIBUTE_MODIFIERS, new AttributeList(List.of(), false));
        if (getMaterial() == Material.PLAYER_HEAD && this instanceof HeadWithValue value) {
            builder.set(ItemComponent.PROFILE, new HeadProfile(new PlayerSkin(value.value(), "")));
        }
        if (this instanceof ColoredLeather leather) {
            builder.set(ItemComponent.DYED_COLOR, new DyedItemColor(leather.color().asRGB(), false));
        }
        ItemStackModifiers modifiers = modifierBuilder();
        ItemStack item = build(modifiers.apply(builder.set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE).set(Tag.NBT("ExtraAttributes"), CompoundBinaryTag.builder().putString("id", getId()).build()))).build();
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
        for (Material material : Material.values()) {
            String name = StringUtils.connect(material.namespace().value().split("_"), true);
            SbItemStack.initSbItem(new BaseSbItem(material, name));
        }
        HashMap<ISbItem, EnchantedRecipe> recipes = new HashMap<>();
        Reflections reflections = new Reflections("me.carscupcake.sbremake.item.impl");
        for (Class<? extends ISbItem> clazz : reflections.getSubTypesOf(ISbItem.class)) {
            try {
                if (clazz.isInterface()) continue;
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
        for (Map.Entry<ISbItem, EnchantedRecipe> recipe : recipes.entrySet()) {
            Map<Character, CraftingIngredient> items = Map.of('#', new CraftingIngredient(32, ISbItem.get(recipe.getValue().base())));
            Requirement[] requirements = new Requirement[]{new CollectionRequirement(recipe.getValue().collection(), recipe.getValue().level())};
            Recipe.craftingRecipes.add(new ShapedRecipe(recipe.getKey(), 1, -1, requirements, items, "###", "## "));
            Recipe.craftingRecipes.add(new ShapedRecipe(recipe.getKey(), 1, -1, requirements, items, " # ", "###", " # "));
        }
    }

    static ISbItem get(Class<? extends ISbItem> itemClass) {
        try {
            return SbItemStack.raw((String) itemClass.getDeclaredMethod("getId").invoke(itemClass.getConstructor().newInstance()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static ISbItem get(Material material) {
        try {
            return SbItemStack.raw(material.namespace().value().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
