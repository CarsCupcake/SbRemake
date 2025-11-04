package me.carscupcake.sbremake.item.impl.shard;

import me.carscupcake.sbremake.item.ItemRarity;

public interface IAttributeShard {
    String getDisplayName();
    ItemRarity getRarity();
    String getShardId();
    String getAbilityName();
    String getId();
    ShardCategory getCategory();
    ShardFamily[] getFamilies();
}
