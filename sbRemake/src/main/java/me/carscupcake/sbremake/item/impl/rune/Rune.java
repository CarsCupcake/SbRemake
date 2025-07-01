package me.carscupcake.sbremake.item.impl.rune;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Listener;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.item.Material;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class Rune implements ISbItem {
    public static final Map<String, IRune<? extends Entity>> runes = new HashMap<>();

    static {
        Reflections reflections = new Reflections("me.carscupcake.sbremake.item.impl.rune.impl");
        for (Class<? extends IRune> clazz : reflections.getSubTypesOf(IRune.class)) {
            try {
                if (clazz.isInterface()) continue;
                if (clazz.isRecord()) continue;
                if (clazz.isEnum()) continue;
                Constructor<? extends IRune> constructor = clazz.getConstructor();
                IRune instance = constructor.newInstance();
                runes.put(instance.getId(), instance);
                if (instance instanceof Listener listener) {
                    MinecraftServer.getGlobalEventHandler().addChild(listener.node());
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }

    @Override
    public String getId() {
        return "RUNE";
    }

    @Override
    public String getName() {
        return "Rune";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Rune;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.SPECIAL;
    }
}
