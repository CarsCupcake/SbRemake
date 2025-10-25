package me.carscupcake.sbremake.player.potion;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.config.KeyClass;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.network.packet.server.play.RemoveEntityEffectPacket;
import net.minestom.server.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public interface IPotion extends KeyClass {
    @Override
    default String key() {
        return getId();
    }

    String getName();

    String getId();

    RGBLike getPotionColor();

    PotionLore description();

    boolean isBuff();

    String getPrefix();

    default @Nullable PotionEffect getVanillaEffect() {
        return null;
    }

    default Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
        return new HashMap<>(0);
    }

    default void start(SkyblockPlayer player, int level, long duration) {

    }

    default void stop(SkyblockPlayer player, int level) {
        for (Map.Entry<Stat, PlayerStatEvent.PlayerStatModifier> entry : getStatModifiers((byte) level).entrySet()) {
            player.getTemporaryModifiers().removeModifier(entry.getKey(), entry.getValue());
        }
        if (getVanillaEffect() != null) {
            player.sendPacket(new RemoveEntityEffectPacket(player.getEntityId(), getVanillaEffect()));
        }
    }

    //For instant potions onle the method start will be run (duration is 0)
    default boolean isInstant() {
        return false;
    }

    byte getMaxLevel();

    HashMap<String, IPotion> potions = new HashMap<>();

    @SuppressWarnings("unused")
    static IPotion fromKey(String key) {
        return potions.get(key);
    }
}
