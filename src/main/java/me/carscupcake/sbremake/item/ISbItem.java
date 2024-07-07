package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantment;
import me.carscupcake.sbremake.util.StringUtils;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.item.component.DyedItemColor;
import net.minestom.server.item.component.HeadProfile;
import net.minestom.server.item.component.ItemBlockState;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.Unit;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if (provider != null) return provider.stats().getOrDefault(stat, 0d);
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

    default SbItemStack create() {
        ItemStack.Builder builder = ItemStack.builder(getMaterial()).set(ItemComponent.ATTRIBUTE_MODIFIERS, new AttributeList(List.of(), false));
        if (getMaterial() == Material.PLAYER_HEAD && this instanceof HeadWithValue value) {
            builder.set(ItemComponent.PROFILE, new HeadProfile(new PlayerSkin(value.value(), "")));
        }
        if (this instanceof ColoredLeather leather) {
            builder.set(ItemComponent.DYED_COLOR, new DyedItemColor(leather.color().asRGB(), false));
        }
        ItemStack item = build(modifierBuilder().apply(builder.set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE)
                .set(Tag.NBT("ExtraAttributes"), CompoundBinaryTag.builder().putString("id", getId()).build())))
                .build();
        SbItemStack itemStack = SbItemStack.from(item);
        return itemStack.update();
    }

    enum LorePlace {
        Top, AboveAbility, BelowAbility
    }

    interface StatProvider {
        Map<Stat, Double> stats();
    }

    static void init() {
        for (Material material : Material.values()) {
            String name = StringUtils.connect(material.namespace().value().split("_"), true);
            SbItemStack.initSbItem(new BaseSbItem(material, name));
        }

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
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
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
