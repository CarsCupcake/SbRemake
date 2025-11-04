package me.carscupcake.sbremake.item.impl.shard;

import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.Lore;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.Nullable;

public interface IAttributeShard {
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
}
