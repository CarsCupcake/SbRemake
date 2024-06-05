package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PlayerHeadMeta;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Map;

public interface ISbItem {
    String getId();

    String getName();

    Material getMaterial();

    ItemType getType();

    ItemRarity getRarity();

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

    default SbItemStack create() {
        ItemStack.Builder builder = ItemStack.builder(getMaterial());
        if (getMaterial() == Material.PLAYER_HEAD && this instanceof HeadWithValue value) {
            builder.meta(new PlayerHeadMeta.Builder().playerSkin(new PlayerSkin(value.value(), "")).build());
        }
        SbItemStack itemStack = SbItemStack.from(builder.set(Tag.NBT("ExtraAttributes"), NBT.Compound(mutableNBTCompound -> mutableNBTCompound.put("id", NBT.String(getId())))).build());
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
}
