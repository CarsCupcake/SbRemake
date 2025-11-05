package me.carscupcake.sbremake.item.impl.shard;

import me.carscupcake.sbremake.config.KeyClass;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.Lore;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IAttributeShard extends KeyClass {
    static @Nullable IAttributeShard fromKey(String id) {
        for (var shard : Shard.values())
            if (shard.getId().equals(id))
                return shard;
        return null;
    }

    @Override
    default String key() {
        return getId();
    }

    String getDisplayName();

    ItemRarity getRarity();

    String getShardId();

    String getAbilityName();

    String getId();

    ShardCategory getCategory();

    ShardFamily[] getFamilies();

    Lore getLore();

    Material getMaterial();

    @Nullable String getHeadValue();

    default int compareTo(@NotNull IAttributeShard shard) {
        var rarity = getRarity().compareTo(shard.getRarity());
        if (rarity != 0) return rarity;
        var myId = Integer.parseInt(getShardId().substring(1));
        var compareId = Integer.parseInt(shard.getShardId().substring(1));
        return Integer.compare(myId, compareId);
    }
}
